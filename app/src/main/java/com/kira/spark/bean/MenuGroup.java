package com.kira.spark.bean;

/**
 * Created by Lincoln on 18/05/16.
 */
public class MenuGroup {
    private String name;
    private int id;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    private String key;
    private String thumbnail;

    public MenuGroup() {
    }

    public MenuGroup(String name, int id, String thumbnail) {
        this.name = name;
        this.id = id;
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
