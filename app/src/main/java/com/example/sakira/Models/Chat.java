package com.example.sakira.Models;

public class Chat {
    private String sender;
    private String receiver;
    private String message;
    private String algo;

    public Chat(String sender, String receiver, String message,String algo) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.algo = algo;
    }

    public String getAlgo() {
        return algo;
    }

    public void setAlgo(String algo) {
        this.algo = algo;
    }

    public Chat() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
