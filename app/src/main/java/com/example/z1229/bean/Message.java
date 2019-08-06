package com.example.z1229.bean;

public class Message {
	
	private String content;
	private String type;

	public Message(String type,String content){
		this.type = type;
		this.content = content;
	}

	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
