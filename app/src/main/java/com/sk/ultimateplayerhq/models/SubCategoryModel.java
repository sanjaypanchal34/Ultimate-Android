package com.sk.ultimateplayerhq.models;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.io.Serializable;

public class SubCategoryModel implements Serializable {

    @SerializedName("category_slug")
    private String category_slug;

    @SerializedName("category_name")
    private String category_name;

    @SerializedName("term_id")
    private int term_id;

    public static SubCategoryModel fromJson(JSONObject object) {
        return new Gson().fromJson(object.toString(), SubCategoryModel.class);
    }


    public static SubCategoryModel fromString(String user) {
        return new Gson().fromJson(user, SubCategoryModel.class);
    }


    public SubCategoryModel(String category_name,String category_slug) {
        this.category_name = category_name;
        this.category_slug = category_slug;
    }

    public static String toString(SubCategoryModel model) {
        return new Gson().toJson(model);
    }


    public String getCategory_slug() {
        return category_slug;
    }

    public void setCategory_slug(String category_slug) {
        this.category_slug = category_slug;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public int getTerm_id() {
        return term_id;
    }

    public void setTerm_id(int term_id) {
        this.term_id = term_id;
    }
}