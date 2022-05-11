package entity.scrapper;

import entity.manga.Manga;
import exceptions.ConnectionException;

import java.util.List;

public abstract class MangaScrapper {
    protected String url;

    public abstract List<Manga> fetchManga(String mangaKeyword) throws ConnectionException;

    public abstract Manga fetchManga(Manga manga) throws ConnectionException;
}
