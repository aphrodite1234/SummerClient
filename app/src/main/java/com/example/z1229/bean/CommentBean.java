package com.example.z1229.bean;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CommentBean {
    private int id;
    private int dyId;
    private String type;
    private String photo;
    private Long senderPhone;
    private String senderName;
    private Long receiverPhone;
    private String receiverName;
    private String dateTime;
    private String content;
    private int zanCount;
    private int zanBool;
    private int state;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDyId() {
        return dyId;
    }

    public void setDyId(int dyId) {
        this.dyId = dyId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPhoto() {
        return photo;
    }

    public Long getSenderPhone() {
        return senderPhone;
    }

    public void setSenderPhone(Long senderPhone) {
        this.senderPhone = senderPhone;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public void setReceiverPhone(Long receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public Long getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.dateTime = format.format(dateTime);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getZanCount() {
        return zanCount;
    }

    public void setZanCount(int zanCount) {
        this.zanCount = zanCount;
    }

    public int getZanBool() {
        return zanBool;
    }

    public void setZanBool(int zanBool) {
        this.zanBool = zanBool;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
