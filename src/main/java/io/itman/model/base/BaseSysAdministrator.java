package io.itman.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseSysAdministrator<M extends BaseSysAdministrator<M>> extends Model<M> implements IBean {

	public M setId(java.lang.Integer id) {
		set("id", id);
		return (M)this;
	}
	
	public java.lang.Integer getId() {
		return getInt("id");
	}

	public M setUserName(java.lang.String userName) {
		set("userName", userName);
		return (M)this;
	}
	
	public java.lang.String getUserName() {
		return getStr("userName");
	}

	public M setUserPsd(java.lang.String userPsd) {
		set("userPsd", userPsd);
		return (M)this;
	}
	
	public java.lang.String getUserPsd() {
		return getStr("userPsd");
	}

	public M setRealName(java.lang.String realName) {
		set("realName", realName);
		return (M)this;
	}
	
	public java.lang.String getRealName() {
		return getStr("realName");
	}

	public M setMobile(java.lang.String mobile) {
		set("mobile", mobile);
		return (M)this;
	}
	
	public java.lang.String getMobile() {
		return getStr("mobile");
	}

	public M setRoleId(java.lang.Integer roleId) {
		set("roleId", roleId);
		return (M)this;
	}
	
	public java.lang.Integer getRoleId() {
		return getInt("roleId");
	}

	public M setPsdErrorCount(java.lang.Integer psdErrorCount) {
		set("psdErrorCount", psdErrorCount);
		return (M)this;
	}
	
	public java.lang.Integer getPsdErrorCount() {
		return getInt("psdErrorCount");
	}

	public M setLoginCount(java.lang.Integer loginCount) {
		set("loginCount", loginCount);
		return (M)this;
	}
	
	public java.lang.Integer getLoginCount() {
		return getInt("loginCount");
	}

	public M setLoginIp(java.lang.String loginIp) {
		set("loginIp", loginIp);
		return (M)this;
	}
	
	public java.lang.String getLoginIp() {
		return getStr("loginIp");
	}

	public M setLoginCity(java.lang.String loginCity) {
		set("loginCity", loginCity);
		return (M)this;
	}
	
	public java.lang.String getLoginCity() {
		return getStr("loginCity");
	}

	public M setStatus(java.lang.Integer status) {
		set("status", status);
		return (M)this;
	}
	
	public java.lang.Integer getStatus() {
		return getInt("status");
	}

	public M setIsDelete(java.lang.Integer isDelete) {
		set("isDelete", isDelete);
		return (M)this;
	}
	
	public java.lang.Integer getIsDelete() {
		return getInt("isDelete");
	}

	public M setLoginDate(java.util.Date loginDate) {
		set("loginDate", loginDate);
		return (M)this;
	}
	
	public java.util.Date getLoginDate() {
		return get("loginDate");
	}

	public M setCreateBy(java.lang.Integer createBy) {
		set("createBy", createBy);
		return (M)this;
	}
	
	public java.lang.Integer getCreateBy() {
		return getInt("createBy");
	}

}
