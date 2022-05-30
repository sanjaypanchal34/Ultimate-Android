package com.sk.ultimateplayerhq.utils;

import android.graphics.Color;

public class ColorUtils {
    public static int getColor(String kit_color) {
        switch (kit_color.toLowerCase()) {
            case "red":
                return Color.parseColor("#ff0000");
            case "green":
                return Color.parseColor("#008000");
            case "blue":
                return Color.parseColor("#0000ff");
            case "pink":
                return Color.parseColor("#ffc0cb");
            case "yellow":
                return Color.parseColor("#ffff00");
            case "grey":
                return Color.parseColor("#808080");
            case "white":
                return Color.parseColor("#ffffff");
            case "black":
                return Color.parseColor("#000000");
            default:
                return Color.parseColor("#FFC0CB");
        }
    }
}
