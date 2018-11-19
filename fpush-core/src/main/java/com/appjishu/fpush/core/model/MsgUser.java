package com.appjishu.fpush.core.model;

import java.io.Serializable;

public class MsgUser implements Serializable {
	private static final long serialVersionUID = 1L;
	private String alias;
	private String account;
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}	
}
