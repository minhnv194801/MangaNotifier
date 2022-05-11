package entity.database;

import utils.Configs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class MangaDatabase {
    private static Connection connect;
    private static String currentSource;

    public static Connection getConnection() {
        if (currentSource == null) {
            currentSource = Configs.mangaSource;
        }

        if (currentSource != Configs.mangaSource) {
            try {
                connect.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            connect = null;
            currentSource = Configs.mangaSource;
        }

        if (connect != null) {
            return connect;
        }

        try {
            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:" + currentSource +"_database.db";
            connect = DriverManager.getConnection(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connect;
    }
}
