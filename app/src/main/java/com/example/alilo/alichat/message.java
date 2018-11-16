package com.example.alilo.alichat;

/**
 * Created by alilo on 05/11/2018.
 */

public class message {
    private String message ,type,from;
    private boolean seen;
    private long time ;

    public message(String message, String type,String from, boolean seen, long time) {
        this.message = message;
        this.type = type;
        this.from = from ;
        this.seen = seen;
        this.time = time;
    }


    public message() {
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public boolean isSeen() {
        return seen;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}
