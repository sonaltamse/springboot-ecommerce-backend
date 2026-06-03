package com.ecommerce.project.security.response;

public class MessageResponse {
    private String message;

    public MessageResponse(String s) {
        this.message = s;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
