package controller;

import entity.log.ErrorLog;
import entity.log.Log;
import entity.log.UpdateLog;
import entity.manga.Manga;
import entity.scrapper.MangaScrapper;
import entity.scrapper.factory.MangaScrapperFactory;
import exceptions.ConnectionException;
import utils.Configs;
import utils.CurrentTime;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class HomeController extends BaseController {

    private static final int MAXIMUM_NUMBER_OF_PERMIT = 5;
    private static final Semaphore semaphore = new Semaphore(MAXIMUM_NUMBER_OF_PERMIT);
    private static HomeController instance;
    private boolean newUpdateExist;

    private HomeController() {
    }

    public static HomeController getInstance() {
        if (instance == null) {
            instance = new HomeController();
        }

        return instance;
    }

    public void update() {
        MangaScrapperFactory scrapperFactory = new MangaScrapperFactory();
        MangaScrapper mangaScrapper = scrapperFactory.getMangaScrapper(Configs.mangaSource);

        newUpdateExist = false;
        List<Thread> threads = new ArrayList<Thread>();
        List<Manga> mangaLst = Manga.getAllManga();
        for (Manga manga : mangaLst) {
            try {
                Thread t = new Thread() {
                    public void run() {
                        boolean permit = true;
                        try {
                            semaphore.acquire();
                        } catch (Exception e) {
                            e.printStackTrace();
                            permit = false;
                        }
                        if (permit) {
                            try {
                                Manga freshManga = mangaScrapper.fetchManga(manga);
                                if (freshManga != null && !freshManga.equals(manga)) {
                                    freshManga.update();
                                    Log log = new UpdateLog(CurrentTime.get(), "CHECK OUT THE NEW RELEASES OF " + manga.getTitle());
                                    log.save();
                                    newUpdateExist = true;
                                }
                            } catch (ConnectionException e) {
                                e.printStackTrace();
                                Log log = new ErrorLog(CurrentTime.get(), e.getMessage());
                                log.save();
                            } finally {
                                semaphore.release();
                            }
                        }
                    }
                };
                threads.add(t);
                t.setDaemon(true);
                t.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (!newUpdateExist) {
            Log log = new UpdateLog(CurrentTime.get(), "No new release found");
            log.save();
        }

    }

    public List<Manga> getMangaList(String keyword) {
        return Manga.getMangaByKeyword(keyword);
    }

    public void removeManga(Manga manga) {
        manga.remove();
        Log removeLog = new UpdateLog("Successfully remove " + manga.getTitle() + " from your collection!");
        removeLog.save();
    }

    public List<Manga> getMangaList() {
        return Manga.getAllManga();
    }

    public List<Manga> getBookmarkedMangaList() {
        return Manga.getAllBookmarkedManga();
    }

}
