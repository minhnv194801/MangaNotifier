package views.home;

import entity.manga.Manga;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import utils.HyperlinkMaker;
import views.FXMLScreenHandler;
import views.popup.PopupScreen;

import java.io.IOException;
import java.util.Optional;

public class MangaViewHandler extends FXMLScreenHandler {

    @FXML
    private HBox hboxManga;

    @FXML
    private ImageView image;

    @FXML
    private Label title;

    @FXML
    private Label chapter1;

    @FXML
    private Label chapter2;

    @FXML
    private Button btnDelete;

    private Manga manga;
    private final HomeScreenHandler homeScreen;

    public MangaViewHandler(String screenPath, HomeScreenHandler homeScreen) throws IOException {
        super(screenPath);
        this.homeScreen = homeScreen;
        btnDelete.setOnAction(event -> {
            Alert alert =
                    new Alert(Alert.AlertType.WARNING,
                            "Are you sure you want to delete " + manga.getTitle(),
                            ButtonType.YES, ButtonType.NO);
            alert.setTitle("Remove manga confirmation");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.YES) {
                this.homeScreen.removeManga(manga);
            }
        });
    }

    public void setManga(Manga manga) {
        this.manga = manga;
        renderManga();
    }

    private void renderManga() {
        try {
            HyperlinkMaker.make(title, manga.getTitle(), manga.getUrl());
            ContextMenu contextMenu = new ContextMenu();
            if (!manga.isBookmarked()) {
                MenuItem menuItem = new MenuItem("Bookmark this");
                menuItem.setOnAction(event -> {
                    manga.bookmark();
                    homeScreen.refresh();
                    PopupScreen.success("Successfully bookmark " + manga.getTitle());
                });
                contextMenu.getItems().add(menuItem);
            } else {
                MenuItem menuItem = new MenuItem("Remove bookmark");
                menuItem.setOnAction(event -> {
                    manga.unBookmark();
                    homeScreen.refresh();
                    PopupScreen.success("Successfully remove bookmark from " + manga.getTitle());
                });
                contextMenu.getItems().add(menuItem);
            }
            title.setContextMenu(contextMenu);
            int i = manga.getLatestTitles().size() - 1;
            HyperlinkMaker.make(chapter1, manga.getLatestTitles().get(i), manga.getLatestUrls().get(i));
            i--;
            if (i >= 0) {
                HyperlinkMaker.make(chapter2, manga.getLatestTitles().get(i), manga.getLatestUrls().get(i));
            }
            image.setImage(manga.getMangaImage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
