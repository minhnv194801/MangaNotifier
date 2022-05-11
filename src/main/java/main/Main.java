package main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import views.home.HomeScreenHandler;
import views.popup.PopupScreen;

import java.util.Objects;

public class Main extends Application {

    private static Main app;

    public static void main(String[] args) {
        launch(args);
    }

    public static Main getInstance() {
        return app;
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            app = this;
            primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/logo.png"))));
            PopupScreen loadingPopup = PopupScreen.loading("Please wait...!!!");
            Objects.requireNonNull(loadingPopup).show();
            Thread t = new Thread() {
                public void run() {
                    Platform.runLater(() -> {
                        try {
                            HomeScreenHandler screen = new HomeScreenHandler(primaryStage, "/fxml/HomeScreen.fxml");
                            screen.setScreenTitle("SirMangaNox");
                            screen.show();
                            loadingPopup.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            };
            t.setDaemon(true);
            t.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
