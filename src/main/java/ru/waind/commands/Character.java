package ru.waind.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import ru.waind.RPCreator;
import ru.waind.utils.ColorFormater;

public class Character implements CommandExecutor {

    RPCreator main = RPCreator.getInstance();
    String header = main.getLang().getString("header");
    ConfigurationSection help = main.getLang().getConfigurationSection("help-command");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player) {
            if(args.length == 0) {

            }
        }


        return true;
    }

    public void helpArg(Player p) {
        p.sendMessage(ColorFormater.setColor(help.getString("header")));

    }
}
