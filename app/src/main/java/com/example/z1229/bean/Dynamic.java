package com.example.z1229.bean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Dynamic {

	private String type;
	private int dyid;
	private Long senderPhone;
	private String senderName;
	private String b_type;
	private Long receiverPhone;
	private String receiverName;
	private String content;
	private ArrayList<byte[]> picture;
	private ArrayList<String> url;
	private int comment_count;
	private int zan_count;
	private int zan_bool;
	private String dytime;
	private int state;
	private int brocount;

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
	public ArrayList<byte[]> getPicture() {
		return picture;
	}
	public void setPicture(ArrayList<byte[]> picture) {
		this.picture = picture;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getB_type() {
		return b_type;
	}
	public void setB_type(String b_type) {
		this.b_type = b_type;
	}
	public String getDytime() { 
		return dytime;
	}
	public void setDytime(Date date) {
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
	public int getComment_count() {
		return comment_count;
	}
	public void setComment_count(int comment_count) {
		this.comment_count = comment_count;
	}
	public int getZan_count() {
		return zan_count;
	}
	public void setZan_count(int zan_count) {
		this.zan_count = zan_count;
	}
	public int getZan_bool() {
		return zan_bool;
	}
	public void setZan_bool(int zan_bool) {
		this.zan_bool = zan_bool;
	}

	public ArrayList<String> getUrl() {
		return url;
	}

	public void setUrl(ArrayList<String> url) {
		this.url = url;
	}

	public void setBrocount(int brocount) {
		this.brocount = brocount;
	}

	public int getBrocount() {
		return brocount;
	}
}
