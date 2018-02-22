package com.george888.mina.hereguide.pojo;

/**
 * Created by minageorge on 9/5/17.
 */

public class Review {
    private String user;
    private String rate = "0";
    private String pic = "";
    private String text;
    private String time;

    public Review(String user, String rate, String pic, String text, String time) {
        this.user = user;
        this.rate = rate;
        this.pic = pic;
        this.text = text;
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
