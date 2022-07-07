package ru.waind.events;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import ru.waind.ConfigTypes;
import ru.waind.RPCreator;

import java.util.HashMap;

public class Join implements Listener {

    @EventHandler
    public void event(PlayerJoinEvent e) {
        RPCreator main = RPCreator.getInstance();

        if(!main.getPlayerFields().containsKey(e.getPlayer().getUniqueId())) {
            ConfigurationSection fields = main.getCards().getConfigurationSection("Players-Cards").getConfigurationSection("fields");
            HashMap<String, HashMap<ConfigTypes, String>> h = new HashMap<>();
            HashMap<ConfigTypes, String> pH = new HashMap<>();

            fields.getKeys(false).stream().filter(fields::isConfigurationSection).forEach(f -> {

                pH.put(ConfigTypes.valueOf(fields.getConfigurationSection(f).getString("type")),
                        main.getConfig().getString("empty-player-name"));
                h.put(f, pH);
            });

            main.getPlayerFields().put(e.getPlayer().getUniqueId(), h);
        }
    }
}
