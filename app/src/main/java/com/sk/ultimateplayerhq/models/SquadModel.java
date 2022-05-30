package com.sk.ultimateplayerhq.models;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

public class SquadModel implements Serializable{

    private  int id;
    private String name;
    private String email;
    private String profile_url;
    private String position;
    private String kit_color;
    private String jersey_number;
    private String appearances;
    private String goals;
    private String assists;
    private String mom;
    private String phone;
    private String address;

    public static SquadModel fromJson(JSONObject object) {
        return new Gson().fromJson(object.toString(), SquadModel.class);
    }


    public static SquadModel fromString(String user) {
        return new Gson().fromJson(user, SquadModel.class);
    }

    public static String toString(SquadModel model) {
        return new Gson().toJson(model);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfile_url() {
        return profile_url;
    }

    public void setProfile_url(String profile_url) {
        this.profile_url = profile_url;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getKit_color() {
        return kit_color;
    }

    public void setKit_color(String kit_color) {
        this.kit_color = kit_color;
    }

    public String getJersey_number() {
        return jersey_number;
    }

    public void setJersey_number(String jersey_number) {
        this.jersey_number = jersey_number;
    }

    public String getAppearances() {
        return appearances;
    }

    public void setAppearances(String appearances) {
        this.appearances = appearances;
    }

    public String getGoals() {
        return goals;
    }

    public void setGoals(String goals) {
        this.goals = goals;
    }

    public String getAssists() {
        return assists;
    }

    public void setAssists(String assists) {
        this.assists = assists;
    }

    public String getMom() {
        return mom;
    }

    public void setMom(String mom) {
        this.mom = mom;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
