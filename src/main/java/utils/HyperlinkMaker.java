package utils;

import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import main.Main;

public class HyperlinkMaker {
    public static void make(Label hyperlinkLabel, String labelTitle, String labelUrl) {
        hyperlinkLabel.setText(labelTitle);
        hyperlinkLabel.setCursor(Cursor.HAND);
        hyperlinkLabel.setOnMouseEntered(event -> {
            hyperlinkLabel.setUnderline(true);
        });
        hyperlinkLabel.setOnMouseExited(event -> {
            hyperlinkLabel.setUnderline(false);
        });
        hyperlinkLabel.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                Main.getInstance().getHostServices().showDocument(labelUrl);
            }
        });
    }
}
