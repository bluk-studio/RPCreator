package ru.waind.events;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import ru.waind.ConfigTypes;
import ru.waind.RPCreator;
import ru.waind.utils.ColorFormater;

import java.util.List;

public class ChatListener implements Listener {

//    @EventHandler
//    public void event(AsyncPlayerChatEvent e) {
//        char[] arr = e.getMessage().toCharArray();
//
//        RPCreator main = RPCreator.getInstance();
//        ConfigurationSection format = main.getConfig().getConfigurationSection("Format");
//        // UUID, HashMap<String, HashMap<ConfigType, String>>
//        String name = main.getPlayerFields().get(e.getPlayer().getUniqueId()).get("1").get(ConfigTypes.valueOf("string"));
//        String message;
//
//        // Диапазон рп-чата.
//        int range = main.getConfig().getInt("roleplay-chat-range");
//
//        List<Player> rangePlayers = e.getPlayer().getNearbyEntities(range, range, range).stream()
//                .filter(entity -> entity instanceof Player)
//                .map(entity -> (Player) entity).toList();
//
//        // Если имени персонажа нет
//        if(name == null) {
//            name = ColorFormater.setColor(main.getConfig().getString("empty-player-name"));
//        }
//
//        // Угадай мелодию, какой знак в чате - такое и рп-сообщение.
//
//        switch (arr[0]) {
//            case '*' ->
//                    message = placeholderSetter(format.getString("action"), e.getPlayer().getDisplayName(), name, e.getMessage(), "*");
//            case '#' ->
//                    message = placeholderSetter(format.getString("whisper"), e.getPlayer().getDisplayName(), name, e.getMessage(), "#");
//            case '!' ->
//                    message = placeholderSetter(format.getString("ooc"), e.getPlayer().getDisplayName(), name, e.getMessage(), "!");
//            default ->
//                    message = placeholderSetter(format.getString("message"), e.getPlayer().getDisplayName(), name, e.getMessage(), "");
//        }
//
//        if(arr[arr.length-1] == '!') {
//            message = placeholderSetter(format.getString("shout"), e.getPlayer().getDisplayName(), name, e.getMessage(), "!");
//        }
//
//        if(arr[0] != '!') {
//            String finalMessage = message;
//            rangePlayers.forEach(player -> {
//                player.sendMessage(finalMessage);
//            });
//            e.getPlayer().sendMessage(finalMessage);
//
//            e.setCancelled(true);
//        } else {
//            e.setFormat(message);
//        }
//    }
//
//    public static String placeholderSetter(String format, String displayname, String charactername, String message, String reg) {
//
//        String setDisplayName = format.replaceAll("%player", displayname);
//        String setCharacterName = setDisplayName.replaceAll("%charactername", charactername);
//        String setMessage = setCharacterName.replaceAll("%message", message);
//
//        String formattedStr = setMessage.replaceFirst(reg, "");
//
//        return ColorFormater.setColor(formattedStr);
//    }
}
