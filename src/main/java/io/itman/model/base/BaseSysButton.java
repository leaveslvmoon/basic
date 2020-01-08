package io.itman.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseSysButton<M extends BaseSysButton<M>> extends Model<M> implements IBean {

	public M setId(java.lang.Integer id) {
		set("id", id);
		return (M)this;
	}
	
	public java.lang.Integer getId() {
		return getInt("id");
	}

	public M setName(java.lang.String name) {
		set("name", name);
		return (M)this;
	}
	
	public java.lang.String getName() {
		return getStr("name");
	}

	public M setMethodName(java.lang.String methodName) {
		set("methodName", methodName);
		return (M)this;
	}
	
	public java.lang.String getMethodName() {
		return getStr("methodName");
	}

	public M setIcon(java.lang.String icon) {
		set("icon", icon);
		return (M)this;
	}
	
	public java.lang.String getIcon() {
		return getStr("icon");
	}

	public M setSort(java.lang.Integer sort) {
		set("sort", sort);
		return (M)this;
	}
	
	public java.lang.Integer getSort() {
		return getInt("sort");
	}

	public M setStatus(java.lang.Integer status) {
		set("status", status);
		return (M)this;
	}
	
	public java.lang.Integer getStatus() {
		return getInt("status");
	}

	public M setValue(java.lang.Long value) {
		set("value", value);
		return (M)this;
	}
	
	public java.lang.Long getValue() {
		return getLong("value");
	}

	public M setOpenMode(java.lang.Integer openMode) {
		set("openMode", openMode);
		return (M)this;
	}
	
	public java.lang.Integer getOpenMode() {
		return getInt("openMode");
	}

}
