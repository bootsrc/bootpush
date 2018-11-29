package com.appjishu.fpush.server.entity;

import java.io.Serializable;
import java.util.Date;

public class AppAccount implements Serializable {
	private static final long serialVersionUID = 2085724763291169990L;

	private long appId;
	private String appKey;
	private String appSecretKey;
	private String keyToken;
	private String secretToken;
	private Date createTime;
	private Date updateTime;
	private Date keyTokenExpire;
	private Date secretTokenExpire;
	private String mobilePhone;
	
	public long getAppId() {
		return appId;
	}
	public void setAppId(long appId) {
		this.appId = appId;
	}
	public String getAppKey() {
		return appKey;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	public String getAppSecretKey() {
		return appSecretKey;
	}
	public void setAppSecretKey(String appSecretKey) {
		this.appSecretKey = appSecretKey;
	}
	public String getKeyToken() {
		return keyToken;
	}
	public void setKeyToken(String keyToken) {
		this.keyToken = keyToken;
	}
	public String getSecretToken() {
		return secretToken;
	}
	public void setSecretToken(String secretToken) {
		this.secretToken = secretToken;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Date getKeyTokenExpire() {
		return keyTokenExpire;
	}
	public void setKeyTokenExpire(Date keyTokenExpire) {
		this.keyTokenExpire = keyTokenExpire;
	}
	public Date getSecretTokenExpire() {
		return secretTokenExpire;
	}
	public void setSecretTokenExpire(Date secretTokenExpire) {
		this.secretTokenExpire = secretTokenExpire;
	}
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
}
