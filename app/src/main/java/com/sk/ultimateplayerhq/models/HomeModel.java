package com.sk.ultimateplayerhq.models;

import java.io.Serializable;
import java.util.List;

public class HomeModel implements Serializable {

    String title;
    List<SessionModel> list;


    public HomeModel(String title, List<SessionModel> list) {
        this.title = title;
        this.list = list;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<SessionModel> getList() {
        return list;
    }

    public void setList(List<SessionModel> list) {
        this.list = list;
    }

}
