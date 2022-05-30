package com.sk.ultimateplayerhq.models;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EventModel {
    @SerializedName("id")
    int id;
    @SerializedName("title")
    String title;
    @SerializedName("location")
    String location;
    @SerializedName("start")
    String start;
    @SerializedName("end")
    String end;
    @SerializedName("logo_image")
    String logo_image;
    @SerializedName("description")
    String description;
    @SerializedName("player_id")
    String player_id;
    @SerializedName("won_lost")
    String won_lost;
    @SerializedName("is_training")
    int is_training;
    @SerializedName("playerData")
    List<Player> playerData;


    public static EventModel fromJson(JSONObject object) {
        return new Gson().fromJson(object.toString(), EventModel.class);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getLogo_image() {
        return logo_image;
    }

    public void setLogo_image(String logo_image) {
        this.logo_image = logo_image;
    }

    public String getPlayer_id() {
        return player_id;
    }

    public void setPlayer_id(String player_id) {
        this.player_id = player_id;
    }

    public int getIs_training() {
        return is_training;
    }

    public void setIs_training(int is_training) {
        this.is_training = is_training;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getWon_lost() {
        return won_lost == null ? "" : won_lost;
    }

    public void setWon_lost(String won_lost) {
        this.won_lost = won_lost;
    }


    public List<Player> getPlayerData(String status) {
        List<Player> temp = new ArrayList<>();
       if(playerData!=null){
           for (Player player : playerData) {
               if (player.status.equalsIgnoreCase(status)) {
                   temp.add(player);
               }
           }
       }
        return temp;
    }

    public void setPlayerData(List<Player> playerData) {
        this.playerData = playerData;
    }


    public List<Player> getPlayerData() {
        return playerData;
    }

    public static class Player implements Serializable {
        @SerializedName("status")
        String status;
        @SerializedName("name")
        String name;
        @SerializedName("image")
        String image;
        @SerializedName("kit_color")
        String kit_color;
        @SerializedName("jersey_number")
        String jersey_number;
        @SerializedName("id")
        int id;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
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

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
