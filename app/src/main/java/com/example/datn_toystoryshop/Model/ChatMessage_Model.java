package com.example.datn_toystoryshop.Model;

public class ChatMessage_Model {
    private String senderId;
    private String receiverId;
    private String message;
    private String chatType;

    public ChatMessage_Model(String senderId, String receiverId, String message, String chatType) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.chatType = chatType;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getChatType() {
        return chatType;
    }

    public void setChatType(String chatType) {
        this.chatType = chatType;
    }
}
