package io.itman.admin.service.impl;


import com.alibaba.fastjson.JSON;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import io.itman.admin.service.LoginService;
import io.itman.admin.vo.User;
import io.itman.library.chunzhenip.IPSeeker;
import io.itman.library.macUtil.GetMacAddress;
import io.itman.library.util.IpUtil;
import io.itman.library.util.JsonUtil;
import io.itman.library.util.MD5;
import io.itman.library.util.TreeUtil;
import io.itman.library.vo.MenuTree;
import io.itman.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public String login(HttpServletRequest request) {
        String ip = IpUtil.getIp(request);
        //获取基础信息
        SysBasicinfo basicinfo = SysBasicinfo.dao.findFirst("select * from sys_basicinfo");
        //校验ip
//        if(basicinfo.getIp()==1){
//            List<IpWhitelist> ipWhitelists = IpWhitelist.dao.find("select * from ip_whitelist where status = 1");
//            ipWhitelists = ipWhitelists.stream().filter(ipWhitelist ->ipWhitelist.getIp().equals(ip)).collect(Collectors.toList());
//            if(ipWhitelists.size()<1){
//                request.setAttribute("message", "很抱歉，请用绑定的ip地址登录");
//                return "forward:/admin/login";
//            }
//        }
        //校验mac
//        if(basicinfo.getMac()==1){
//            String mac = GetMacAddress.getMacAddress(ip);
//            List<MacWhitelist> macWhitelists = MacWhitelist.dao.find("select * from mac_whitelist where status = 1");
//            macWhitelists = macWhitelists.stream().filter(ipWhitelist ->ipWhitelist.getMac().equals(mac)).collect(Collectors.toList());
//            if(macWhitelists.size()<1){
//                request.setAttribute("message", "很抱歉，请用绑定的mac地址登录");
//                return "forward:/admin/login";
//            }
//        }
        HttpSession session = request.getSession();
        String userName = request.getParameter("userName");
        String passWord = MD5.getMD5(request.getParameter("userPsd"));
        SysAdministrator administrator = SysAdministrator.dao.findFirst("SELECT * from sys_administrator where userName = '" + userName + "' and isDelete = 0");
        if (administrator == null) {
            request.setAttribute("message", "用户名不存在");
            return "forward:/admin/login";
        } else if (administrator.getStatus() == 0) {
            request.setAttribute("message", "用户被禁用");
            return "forward:/admin/login";
        }else if(SysRole.dao.findById(administrator.getRoleId()).getStatus()==0){
            request.setAttribute("message", "角色被禁用");
            return "forward:/admin/login";
        }
        if (basicinfo.getPsdCount() < administrator.getPsdErrorCount() && stringRedisTemplate.hasKey("lock")) {
            if (!administrator.getUserPsd().equals(passWord)) {
                administrator.setPsdErrorCount(administrator.getPsdErrorCount() + 1).update();
                //向redis里存入数据和设置缓存时间
                stringRedisTemplate.opsForValue().set(administrator.getUserName(), "lock", administrator.getPsdErrorCount() * basicinfo.getPsdTime(), TimeUnit.MINUTES);
                request.setAttribute("message", "登录失败多次，账户锁定" + administrator.getPsdErrorCount() * basicinfo.getPsdTime() + "分钟");
            }
            request.setAttribute("message", "登录失败多次，账户锁定" + stringRedisTemplate.getExpire(administrator.getUserName(), TimeUnit.MINUTES) + "分钟");
            return "forward:/admin/login";
        } else if (!administrator.getUserPsd().equals(passWord)) {
            administrator.setPsdErrorCount(administrator.getPsdErrorCount() + 1).update();
            request.setAttribute("message", "密码错误");
            //stringRedisTemplate.delete("lock");//根据key删除缓存
            if (basicinfo.getPsdCount() < administrator.getPsdErrorCount() && basicinfo.getPsdCount() != 0) {
                stringRedisTemplate.opsForValue().set(administrator.getUserName(), "lock", administrator.getPsdErrorCount() * basicinfo.getPsdTime(), TimeUnit.MINUTES);
            }
            return "forward:/admin/login";
        }
        IPSeeker ips = new IPSeeker("qqwry.dat", "./chunzhen/");
        administrator.setLoginCount(administrator.getLoginCount() + 1);
        administrator.setLoginDate(new Date());
        administrator.setPsdErrorCount(0);
        administrator.setLoginIp(ip);
        administrator.setLoginCity(ips.getIPLocation(ip).getCountry());
        administrator.update();
        int roleId = administrator.getRoleId();
        List<SysRolemenu> rolemenuList = SysRolemenu.dao.find("select * from sys_rolemenu where roleId ='" + roleId + "'");
        List<SysMenu> sysmenuList = SysMenu.dao.find("SELECT sm.*,(SELECT menuId from sys_role where id = "+roleId+" ) defaultMenuId from sys_rolemenu rm left JOIN sys_menu sm   on rm.menuId = sm.id where roleId =" + roleId + "");
        //顶部父菜单
        List<SysMenu> topmenuList = sysmenuList.stream().filter(s -> s.getParentId()==0).collect(Collectors.toList());
        User user = new User(administrator,rolemenuList,sysmenuList,topmenuList);
        session.setMaxInactiveInterval(basicinfo.getPsdSessionOut() * 60);
        session.setAttribute("user", user);
        return "redirect:/admin/index";
    }

    @Override
    public void index(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        request.setAttribute("menuList", user.getSysmenuList());
        request.setAttribute("topmemuList", user.getTopmenuList());
        List<SysMenu> sysMenuList = user.getSysmenuList();
        TreeUtil modelTree = new TreeUtil();
        List<MenuTree> treeList=new ArrayList<>();
        for(SysMenu menu:sysMenuList){
            MenuTree tree =new MenuTree();
            tree.setId(menu.getId());
            String icon="<i class=\"fa "+menu.getIcon()+"\"></i>&nbsp&nbsp";
            tree.setName(menu.getName());
            tree.setTitle(menu.getName());
            tree.setpId(menu.getParentId());
            //tree.setIconCls(menu.getIcon());
            tree.setIcon(menu.getIcon());
            tree.setUrl(menu.getUrl());
            treeList.add(tree);
        }
        List<List<Object>> lists = modelTree.treeModelList(treeList);
        request.setAttribute("modelTree",JSON.toJSONString(lists));
    }

    @Override
    public JsonUtil save(HttpServletRequest request, String password, String newPwd, String confirmPwd) {
        JsonUtil result = new JsonUtil();
        User user = (User) request.getSession().getAttribute("user");
        Integer userId = user.getAdministrator().getId();
        Record userPsd = Db.findFirst("select userPsd from sys_administrator where id = ?", userId);
        String userpassword = userPsd.get("userPsd");
        password = MD5.getMD5(password);
        if (userpassword.equals(password)) {
            if (newPwd.equals(confirmPwd)) {
                newPwd = MD5.getMD5(newPwd);
                int count = Db.update("update sys_administrator set userPsd = ? where id = ?", newPwd, userId);
                if (count > 0) {
                    result.setMessage("恭喜您，修改成功！");
                    return result;
                }
            }
            result.setMessage("新密码与确认密码不一致！");
            return result;
        }
        result.setMessage("密码错误！");
        return null;
    }
}
