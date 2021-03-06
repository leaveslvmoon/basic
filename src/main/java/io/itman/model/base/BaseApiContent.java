package io.itman.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseApiContent<M extends BaseApiContent<M>> extends Model<M> implements IBean {

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

	public M setDes(java.lang.String des) {
		set("des", des);
		return (M)this;
	}
	
	public java.lang.String getDes() {
		return getStr("des");
	}

	public M setType(java.lang.Integer type) {
		set("type", type);
		return (M)this;
	}
	
	public java.lang.Integer getType() {
		return getInt("type");
	}

	public M setMethod(java.lang.String method) {
		set("method", method);
		return (M)this;
	}
	
	public java.lang.String getMethod() {
		return getStr("method");
	}

	public M setUrl(java.lang.String url) {
		set("url", url);
		return (M)this;
	}
	
	public java.lang.String getUrl() {
		return getStr("url");
	}

	public M setRequestPara(java.lang.String requestPara) {
		set("requestPara", requestPara);
		return (M)this;
	}
	
	public java.lang.String getRequestPara() {
		return getStr("requestPara");
	}

	public M setRequestDes(java.lang.String requestDes) {
		set("requestDes", requestDes);
		return (M)this;
	}
	
	public java.lang.String getRequestDes() {
		return getStr("requestDes");
	}

	public M setResponsePara(java.lang.String responsePara) {
		set("responsePara", responsePara);
		return (M)this;
	}
	
	public java.lang.String getResponsePara() {
		return getStr("responsePara");
	}

	public M setResponseDes(java.lang.String responseDes) {
		set("responseDes", responseDes);
		return (M)this;
	}
	
	public java.lang.String getResponseDes() {
		return getStr("responseDes");
	}

	public M setUpdateTime(java.util.Date updateTime) {
		set("updateTime", updateTime);
		return (M)this;
	}
	
	public java.util.Date getUpdateTime() {
		return get("updateTime");
	}

}
