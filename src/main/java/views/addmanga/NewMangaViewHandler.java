package views.addmanga;

import entity.manga.Manga;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import utils.HyperlinkMaker;
import views.FXMLScreenHandler;

import java.io.IOException;

public class NewMangaViewHandler extends FXMLScreenHandler {

    @FXML
    private ImageView image;

    @FXML
    private Label titleLabel;

    @FXML
    private Button addBtn;

    private Manga manga;
    private final NewMangaResultList mangaListView;

    public NewMangaViewHandler(String screenPath, NewMangaResultList mangaListView) throws IOException {
        super(screenPath);
        this.mangaListView = mangaListView;

        addBtn.setOnAction(event -> {
            mangaListView.addManga(manga);
        });
    }

    public void setManga(Manga manga) {
        this.manga = manga;
        renderManga();
    }

    private void renderManga() {
        try {
            HyperlinkMaker.make(titleLabel, manga.getTitle(), manga.getUrl());
            image.setImage(manga.getMangaImage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
