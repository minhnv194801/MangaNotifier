package controller;

import entity.log.UpdateLog;
import entity.manga.Manga;
import entity.scrapper.MangaScrapper;
import entity.scrapper.factory.MangaScrapperFactory;
import exceptions.ConnectionException;
import utils.Configs;

import java.util.List;

public class AddMangaController extends BaseController {

    private static AddMangaController instance;

    private List<Manga> newMangaLst;

    private AddMangaController() {
    }

    public static AddMangaController getInstance() {
        if (instance == null) {
            instance = new AddMangaController();
        }

        return instance;
    }

    public void addManga(Manga manga) {
        manga.save();
        newMangaLst.remove(manga);
        new UpdateLog("Successfully add " + manga.getTitle() + " to your collection!").save();
    }

    public List<Manga> getNewMangaLst() {
        return this.newMangaLst;
    }

    public void setNewMangaLst(String keyword) throws ConnectionException {
        MangaScrapperFactory scrapperFactory = new MangaScrapperFactory();
        MangaScrapper mangaScrapper = scrapperFactory.getMangaScrapper(Configs.mangaSource);
        newMangaLst = mangaScrapper.fetchManga(keyword);
        newMangaLst.removeIf(manga -> (manga.isExist()));
    }

    public boolean isEmpty() {
        if (newMangaLst == null) {
            return true;
        } else {
            return newMangaLst.isEmpty();
        }
    }
}
