package ru.waind.database;

import org.bukkit.Bukkit;
import ru.waind.RPCreator;

import java.io.File;
import java.sql.*;

public class SqliteConnector {

    private static Connection conn;
    private static ResultSet rs;
    private static Statement stmt;
    private static RPCreator main = RPCreator.getInstance();
    private static String sep = File.pathSeparator;

    public static Connection getConn() { return conn; }

    public static void connect() throws ClassNotFoundException {
        conn = null;
        Class.forName("org.sqlite.JDBC");
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:" + Bukkit.getServer().getWorldContainer().getPath() + sep + "plugins" + sep + "RPCreator" + sep + "database.db");
        } catch(SQLException ex) {
            ex.printStackTrace();
            Bukkit.getServer().getPluginManager().disablePlugin(main);
        }
    }

    public static void writeDefault() throws SQLException, ClassNotFoundException {
        connect();
        stmt = conn.createStatement();
        stmt.execute("CREATE TABLE if not exists player_data(" +
                "uuid VARCHAR(255) PRIMARY KEY NOT NULL," +
                "type ENUM('string', 'option', 'integer', 'string_list') NOT NULL, " +
                "name VARCHAR(255) NOT NULL, " +
                "value VARCHAR(255) DEFAULT NULL, " +
                "field_id VARCHAR(255) NOT NULL," +
                "hash VARCHAR(255) NOT NULL);");
        stmt.close();
        close();
    }

    public static ResultSet read(String sql) throws SQLException, ClassNotFoundException {

        connect();

        stmt = conn.createStatement();
        rs = stmt.executeQuery(sql);

        ResultSet lRs = rs;

        close();

        return(lRs);
    }


    public static void close() throws SQLException {
        if(rs != null) { rs.close(); }
        if(stmt != null) { stmt.close(); }
        if(conn != null) { conn.close(); }

        rs = null;
        stmt = null;
        conn = null;
    }
}
