package com.kira.spark.bean;

/**
 * Created by ravii on 14/05/2017.
 */

public class Order {
    private String menu_id;
    private String menu_name;
    private String price;
    private String count;
    private String spicyLevel;
    private String desc;
    private String tableNo;


    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMenu_name() {
        return menu_name;
    }

    public void setMenu_name(String menu_name) {
        this.menu_name = menu_name;
    }

    public String getMenu_id() {
        return menu_id;
    }

    public void setMenu_id(String menu_id) {
        this.menu_id = menu_id;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getSpicyLevel() {
        return spicyLevel;
    }

    public void setSpicyLevel(String spicyLevel) {
        this.spicyLevel = spicyLevel;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTableNo() {
        return tableNo;
    }

    public void setTableNo(String tableNo) {
        this.tableNo = tableNo;
    }
}
