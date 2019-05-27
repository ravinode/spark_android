package com.kira.spark.bean;

/**
 * Created by ravii on 23/04/2017.
 */

public class Menu {

    private String name;
    private String desc;
    private String id;
    private String thumbnail;
    private Double price;
    private Double actualPrice;
    private String currency;
    private int qty;

    public Menu()
    {

    }
    public Menu(String name, String desc, String id, String thumbnail, Double price, String currency) {
        this.name = name;
        this.desc = desc;
        this.id = id;
        this.thumbnail = thumbnail;
        this.price = price;
        this.currency = currency;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public Double getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(Double actualPrice) {
        this.actualPrice = actualPrice;
    }
}


