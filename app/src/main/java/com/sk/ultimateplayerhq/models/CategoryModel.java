package com.sk.ultimateplayerhq.models;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

public class CategoryModel implements Serializable{

    private  int position;
    private String title;
    private boolean isVisible = false;
    private List<SubCategoryModel> list;

    public CategoryModel(String title, List<SubCategoryModel> list, boolean isVisible,int position) {
        this.title = title;
        this.list = list;
        this.isVisible = isVisible;
        this.position = position;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<SubCategoryModel> getList() {
        return list;
    }

    public void setList(List<SubCategoryModel> list) {
        this.list = list;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }


    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
