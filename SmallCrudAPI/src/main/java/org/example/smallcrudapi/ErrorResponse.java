package org.example.smallcrudapi;

public class ErrorResponse {

    public ErrorResponse (int status, String message){
        this.status = status;
        this.message = message;
    }

    private int status;
    private String message;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
