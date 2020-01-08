package io.itman.admin.service.impl;


import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import io.itman.admin.service.MenuService;
import io.itman.admin.vo.SysmenubuttonList;
import io.itman.library.util.DataGridReturn;
import io.itman.library.util.JsonUtil;
import io.itman.library.util.MenuTreeUtil;
import io.itman.library.util.StringUtil;
import io.itman.library.vo.Tree;
import io.itman.model.SysButton;
import io.itman.model.SysMenu;
import io.itman.model.SysMenubutton;
import io.itman.model.SysRolemenu;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MenuServiceImpl implements MenuService {

    /**
     * 构建树实体
     *
     * @return
     */
    @Override
    public List<Tree> buildTree() {
        List<Tree> treeList = new ArrayList<>();
        List<Record> recordList = Db.find("select id, name text,parentId pid, icon iconCls from sys_menu");
        for (Record r : recordList) {
            Tree tree = new Tree();
            tree.setId(r.get("id"));
            tree.setName(r.get("text"));
            tree.setIconCls(r.get("iconCls"));
            tree.setpId(r.get("pid"));
            treeList.add(tree);
        }
        return treeList;
    }

    /**
     * 禁止删除已绑定角色
     *
     * @param ID
     * @return
     */
    @Override
    public JsonUtil del(String ID) {
        JsonUtil result = new JsonUtil();
            for (String id : ID.split(",")) {
                List<SysRolemenu> sysRolemenuList = SysRolemenu.dao.find("select * from sys_rolemenu where menuId = " + id + "");
                if (sysRolemenuList.size() > 0) {
                    result.setResult(0);
                    result.setMessage("该菜单已绑定角色权限，禁止删除");
                    return result;
                }
                SysMenu.dao.deleteById(id);
                Db.update("delete from sys_menubutton where menuId = ?", id);
            }
            result.setMessage("删除成功");
        return result;
    }

    /**
     * 跳转菜单修改页面，数据回显
     *
     * @param ID
     * @param request
     * @return
     */
    @Override
    public void edit(int ID, HttpServletRequest request) {
        List<Record> recordList = Db.find("SELECT sb.icon buttonIcon, sb.name buttonName,smb.* from sys_menubutton smb  LEFT JOIN sys_button sb on smb.buttonId= sb.id where sb.status = 1 and  menuId ='" + ID + "'");
        List<SysMenubutton> menuButton = new ArrayList<>();
        for (Record r : recordList) {
            SysMenubutton smb = new SysMenubutton();
            smb.setId(r.get("id"));
            smb.setWindowName(r.get("windowName"));
            smb.setMenuId(r.get("menuId"));
            smb.setButtonId(r.get("buttonId"));
            smb.setButtonUrl(r.get("buttonUrl"));
            smb.setButtonSaveUrl(r.get("buttonSaveUrl"));
            smb.setWidth(r.get("width"));
            smb.setHeight(r.get("height"));
            smb.setOpenMode(r.get("openMode"));
            smb.setJsFunctionBefore(r.get("jsFunctionBefore"));
            smb.setJsFunctionAfter(r.get("jsFunctionAfter"));
            smb.setButtonName(r.get("buttonName"));
            smb.setButtonIcon(r.getStr("buttonIcon"));
            menuButton.add(smb);
        }
        SysMenu sysMenu = SysMenu.dao.findById(ID);
        request.setAttribute("sysMenu",sysMenu);
        request.setAttribute("id", ID);
        request.setAttribute("menuButton", menuButton);
    }

    /**
     * 新增、修改保存菜单关联按钮，菜单
     *
     * @param model
     * @param sysmenubuttonList
     * @return
     */
    @Override
    public JsonUtil save(SysMenu model, SysmenubuttonList sysmenubuttonList) {
        Db.update("delete from sys_menubutton where menuId = ?", model.getId());
        JsonUtil result = new JsonUtil();
        if (model.getSort() == null) {
            model.setSort(StringUtil.DEFAULT_SORT);
            if (model.getParentId() == null) {
                model.setParentId(0);
            }
        }
        if (model.getId() == 0) {
            model.setId(null);
            model.save();
            result.setMessage("恭喜您，添加成功！");
        } else {
            model.update();
            result.setMessage("恭喜您，修改成功！");
        }
        List<SysMenubutton> sysmenubuttons = sysmenubuttonList.getSysmenubuttons();
        if (sysmenubuttons != null && sysmenubuttons.size() > 0) {
            for (SysMenubutton sysmenubutton : sysmenubuttons) {
                if (null != sysmenubutton.getButtonId()) {
                    sysmenubutton.setMenuId(model.getId());
                    sysmenubutton.save();
                }
            }
        }
        return result;
    }

    /**
     * 新增、修改保存菜单关联按钮，菜单
     *
     * @param request
     * @return
     */
    @Override
    public void MenuButton(String id, HttpServletRequest request) {
        List<SysButton> buttonList = SysButton.dao.find("select * from sys_button where status = 1 ");
        request.setAttribute("buttonList", buttonList);
        return;
    }

    /**
     * 获取菜单树
     *
     * @return
     */
    @Override
    public List<Object> getMenuTree() {
        MenuTreeUtil menuTree = new MenuTreeUtil();
        List<Object> menuList = new ArrayList<Object>();
        try {
            List<Tree> list = this.buildTree();
            menuList = menuTree.menuList(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return menuList;
    }

    @Override
    public DataGridReturn DataList(Map<String, Object> map) {
        SqlPara sqlPara = Db.getSqlPara("getMenuList", Kv.by("map", map));
        List<Record> rList=  Db.find(sqlPara);
        List<Map> list=new ArrayList<>();
        for (Record r:rList) {
            Map m=r.getColumns();
            if(r.getInt("parentId")!=0){
                m.put("_parentId",r.getInt("parentId"));
            }
            list.add(m);
        }
        DataGridReturn dgr=new DataGridReturn(list.size(), list);
        return dgr;
    }
}
