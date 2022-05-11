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

public class MangakakalotMangaScrapper extends MangaScrapper {

    public MangakakalotMangaScrapper() {
        this.url = "https://mangakakalot.com/search/story/";
    }

    public List<Manga> fetchManga(String mangaKeyword) throws ConnectionException {
        List<Manga> resultMangas = new ArrayList<Manga>();

        try {
            Document doc = Jsoup.connect(url + mangaKeyword.trim().replaceAll("[\\[/$&+,:;=?@#|'<>.^*()%!-\\]]", "_").replaceAll(" ", "_")).get();
            Elements mangaElements = doc.select("div.story_item");

            for (Element anElement : mangaElements) {
                Element mangaElement = anElement.selectFirst("h3.story_name").selectFirst("a");
                String mangaTitle = mangaElement.ownText();
                String mangaUrl = mangaElement.attr("href");
                String mangaImage = anElement.selectFirst("img").attr("src");
                List<String> chapterTitles = new ArrayList<String>();
                List<String> chapterUrls = new ArrayList<String>();

                Elements chapterElements = anElement.select("em.story_chapter");
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

    public Manga fetchManga(Manga manga) throws ConnectionException {
        try {
            Document doc = Jsoup.connect(url + manga.getTitle().trim().replaceAll("[\\[/$&+,:;=?@#|'<>.^*()%!-\\]]", "_").replaceAll(" ", "_")).get();
            Elements mangaElements = doc.select("div.story_item");

            for (Element anElement : mangaElements) {
                String mangaTitle = anElement.selectFirst("h3.story_name").selectFirst("a").ownText();
                if (!mangaTitle.equals(manga.getTitle())) {
                    continue;
                }

                String mangaUrl = anElement.attr("href");
                String mangaImage = anElement.selectFirst("img").attr("src");
                List<String> chapterTitles = new ArrayList<String>();
                List<String> chapterUrls = new ArrayList<String>();

                Elements chapterElements = anElement.select("em.story_chapter");
                for (Element chapterElement : chapterElements) {
                    String chapterTitle = chapterElement.selectFirst("a").ownText();
                    chapterTitles.add(chapterTitle);
                    String chapterUrl = chapterElement.selectFirst("a").attr("href");
                    chapterUrls.add(chapterUrl);
                }

                return new Manga(mangaTitle, mangaUrl, mangaImage, chapterTitles, chapterUrls);
            }
        } catch (IOException e) {
            String msg = "";
            msg += "Fail to connect the website of " + manga.getTitle() + "\n";
            msg += url + manga.getTitle().trim().replaceAll("[/$&+,:;=?@#|'<>.^*()%!-]", "_").replaceAll(" ", "_") + "\n";
            e.printStackTrace();
            throw new ConnectionException(msg);
        }

        return null;
    }
}
