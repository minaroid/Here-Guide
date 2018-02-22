package com.george888.mina.hereguide.pojo;

/**
 * Created by minageorge on 1/22/18.
 */

public class HomeType {
    private String title;
    private String type;
    private int pic;

    public HomeType(String title, String type, int pic) {
        this.setTitle(title);
        this.setType(type);

        this.setPic(pic);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPic() {
        return pic;
    }

    public void setPic(int pic) {
        this.pic = pic;
    }

    @Override
    public String toString() {
        return "title : " + getTitle() + " , " + "type : " + getType() + "count : ";
    }
}
