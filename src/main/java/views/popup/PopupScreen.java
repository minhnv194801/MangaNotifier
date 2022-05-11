package views.popup;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import views.BaseScreenHandler;

import java.io.IOException;


public class PopupScreen extends BaseScreenHandler {
    @FXML
    ImageView image;

    @FXML
    Label message;

    @FXML
    Button primaryButton;


    public PopupScreen(Stage stage) throws IOException {
        super(stage, "/fxml/popup.fxml");
        primaryButton.setOnMouseClicked(mouseEvent -> close(0));
    }

    private static PopupScreen popup(String message, String imagepath, Boolean undecorated) throws IOException {
        PopupScreen popup = new PopupScreen(new Stage());
        if (undecorated) popup.stage.initStyle(StageStyle.UNDECORATED);
        popup.message.setText(message);
        popup.setImage(imagepath);
        return popup;
    }

    public static void success(String message) {
        try {
            popup(message, "/images/check.png", true).show(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void error(String message) {
        try {
            popup(message, "/images/cancel.png", false).show(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static PopupScreen loading(String message) {
        try {
            PopupScreen loadingPopup = popup(message, "/images/loading.gif", true);
            loadingPopup.primaryButton.setVisible(false);
            return loadingPopup;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void setImage(String path) {
        super.setImage(image, path);
    }

    public void show(Boolean autoclose) {
        super.show();
        if (autoclose) close(1);
    }

    public void show(double time) {
        super.show();
        close(time);
    }

    public void close(double time) {
        PauseTransition delay = new PauseTransition(Duration.seconds(time));
        delay.setOnFinished(event -> stage.close());
        delay.play();
    }
}
