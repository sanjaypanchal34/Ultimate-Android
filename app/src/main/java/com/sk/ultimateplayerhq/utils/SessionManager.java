package com.sk.ultimateplayerhq.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.sk.ultimateplayerhq.models.UserModel;

public class SessionManager {
    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;

    public static void init(@NonNull Context context) {
        preferences = context.getSharedPreferences("ojdfhuidgf", 0);
        editor = preferences.edit();
        editor.apply();
    }

    public static void setUser(UserModel user) {
        editor.putString("USER", UserModel.toString(user));
        editor.apply();
    }

    public static void setToken(String token) {
        editor.putString("TOKEN", token);
        editor.apply();
    }


    public static void setNewUserID(int id) {
        editor.putInt("NEW_ID", id);
        editor.apply();
    }

    public static void setRole(String role) {
        editor.putString("PLAYER_ROLE", role);
        editor.apply();
    }


    public static void setLogged(boolean is) {
        editor.putBoolean("IS_LOGGED", is);
        editor.apply();
    }



    public static void setIsPlayerLogin(boolean is) {
        editor.putBoolean("IS_PLAYER_LOGIN", is);
        editor.apply();
    }

    public static  boolean isLogged(){
        return preferences.getBoolean("IS_LOGGED",false);
    }
    public static  boolean isPlayerLogin(){
        return preferences.getBoolean("IS_PLAYER_LOGIN",false);
    }
    public static  String getToken(){
         return preferences.getString("TOKEN","");
    }
    public static  int getNewUserID(){
         return preferences.getInt("NEW_ID",-1);
    }
    public static  String getPlayerRole(){
         return preferences.getString("PLAYER_ROLE","coach");
    }


    public static UserModel getUser() {
        return UserModel.fromString(preferences.getString("USER", ""));
    }

    public static void setHomeBanner(String app_banner_url) {
        editor.putString("HOME_BANNER", app_banner_url);
        editor.apply();
    }


    public static String getHomeBanner(){
        return preferences.getString("HOME_BANNER","");
    }
}
