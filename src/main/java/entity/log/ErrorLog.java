package entity.log;

import entity.database.MangaDatabase;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import java.sql.Connection;
import java.sql.Statement;

public class ErrorLog extends Log {
    public ErrorLog(String date, String message) {
        super(date, message);
    }

    public ErrorLog(String message) {
        super(message);
    }

    @Override
    public Label toLabel() {
        Label label = new Label(this.toString());
        label.setTextFill(Color.RED);
        return label;
    }

    public void save() {
        try {
            Connection conn = MangaDatabase.getConnection();
            String sql = "INSERT INTO logs VALUES ("
                    + "'" + date.replaceAll("'", "''") + "', "
                    + "'" + message.replaceAll("'", "''") + "', "
                    + "1"
                    + ");";
            Statement stm = conn.createStatement();
            stm.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
