package com.appjishu.fpush.core.model;

import java.io.Serializable;

public class MsgData implements Serializable {
	private static final long serialVersionUID = 1L;
    private String title;
    private String description;
    
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}  
}
