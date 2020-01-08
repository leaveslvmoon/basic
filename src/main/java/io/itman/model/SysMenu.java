package io.itman.model;

import io.itman.model.base.BaseSysMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class SysMenu extends BaseSysMenu<SysMenu> {
	public static final SysMenu dao = new SysMenu().dao();


	private Integer roleId;

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	private List<Integer> buttonIdList;

	public List<Integer> getButtonIdList() {
		return buttonIdList;
	}

	public void setButtonIdList(List<Integer> buttonIdList) {
		this.buttonIdList = buttonIdList;
	}

	private List<SysMenu> children=new ArrayList<SysMenu>();

	public void addChild(SysMenu sysMenu){
		children.add(sysMenu);
	}

	public List<SysMenu> getChildren() {
		return children;
	}

	public void setChildren(List<SysMenu> children) {
		this.children = children;
	}
}
