package com.kira.spark.util;

import com.kira.spark.bean.Item;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class SparkCal {

    private static final DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static String getTotalWithoutTaxPrice(List<Item> list)
    {
        double price = 0;
        for(int i=0;i<list.size(); i++)
        {
             price = list.get(i).getPrice() + price;
        }
        return Double.toString(price);
    }

    public static String getQty(List<Item> list)
    {
        double qty = 0;
        for(int i=0;i<list.size(); i++)
        {
            qty = list.get(i).getQty() + qty;
        }
        return String.format("%.0f", qty);
    }
    public static String generateUniqueID()
    {
        Date date = new Date();
        String uniqueID = UUID.randomUUID().toString();
        return uniqueID + sdf.format(date);
    }
}
