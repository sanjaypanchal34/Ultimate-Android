package com.sk.ultimateplayerhq.models;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.Serializable;

public class ShopModel implements Serializable {
    int ID;
    String product_title;
    String product_price;
    String guid;
    String thumbnail_url;

    public static ShopModel fromJson(JSONObject object) {
        return new Gson().fromJson(object.toString(), ShopModel.class);
    }


    public static ShopModel fromString(String user) {
        return new Gson().fromJson(user, ShopModel.class);
    }

    public static String toString(ShopModel model) {
        return new Gson().toJson(model);
    }


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getProduct_title() {
        return product_title;
    }

    public void setProduct_title(String product_title) {
        this.product_title = product_title;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getThumbnail_url() {
        return thumbnail_url;
    }

    public void setThumbnail_url(String thumbnail_url) {
        this.thumbnail_url = thumbnail_url;
    }
}
