package views;

import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.Objects;

/**
 * This class represent all class that handle FXML file in EcoBikeRentalService software.
 */
public class FXMLScreenHandler {

    protected FXMLLoader loader;
    protected AnchorPane content;

    /**
     * Initialize FXMLScreenHandler.
     *
     * @param screenPath path to GUI's FXML file
     * @throws IOException if fail to construct the instance
     */
    public FXMLScreenHandler(String screenPath) throws IOException {
        this.loader = new FXMLLoader(getClass().getResource(screenPath));
        // Set this class as the controller
        this.loader.setController(this);
        this.content = loader.load();
    }

    /**
     * Get the content in the screen.
     *
     * @return the content in the screen
     */
    public AnchorPane getContent() {
        return this.content;
    }

    /**
     * Set the image to an {@link javafx.scene.image.ImageView ImageView}.
     *
     * @param imv  the image view that need to be set
     * @param path the path to the image
     */
    public void setImage(ImageView imv, String path) {
        Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));
        imv.setImage(img);
    }
}
