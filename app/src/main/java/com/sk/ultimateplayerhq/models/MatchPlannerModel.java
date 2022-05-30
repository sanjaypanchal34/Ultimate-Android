package com.sk.ultimateplayerhq.models;

public class MatchPlannerModel {
    int id;
    String formation;
    String lineup_name;
    String lineup_image;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFormation() {
        return formation;
    }

    public void setFormation(String formation) {
        this.formation = formation;
    }

    public String getLineup_name() {
        return lineup_name;
    }

    public void setLineup_name(String lineup_name) {
        this.lineup_name = lineup_name;
    }

    public String getLineup_image() {
        return lineup_image;
    }

    public void setLineup_image(String lineup_image) {
        this.lineup_image = lineup_image;
    }
}
