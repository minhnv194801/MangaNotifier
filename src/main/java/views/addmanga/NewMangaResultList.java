package views.addmanga;

import controller.AddMangaController;
import entity.manga.Manga;
import exceptions.ConnectionException;
import exceptions.EmptyMangaListException;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import views.BaseScreenHandler;
import views.popup.PopupScreen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class NewMangaResultList extends BaseScreenHandler {

    private static final int MAXIMUM_NUMBER_OF_PERMIT = 5;
    private static final Semaphore semaphore = new Semaphore(MAXIMUM_NUMBER_OF_PERMIT);

    @FXML
    private ScrollPane mangaScrollPane;

    @FXML
    private VBox mangaVBox;

    public NewMangaResultList(Stage stage, String screenPath) throws IOException {
        super(stage, screenPath);
        setBController(AddMangaController.getInstance());
        stage.setResizable(false);
        mangaScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    }

    public void setKeyword(String keyword) throws ConnectionException, EmptyMangaListException {
        getBController().setNewMangaLst(keyword);
        renderMangaPane();
        if (getBController().isEmpty()) {
            throw new EmptyMangaListException("No new manga with that keyword found!!!");
        }
    }

    private void renderMangaPane() {
        mangaVBox.getChildren().clear();

        List<Manga> mangaLst = getBController().getNewMangaLst();

        List<Thread> threadList = new ArrayList<Thread>();
        try {
            for (Manga manga : mangaLst) {
                NewMangaViewHandler mangaView = new NewMangaViewHandler("/fxml/ResultMangaList_manga.fxml", this);
                Thread t = new Thread(() -> {
                    boolean permit = true;

                    try {
                        semaphore.acquire();
                    } catch (InterruptedException e) {
                        permit = false;
                        e.printStackTrace();
                    }

                    if (permit) {
                        mangaView.setManga(manga);
                    }

                    semaphore.release();
                });
                threadList.add(t);
                t.setDaemon(true);
                t.start();
                mangaVBox.getChildren().add(mangaView.getContent());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Thread t: threadList) {
            if (t.isAlive()) {
                try {
                    t.join(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void addManga(Manga manga) {
        getBController().addManga(manga);
        if (getBController().isEmpty()) {
            close();
        }
        renderMangaPane();
        String str = "Successfully add " + manga.getTitle() + " to your collection!";
        PopupScreen.success(str);
        homeScreenHandler.refresh();
    }

    @Override
    public AddMangaController getBController() {
        return (AddMangaController) super.getBController();
    }

    public void show() {
        if (!getBController().getNewMangaLst().isEmpty()) {
            super.show();
        }
    }

}
