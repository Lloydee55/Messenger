package com.example.messenger;

public class Message {

    private String text;
    private String id;
    private String receiverId;

    public Message(){

    }

    public Message(String text, String id, String received) {
        this.text = text;
        this.id = id;
        this.receiverId = received;
    }

    public String getText() {
        return text;
    }

    public String getId() {
        return id;
    }

    public String getReceiverId() {
        return receiverId;
    }
}

