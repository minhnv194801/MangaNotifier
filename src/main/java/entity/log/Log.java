package entity.log;

import entity.database.MangaDatabase;
import javafx.scene.control.Label;
import utils.CurrentTime;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public abstract class Log {

    protected static final int MAXIMUM_NUMBER_OF_LOG = 100;
    protected final String date;
    protected final String message;

    public Log(String date, String message) {
        this.date = date;
        this.message = message;
    }

    public Log(String message) {
        this(CurrentTime.get(), message);
    }

    public static List<Log> getAllLog() {
        List<Log> logList = new ArrayList<>();
        try {
            Connection conn = MangaDatabase.getConnection();
            String sql = "SELECT * FROM logs;";
            Statement stm = conn.createStatement();
            ResultSet res = stm.executeQuery(sql);
            while (res.next()) {
                if (res.getBoolean("is_error")) {
                    logList.add(new ErrorLog(res.getString(1), res.getString(2)));
                } else {
                    logList.add(new UpdateLog(res.getString(1), res.getString(2)));
                }
            }
            while (logList.size() > MAXIMUM_NUMBER_OF_LOG) {
                logList.get(0).remove();
                logList.remove(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return logList;
    }

    public abstract void save();

    public abstract Label toLabel();

    public void remove() {
        try {
            Connection conn = MangaDatabase.getConnection();
            String sql = "DELETE FROM logs WHERE "
                    + "date LIKE " + "'" + date + "' "
                    + "AND message LIKE " + "'" + message.replaceAll("'", "''") + "'"
                    + ";";
            Statement stm = conn.createStatement();
            stm.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return date + ": " + message;
    }

    public String getDate() {
        return this.date;
    }

}
