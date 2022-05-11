package entity.log;

import entity.database.MangaDatabase;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import java.sql.Connection;
import java.sql.Statement;

public class UpdateLog extends Log {
    public UpdateLog(String date, String message) {
        super(date, message);
    }

    public UpdateLog(String message) {
        super(message);
    }

    public Label toLabel() {
        Label label = new Label(this.toString());
        label.setTextFill(Color.BLACK);
        label.setUnderline(true);
        return label;
    }

    public void save() {
        try {
            Connection conn = MangaDatabase.getConnection();
            String sql = "INSERT INTO logs VALUES ("
                    + "'" + date.replaceAll("'", "''") + "', "
                    + "'" + message.replaceAll("'", "''") + "', "
                    + "0"
                    + ");";
            Statement stm = conn.createStatement();
            stm.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
