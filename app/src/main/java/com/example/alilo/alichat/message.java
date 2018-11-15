package com.example.alilo.alichat;

/**
 * Created by alilo on 05/11/2018.
 */

public class message {
    private String message ,type;
    private String seen;
    private String time ;

    public message(String message, String type, String seen, String time) {
        this.message = message;
        this.type = type;

        this.seen = seen;
        this.time = time;
    }


    public message() {
    }


    public String isSeen() {
        return seen;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSeen() {
        return seen;
    }

    public void setSeen(String seen) {
        this.seen = seen;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}
