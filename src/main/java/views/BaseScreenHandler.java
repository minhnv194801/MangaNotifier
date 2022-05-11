package views;

import controller.BaseController;
import javafx.scene.Scene;
import javafx.stage.Stage;
import views.home.HomeScreenHandler;

import java.io.IOException;

/**
 * This class represent all the class that handle the screen in EcoBikeRentalService software.
 */
public class BaseScreenHandler extends FXMLScreenHandler {

    protected final Stage stage;
    protected HomeScreenHandler homeScreenHandler;

    private Scene scene;
    private BaseController bController;

    /**
     * Initialize BaseScreenHandler.
     *
     * @param stage      stage to show the GUI
     * @param screenPath path to GUI's FXML file
     * @throws IOException if fail to construct the instance
     */
    public BaseScreenHandler(Stage stage, String screenPath) throws IOException {
        super(screenPath);
        this.stage = stage;
    }

    /**
     * Show the screen to the user.
     */
    public void show() {
        if (this.scene == null) {
            this.scene = new Scene(this.content);
        }
        this.stage.setScene(this.scene);
        this.stage.show();
    }

    public void close() {
        this.stage.close();
    }

    /**
     * Set the screen title of the screen.
     *
     * @param string title to be set
     */
    public void setScreenTitle(String string) {
        this.stage.setTitle(string);
    }

    /**
     * Get the controller of the screen.
     *
     * @return controller of the screen
     */
    public BaseController getBController() {
        return this.bController;
    }

    /**
     * Set the controller for the screen.
     *
     * @param bController controller to be set
     */
    public void setBController(BaseController bController) {
        this.bController = bController;
    }

    public void setHomeScreenHandler(HomeScreenHandler HomeScreenHandler) {
        this.homeScreenHandler = HomeScreenHandler;
    }
}

