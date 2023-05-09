package com.project.seedle.messages;

public class MessageList {

    private String name,lastMessage,mobile, profilePic, chatKey;

    private int unseenMessages;


    public MessageList(String name, String lastMessage, int unseenMessages, String mobile, String profilePic, String chatKey) {
        this.name = name;

        this.lastMessage = lastMessage;
        this.unseenMessages = unseenMessages;
        this.mobile = mobile;
        this.profilePic = profilePic;
        this.chatKey = chatKey;
    }



    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }


    public String getProfilePic() {
        return profilePic;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public int getUnseenMessages() {
        return unseenMessages;
    }

    public String getChatKey() {
        return chatKey;
    }
}
