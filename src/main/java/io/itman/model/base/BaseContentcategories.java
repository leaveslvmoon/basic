package io.itman.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseContentcategories<M extends BaseContentcategories<M>> extends Model<M> implements IBean {

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

	public M setParentId(java.lang.Integer parentId) {
		set("parentId", parentId);
		return (M)this;
	}
	
	public java.lang.Integer getParentId() {
		return getInt("parentId");
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

	public M setAllowDelete(java.lang.Integer allowDelete) {
		set("allowDelete", allowDelete);
		return (M)this;
	}
	
	public java.lang.Integer getAllowDelete() {
		return getInt("allowDelete");
	}

	public M setIsDelete(java.lang.Integer isDelete) {
		set("isDelete", isDelete);
		return (M)this;
	}
	
	public java.lang.Integer getIsDelete() {
		return getInt("isDelete");
	}

	public M setRemarks(java.lang.String remarks) {
		set("remarks", remarks);
		return (M)this;
	}
	
	public java.lang.String getRemarks() {
		return getStr("remarks");
	}

	public M setPicurl(java.lang.String picurl) {
		set("picurl", picurl);
		return (M)this;
	}
	
	public java.lang.String getPicurl() {
		return getStr("picurl");
	}

}
