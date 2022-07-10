package ru.waind.classes.ChatConfig;

import org.bukkit.configuration.ConfigurationSection;

import ru.waind.RPCreator;

public class Chat {
    public final ChatType type;
    private final RPCreator pluginInstance = RPCreator.getInstance();
    private final ConfigurationSection configFile = pluginInstance.getConfig().getConfigurationSection("chats");

    public Chat(ChatType chatType) {
        type = chatType;
    }

    /**
     * Returns format string for current Chat.type chat
     * @return format string for this chat
     */
    public String getFormat() {
        return configFile.getString(String.format("%s.format", type.toString().toLowerCase()));
    }

    /**
     * Get config range for this chat (Default value is always 15)
     * @return range for this chat
     */
    public Integer getRange() {
        // todo
        // Default value
        return configFile.getInt(String.format("%s.range", type.toString().toLowerCase()));
    }
}
