package ru.waind.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import me.clip.placeholderapi.PlaceholderAPI;

import java.util.Arrays;
import java.util.List;

import ru.waind.RPCreator;
import ru.waind.classes.ChatConfig.*;

public class ChatListener implements Listener {
    private final RPCreator instance = RPCreator.getInstance();

    @EventHandler
    public void event(AsyncPlayerChatEvent e) {
        // Cancelling event
        e.setCancelled(true);

        // Preparing variables
        Chat chatConfig = new Chat(determineChatType(e.getMessage()));
        Integer range = chatConfig.getRange();
        List<Player> players;

        if (chatConfig.type == ChatType.OOC) {
            players = (List<Player>) instance.getServer().getOnlinePlayers().stream().toList();
        } else {
            players = e.getPlayer().getNearbyEntities(range, range, range).stream()
                    .filter(entity -> entity instanceof Player)
                    .map(entity -> (Player) entity).toList();
        }

        // Sending message to players
        players.forEach(player -> {
            player.sendMessage(
                    PlaceholderAPI.setPlaceholders(
                            e.getPlayer(),
                            setCustomPlaceholders(e.getMessage(), chatConfig.getFormat())
                    )
            );
        });
    }

    private String setCustomPlaceholders(String message, String format) {
        String formattedString;
        formattedString = format.replaceAll("%message%", message);

        return formattedString;
    }

    private static ChatType determineChatType(String message) {
        char[] messageChars = message.toCharArray();

        if (messageChars[messageChars.length-1] == '!' && messageChars[0] != '!') {
            return ChatType.SHOUT ;
        }

        return switch (messageChars[0]) {
            case '*' -> ChatType.ACTION;
            case '#' -> ChatType.WHISPER;
            case '!' -> ChatType.OOC;
            default -> ChatType.DEFAULT;
        };
    }
}
