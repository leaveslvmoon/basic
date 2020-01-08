package io.itman.admin.service.impl;


import com.jfinal.plugin.activerecord.Db;
import io.itman.admin.service.RoleService;
import io.itman.admin.vo.RoleMenu;
import io.itman.admin.vo.User;
import io.itman.library.util.JsonUtil;
import io.itman.library.util.RequestUtil;
import io.itman.model.*;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {
    @Override
    public JsonUtil result(int roleId, RoleMenu roleMenu) {
        JsonUtil result = new JsonUtil();
        //删除该角色已经有的权限
        Db.update("delete from sys_rolemenu where roleId = ?", roleId);
        //一级菜单List
        Set<Integer> firstMenuList = new HashSet<>();
        //二级菜单List
        List<String> secondMenuList = new ArrayList<>();
        //三级菜单List
        List<String> thirdMenuList = new ArrayList<>();
        //二级按钮List
        List<String> secondButtonList = new ArrayList<>();
        //三级按钮List
        List<String> thirdButtonList = new ArrayList<>();
        //判断二级菜单是否为空,若为空 不做处理
        if (roleMenu.getSecondMenu() != null) {
            secondMenuList = Arrays.asList(roleMenu.getSecondMenu().split(","));
            for (String secondMenu : secondMenuList) {
                String secondButtonId = "";
                if (roleMenu.getSecondButton() != null) {
                    //判断二级菜单下按钮是否为空，若为空 secondButtonId=""
                    secondButtonList = Arrays.asList(roleMenu.getSecondButton().split(","));
                    for (String secondButton : secondButtonList) {
                        if (secondButton.split(":")[0].equals(secondMenu)) {
                            secondButtonId += secondButton.split(":")[1] + ",";
                        }
                    }
                }
                //根据当前菜单ID取父级ID
                int parentId = SysMenu.dao.findById(Integer.parseInt(secondMenu)).getParentId();
                //将所有二级菜单的父级ID放入set集合去重
                firstMenuList.add(parentId);
                SysRolemenu sysRolemenu = new SysRolemenu();
                sysRolemenu.setButtonId(secondButtonId);
                sysRolemenu.setMenuId(Integer.parseInt(secondMenu));
                sysRolemenu.setParentId(parentId);
                sysRolemenu.setRoleId(roleId);
                sysRolemenu.save();
            }
            for (int firstMenuId : firstMenuList) {
                SysRolemenu sysRolemenu = new SysRolemenu();
                sysRolemenu.setRoleId(roleId);
                sysRolemenu.setMenuId(firstMenuId);
                sysRolemenu.setParentId(0);
                sysRolemenu.save();
            }
            if (roleMenu.getThirdMenu() != null) {
                //判断三级菜单是否为空，若为空 不增加三级菜单
                thirdMenuList = Arrays.asList(roleMenu.getThirdMenu().split(","));
                for (String thirdMenu : thirdMenuList) {
                    String thirdButtonId = "";
                    if (roleMenu.getThirdButton() != null) {
                        thirdButtonList = Arrays.asList(roleMenu.getThirdButton().split(","));
                        for (String thirdButton : thirdButtonList) {
                            //判断三级菜单下按钮是否为空，若为空 thirdButtonId=""
                            if (thirdButton.split(":")[0].equals(thirdMenu)) {
                                thirdButtonId += thirdButton.split(":")[1] + ",";
                            }
                        }
                    }
                    int parentId = SysMenu.dao.findById(Integer.parseInt(thirdMenu)).getParentId();
                    SysRolemenu sysRolemenu = new SysRolemenu();
                    sysRolemenu.setButtonId(thirdButtonId);
                    sysRolemenu.setMenuId(Integer.parseInt(thirdMenu));
                    sysRolemenu.setParentId(parentId);
                    sysRolemenu.setRoleId(roleId);
                    sysRolemenu.save();
                }
            }
        }
        //更新Session
        updateSession(roleId);
        result.setMessage("恭喜您，修改成功！");
        return result;
    }

    @Override
    public void goPermissions(String ID, HttpServletRequest request) {
        List<SysButton> buttons = SysButton.dao.find("SELECT sb.*, menuId mId from sys_menubutton smb LEFT JOIN sys_button sb on smb.buttonId=sb.id where sb.status = 1");
        for (SysButton button : buttons) {
            button.setMenuId(button.get("mId"));
        }
        User user = (User) RequestUtil.getSession().getAttribute("user");
        int currentRoleId = user.getAdministrator().getRoleId();
        String sql = "";
        if(1==currentRoleId){
            sql = "SELECT * FROM sys_menu where status = 1 ";
        }else {
            sql = "SELECT m.* FROM sys_rolemenu rm LEFT JOIN sys_menu m ON rm.menuId = m.id where status = 1 and rm.roleId = "+currentRoleId+"";
        }
        //一级菜单
        List<SysMenu> menus = SysMenu.dao.find(sql);
        //二级菜单
        List<SysMenu> menus2 = new ArrayList<>();
        //三级菜单
        List<SysMenu> menus3 = new ArrayList<>();
        for (SysMenu menu : menus) {
            //判断是否二级菜单（二级菜单的上级的partenId一定为0）
            //若为一级菜单,要判空处理
            SysMenu menu1 = SysMenu.dao.findById(menu.getParentId());
            if (menu1 != null && menu1.getParentId() == 0) {
                menus2.add(menu);
            } else if (SysMenu.dao.find("select * from sys_menu where status = 1 and  parentId = " + menu.getId() + "").size() == 0) {
                //判断是否为三级菜单（在没有四级菜单情况下。三级菜单的id本身作为查询（partentId）条件，一定没有记录）
                menus3.add(menu);
            }
            List<SysRolemenu> rolemenus = SysRolemenu.dao.find("SELECT buttonId,roleId from sys_rolemenu where menuId ='" + menu.getId() + "'" + "and roleId ='" + ID + "'");
            for (SysRolemenu rolemenu : rolemenus) {
                if (rolemenu.getButtonId() != null && !rolemenu.getButtonId().equals("")) {
                    String[] buttonIdss = rolemenu.getButtonId().split(",");
                    List<Integer> listIds = Arrays.asList(buttonIdss).stream().map(s -> Integer.parseInt(s.trim())).collect(Collectors.toList());
                    menu.setButtonIdList(listIds);
                }
                menu.setRoleId(rolemenu.getRoleId());
                menu.update();
            }
        }
        menus.removeAll(menus2);
        menus.removeAll(menus3);
        request.setAttribute("menus", menus);
        request.setAttribute("menus2", menus2);
        request.setAttribute("menus3", menus3);
        request.setAttribute("buttons", buttons);
        request.setAttribute("roleId", Integer.parseInt(ID));
    }

    @Override
    public JsonUtil del(String ID) {
        JsonUtil result = new JsonUtil();
        for (String id : ID.split(",")) {
            int size = Db.find("select * from sys_administrator where status = 1 and isDelete = 0 and roleId = ?", id).size();
            if(size>0){
                result.setResult(0);
                result.setMessage("该角色已绑定用户，禁止删除");
                return result;
            }
            SysRole.dao.deleteById(id);
            Db.update("delete from sys_rolemenu where roleId = ?", id);
        }
        result.setMessage("删除成功");
        return result;
    }


    private void updateSession(int roleId){
        HttpSession session = RequestUtil.getSession();
        User user = (User)RequestUtil.getSession().getAttribute("user");
        SysAdministrator administrator = user.getAdministrator();
        List<SysRolemenu> rolemenuList = SysRolemenu.dao.find("select * from sys_rolemenu where roleId ='" + roleId + "'");
        List<SysMenu> sysmenuList = SysMenu.dao.find("SELECT sm.*,(SELECT menuId from sys_role where id = "+roleId+" ) defaultMenuId from sys_rolemenu rm left JOIN sys_menu sm   on rm.menuId = sm.id where roleId =" + roleId + "");
        List<SysMenu> topmenuList = sysmenuList.stream().filter(s -> s.getParentId()==0).collect(Collectors.toList());//顶部父菜单
        user = new User(administrator,rolemenuList,sysmenuList,topmenuList);
        session.setAttribute("user", user);
    }
}
