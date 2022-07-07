package ru.waind;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import ru.waind.database.SqliteConnector;
import ru.waind.events.ChatListener;
import ru.waind.events.Join;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.*;

public final class RPCreator extends JavaPlugin {

    private static RPCreator instance;

    private static FileConfiguration cards;
    private static File cardFile;

    private static FileConfiguration lang;
    private static File langFile;

    private static HashMap<UUID, HashMap<String,  HashMap<ConfigTypes, String>>> playerFields = new HashMap<>();

    public static HashMap<UUID, HashMap<String, HashMap<ConfigTypes, String>>> getPlayerFields() {return playerFields;}

    public static RPCreator getInstance() { return instance; }

    public void reloadCardFile() throws IOException {
        cardFile = new File(getDataFolder(), "cards.yml");

        if(!cardFile.exists()) {
            cardFile.createNewFile();
        }

        cards = YamlConfiguration.loadConfiguration(cardFile);
        InputStream defaultStream = getResource("cardFile");
        if(defaultStream != null) {
            YamlConfiguration defaultCard = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            cards.setDefaults(defaultCard);
        }
    }

    public FileConfiguration getCards() { return cards; }

    public FileConfiguration getLang() { return lang; }
    public void reloadLangFile() throws IOException {
        langFile = new File(getDataFolder(), "lang.yml");

        if(!langFile.exists()) {
            langFile.createNewFile();
        }

        lang = YamlConfiguration.loadConfiguration(langFile);
        InputStream defaultStream = getResource("lang.yml");
        if(defaultStream != null) {
            YamlConfiguration defaultLang = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            lang.setDefaults(defaultLang);
        }
    }

    public void saveLang() {
        if(lang != null || langFile != null) {
            try {
                getLang().save(langFile);
            } catch (IOException e) {
                e.printStackTrace();
                getServer().getPluginManager().disablePlugin(instance);
            }
        }
    }

    public void registerEvents() {
        new ArrayList<>(Arrays.asList(
                new Join()
        )).forEach(e -> {
            getServer().getPluginManager().registerEvents(e, this);
        });
    }

    public static void changeFields(Player p) {
        ConfigurationSection card = instance.getCards().getConfigurationSection("Players-Cards").getConfigurationSection("fields");

        card.getKeys(false).stream().filter(card::isConfigurationSection).forEach(c -> {

            int field = Integer.parseInt(c);
            ConfigTypes type = (ConfigTypes) card.getConfigurationSection(c).get("type");
            int hash = (c + type.toString()).hashCode();
            String placeholder = card.getConfigurationSection(c).getString("placeholder");

            String sql;

            try {

                if(SqliteConnector.read("SELECT * FROM player_data WHERE uuid='" + p.getUniqueId() + "'") == null) {
                    sql = "INSERT INTO player_data (uuid, type, name, field_id, hash) VALUES ('?', '?', '?', '?', '?')";
                } else {
                    sql = "UPDATE player_data SET uuid='?', type='?', name='?', field_id='?', hash='?'";
                }

                SqliteConnector.connect();
                PreparedStatement pStmt = SqliteConnector.getConn().prepareStatement(sql);
                pStmt.setString(1, p.getUniqueId().toString());
                pStmt.setString(2, type.name());
                pStmt.setString(3, placeholder);
                pStmt.setInt(4, field);
                pStmt.setString(5, Integer.toString(hash));
                pStmt.executeQuery();
                SqliteConnector.close();

            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }

        });
    }

    // Метод для заполнения playerFields на запуске сервера и синхронизацией в n секунд. Чисто берем данные из бд в оперативу.
    public static void syncWithSqlite() throws ClassNotFoundException, SQLException {

        String sql;

        sql = "SELECT * FROM player_data";

        SqliteConnector.connect();
        Connection conn = SqliteConnector.getConn();
        Statement stmt = conn.createStatement();
        ResultSet selectSet = stmt.executeQuery(sql);

        HashMap<ConfigTypes, String> fieldsValues = new HashMap<>();
        HashMap<String, HashMap<ConfigTypes, String>> fieldIds = new HashMap<>();

        // Заполняем матрешку, то есть по UUID игрока можно будет найти айди поля, а по айди поля можно будет найти значения

        while(selectSet.next()) {

            if(!playerFields.containsKey(UUID.fromString(selectSet.getString("uuid")))) {

                fieldsValues.put(ConfigTypes.valueOf(selectSet.getString("type")), selectSet.getString("value"));
                fieldIds.put(selectSet.getString("field_id"), fieldsValues);

                playerFields.put(UUID.fromString(selectSet.getString("uuid")), fieldIds);
            } else {
                sql = "UPDATE player_data SET field_id='?', type='?', value='?', hash='?'";

                PreparedStatement pStmt = conn.prepareStatement(sql);

                playerFields.get(UUID.fromString(selectSet.getString("uuid"))).forEach((s, h) -> {
                    h.forEach((t, v) -> {
                        try {
                            pStmt.setString(1, s);
                            pStmt.setString(2, String.valueOf(t));
                            pStmt.setString(3, v);
                            pStmt.setString(4, Integer.toString((s+t).hashCode()));
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    });
                });

            }
        }

        SqliteConnector.close();
    }


    @Override
    public void onEnable() {

        instance = this;

        registerEvents();
        saveDefaultConfig();

        // Релоадим cards.yml и lang.yml
        try {
            reloadLangFile();
            reloadCardFile();
        } catch (IOException e) {
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(instance);
        }

        // Подключаемся к бд
        try {
            SqliteConnector.writeDefault();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(instance);
        }

        int sync = getConfig().getInt("sync-with-sqlite");
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    syncWithSqlite();
                } catch (ClassNotFoundException | SQLException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskTimer(this, 0, 20L*sync);


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
