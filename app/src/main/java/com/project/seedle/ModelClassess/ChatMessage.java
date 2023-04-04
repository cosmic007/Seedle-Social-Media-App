package com.project.seedle.ModelClassess;

public class ChatMessage {
    private String messageText;
    private String senderName;
    private long timestamp;

    private  String profileurl;


    public ChatMessage() {
        // Required empty constructor
    }

    public ChatMessage(String messageText, String senderName, long timestamp , String profileurl) {
        this.messageText = messageText;
        this.senderName = senderName;
        this.timestamp = timestamp;
        this.profileurl = profileurl;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getProfileurl(){
        return profileurl;
    }

    public void getProfileurl(String profileurl){ this.profileurl =profileurl;
    }
}