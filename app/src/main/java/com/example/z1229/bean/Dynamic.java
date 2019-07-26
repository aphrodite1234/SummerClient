package com.example.z1229.bean;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;

public class Dynamic {

	private String type;
	private int dyid;
	private Long senderPhone;
	private String senderName;
	private Long receiverPhone;
	private String receiverName;
	private String content;
	private HashMap<String,byte[]> picture;
	private int state = 0;
	private String location;
	private String dytime;
	public int getDyid() {
		return dyid;
	}
	public void setDyid(int dyid) {
		this.dyid = dyid;
	}
	public Long getSenderPhone() {
		return senderPhone;
	}
	public void setSenderPhone(Long senderPhone) {
		this.senderPhone = senderPhone;
	}
	public Long getReceiverPhone() {
		return receiverPhone;
	}
	public void setReceiverPhone(Long receiverPhone) {
		this.receiverPhone = receiverPhone;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public HashMap<String, byte[]> getPicture() {
		return picture;
	}
	public void setPicture(HashMap<String, byte[]> picture) {
		this.picture = picture;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getDytime() { 
		return dytime;
	}
	public void setDytime(Timestamp date) {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sd = sdf.format(date);  
		this.dytime  = sd;
	}
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	public String getReceiverName() {
		return receiverName;
	}
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
