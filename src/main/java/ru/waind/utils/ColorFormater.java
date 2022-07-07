package ru.waind.utils;

import org.bukkit.ChatColor;

public class ColorFormater {

    public static String setColor(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }
}
