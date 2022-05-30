package com.sk.ultimateplayerhq.utils;

import java.util.Locale;

import in.appsaint.communication.Api;

public class UrlUtils {

    public static String get(String method){
        return  String.format(Locale.ENGLISH,"%s?wp_token=%s&id=%d&redirect_link=%s", Api.HOST,SessionManager.getUser().getWp_generate_token(),SessionManager.getUser().getId(),method);
    }
}
