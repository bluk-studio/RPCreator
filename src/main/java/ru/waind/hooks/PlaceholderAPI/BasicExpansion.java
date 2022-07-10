package ru.waind.hooks.PlaceholderAPI;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;

import ru.waind.RPCreator;

import java.util.List;

public class BasicExpansion extends PlaceholderExpansion {
    private final RPCreator pluginInstance = RPCreator.getInstance();

    @Override
    public String getAuthor() {
        return "bluk-studio";
    }

    @Override
    public String getIdentifier() {
        return "RPCreator";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        // Trying to determine needed placeholder
        List<String> configFields = pluginInstance.getCards().getConfigurationSection("Player-Cards.fields").getKeys(false).stream().toList();

        for (String field : configFields) {
            if (params.equalsIgnoreCase(field)) {
                // todo
                // Get this player's value and return it
                return "empty";
            }
        }

        // Unknown placeholder
        return null;
    }
}