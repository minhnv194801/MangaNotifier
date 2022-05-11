package entity.scrapper;

import entity.manga.Manga;
import exceptions.ConnectionException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NettruyenMangaScrapper extends MangaScrapper {

    private final char[] chars = {'%', '[', '/', '$', '&', '+', ',', ':', ';', '=', '?', '@', '#', '|', '\\', '<', '>', '.', '^', '*', '(', ')', '!', '-', ']'};

    public NettruyenMangaScrapper() {
        this.url = "http://www.nettruyengo.com/tim-truyen?keyword=";
    }

    @Override
    public List<Manga> fetchManga(String mangaKeyword) throws ConnectionException {
        mangaKeyword = convertStringToProperUrl(mangaKeyword);

        List<Manga> resultMangas = new ArrayList<Manga>();

        try {
            Document doc = Jsoup.connect(url + mangaKeyword)
                    .userAgent("Chrome/33.0.1750.152")
                    .get();
            Elements mangaElements = doc.select("div.item");

            for (Element mangaElement: mangaElements) {
                String mangaTitle = mangaElement.selectFirst("h3").selectFirst("a").ownText();
                String mangaUrl = mangaElement.selectFirst("h3").selectFirst("a").attr("href");
                String mangaImage = "http:" + mangaElement.selectFirst("div.image").selectFirst("img").attr("data-original");
                List<String> chapterTitles = new ArrayList<String>();
                List<String> chapterUrls = new ArrayList<String>();

                Elements chapterElements = mangaElement.select("li");
                for (Element chapterElement : chapterElements) {
                    String chapterTitle = chapterElement.selectFirst("a").ownText();
                    chapterTitles.add(chapterTitle);
                    String chapterUrl = chapterElement.selectFirst("a").attr("href");
                    chapterUrls.add(chapterUrl);
                }

                resultMangas.add(new Manga(mangaTitle, mangaUrl, mangaImage, chapterTitles, chapterUrls));
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new ConnectionException("Fail to connect the website");
        }

        return resultMangas;
    }

    @Override
    public Manga fetchManga(Manga manga) throws ConnectionException {
        String keyword = manga.getTitle();

        List<Manga> mangaList = fetchManga(keyword);

        for (Manga fetchManga: mangaList) {
            if (fetchManga.getTitle().equals(manga.getTitle())) {
                return fetchManga;
            }
        }

        return null;
    }

    private String convertStringToProperUrl(String str) {
        str = str.trim();

        for (char c: chars) {
            String specialChar = "";
            specialChar += c;
            str = str.replace(specialChar, "%" + String.format("%02x", (int) c));
        }

        str = str.replace(' ', '+');

        return str;
    }

}
