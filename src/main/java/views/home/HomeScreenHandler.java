package views.home;

import controller.HomeController;
import entity.log.ErrorLog;
import entity.log.Log;
import entity.manga.Manga;
import exceptions.ConnectionException;
import exceptions.EmptyMangaListException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utils.Configs;
import utils.CurrentTime;
import views.BaseScreenHandler;
import views.addmanga.NewMangaResultList;
import views.popup.PopupScreen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Semaphore;

public class HomeScreenHandler extends BaseScreenHandler {

    private static final int MAXIMUM_NUMBER_OF_PERMIT = 5;
    private static final Semaphore semaphore = new Semaphore(MAXIMUM_NUMBER_OF_PERMIT);

    @FXML
    private ScrollPane changeScrollPane;

    @FXML
    private VBox changeVBox;

    @FXML
    private ScrollPane mangaScrollPane;

    @FXML
    private VBox mangaVBox;

    @FXML
    private TextField searchTextField;

    @FXML
    private Button reloadBtn;

    @FXML
    private Button addBtn;

    @FXML
    private Label lastUpdateLabel;

    @FXML
    private ComboBox mangaComboBox;

    @FXML
    private ComboBox sourceComboBox;

    private final Stage newMangaListStage = new Stage();

    public HomeScreenHandler(Stage stage, String screenPath) throws IOException {
        super(stage, screenPath);
        setBController(HomeController.getInstance());
        stage.setResizable(false);
        mangaScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        sourceComboBox.getItems().addAll("nettruyen", "mangakakalot");
        sourceComboBox.setValue(Configs.mangaSource);
        sourceComboBox.setOnAction(e -> {
            Configs.mangaSource = (String) sourceComboBox.getValue();
            refresh();
        });

        mangaComboBox.getItems().addAll("All registered mangas", "Bookmarked mangas");
        mangaComboBox.setValue("All registered mangas");
        mangaComboBox.setOnAction(e -> {
            if (mangaComboBox.getValue().equals("Bookmarked mangas")) {
                renderMangaPane(getBController().getBookmarkedMangaList());
            } else {
                renderMangaPane(getBController().getMangaList());
            }
        });

        reloadBtn.setOnAction(event -> {
            newMangaListStage.close();
            PopupScreen loadingPopup = PopupScreen.loading("Please wait...!!!");
            close();
            loadingPopup.show();
            Thread t = new Thread() {
                public void run() {
                    getBController().update();

                    Platform.runLater(() -> {
                        loadingPopup.close();
                        show();
                        PopupScreen.success("Finish updating!");
                    });
                }
            };
            t.setDaemon(true);
            t.start();
        });

        searchTextField.setOnKeyTyped(event -> {
            renderMangaPane(getBController().getMangaList(searchTextField.getText()));
        });

        addBtn.setOnAction(event -> {
            TextInputDialog dialog = new TextInputDialog();

            dialog.setTitle("Entering keyword");
            dialog.setHeaderText("Enter new manga's keyword");
            dialog.setContentText("Keyword:");

            Optional<String> result = dialog.showAndWait();

            result.ifPresent(keyword -> {
                String kw = keyword.trim();
                if (!kw.isBlank()) {
                    try {
                        if (newMangaListStage.isShowing()) {
                            newMangaListStage.close();
                        }
                        PopupScreen loadingPopup = PopupScreen.loading("Please wait...!!!");
                        loadingPopup.show();
                        NewMangaResultList resultList = new NewMangaResultList(newMangaListStage, "/fxml/ResultMangaList.fxml");
                        resultList.setHomeScreenHandler(this);
                        Thread t = new Thread() {
                            public void run() {
                                try {
                                    resultList.setKeyword(kw);
                                } catch (ConnectionException e) {
                                    Log errorLog = new ErrorLog(CurrentTime.get(), e.getMessage());
                                    errorLog.save();
                                    Platform.runLater(() -> {
                                        PopupScreen.error(e.getMessage());
                                    });
                                    refresh();
                                } catch (EmptyMangaListException e) {
                                    Platform.runLater(() -> {
                                        PopupScreen.error("No new manga found with the keyword!!!");
                                    });
                                }

                                Platform.runLater(() -> {
                                    loadingPopup.close();
                                    resultList.show();
                                });
                            }
                        };
                        t.setDaemon(true);
                        t.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        });
    }

    public void removeManga(Manga manga) {
        getBController().removeManga(manga);
        PopupScreen.success("Successfully remove " + manga.getTitle() + " from your collection!");
        searchTextField.setText("");
        refresh();
    }

    private void renderMangaPane(List<Manga> mangaLst) {
        mangaVBox.getChildren().clear();

        List<Thread> threadList = new ArrayList<Thread>();
        try {
            for (Manga manga : mangaLst) {
                MangaViewHandler mangaView = new MangaViewHandler("/fxml/HomeScreen_manga.fxml", this);
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

    public void renderChangeLog() {
        changeVBox.getChildren().clear();
        List<Log> logList = Log.getAllLog();
        for (int i = logList.size() - 1; i >= 0; i--) {
            changeVBox.getChildren().add(logList.get(i).toLabel());
        }
        if (logList.size() != 0) {
            lastUpdateLabel.setText("(Last Update: " + logList.get(logList.size() - 1).getDate() + ")");
        } else {
            lastUpdateLabel.setText("(Last Update: )");
        }
    }

    public void refresh() {
        searchTextField.setText("");
        mangaComboBox.setValue("All registered mangas");
        renderChangeLog();
        renderMangaPane(getBController().getMangaList());
    }

    @Override
    public void show() {
        refresh();
        super.show();
    }

    @Override
    public HomeController getBController() {
        return (HomeController) super.getBController();
    }

}
