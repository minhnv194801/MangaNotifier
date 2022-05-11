package entity.scrapper.factory;

import entity.scrapper.MangaScrapper;
import entity.scrapper.MangakakalotMangaScrapper;
import entity.scrapper.NettruyenMangaScrapper;

public class MangaScrapperFactory {
    public MangaScrapper getMangaScrapper(String scrapperType) {
        if (scrapperType == null) {
            return null;
        }

        if (scrapperType.equalsIgnoreCase("Mangakakalot")) {
            return new MangakakalotMangaScrapper();
        } else if (scrapperType.equalsIgnoreCase("Nettruyen")) {
            return new NettruyenMangaScrapper();
        }

        return null;
    }
}
