package io.itman.admin.vo;



import io.itman.model.SysAdministrator;
import io.itman.model.SysMenu;
import io.itman.model.SysRolemenu;

import java.util.List;

/*
   登录获取对象
 */
public class User {
    private SysAdministrator administrator;
    private List<SysRolemenu> rolemenuList;
    private List<SysMenu> sysmenuList;
    private List<SysMenu> topmenuList;

    public SysAdministrator getAdministrator() {
        return administrator;
    }

    public void setAdministrator(SysAdministrator administrator) {
        this.administrator = administrator;
    }

    public List<SysRolemenu> getRolemenuList() {
        return rolemenuList;
    }

    public void setRolemenuList(List<SysRolemenu> rolemenuList) {
        this.rolemenuList = rolemenuList;
    }

    public List<SysMenu> getSysmenuList() {
        return sysmenuList;
    }

    public void setSysmenuList(List<SysMenu> sysmenuList) {
        this.sysmenuList = sysmenuList;
    }

    public List<SysMenu> getTopmenuList() {
        return topmenuList;
    }

    public void setTopmenuList(List<SysMenu> topmenuList) {
        this.topmenuList = topmenuList;
    }

    public User(SysAdministrator administrator, List<SysRolemenu> rolemenuList,List<SysMenu> sysmenuList, List<SysMenu> topmenuList) {
        this.administrator = administrator;
        this.rolemenuList = rolemenuList;
        this.sysmenuList = sysmenuList;
        this.topmenuList = topmenuList;
    }
}
