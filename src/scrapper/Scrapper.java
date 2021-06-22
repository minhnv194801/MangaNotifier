package scrapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import manga.Manga;

public class Scrapper {
	
	public Scrapper() {
		// TODO Auto-generated constructor stub
	}

	//Use for new manga where we only know keyword
	public static synchronized List<Manga> fetchManga(String mangaKeyword) {
		String search = "https://mangakakalot.com/search/story/";
		List<Manga> resultMangas = new ArrayList<Manga>();
		
		try {
			Document doc = Jsoup.connect(search + mangaKeyword.trim().replaceAll("[$&+,:;=?@#|'<>.^*()%!-]", "_").replaceAll(" ", "_")).get();
			Elements mangaElements = doc.select("div.story_item");

			for (Element anElement: mangaElements) {
				Element mangaElement = anElement.selectFirst("h3.story_name").selectFirst("a");
				String mangaTitle = mangaElement.ownText();
				String mangaUrl = mangaElement.attr("href");
				
				Element chapterElement = anElement.selectFirst("em.story_chapter").selectFirst("a");
				String chapterTitle = chapterElement.ownText();
				String chapterUrl = chapterElement.attr("href");
				
				String mangaImage = anElement.selectFirst("img").attr("src");
				
				resultMangas.add(new Manga(mangaTitle, mangaUrl, chapterTitle, chapterUrl));
			}
		} catch (IOException e) {
			System.out.println("Fail to connect the website");
		} 
		
		return resultMangas;
	}
	
	public static synchronized boolean fetchManga(Manga manga) {
		String search = "https://mangakakalot.com/search/story/";
		try {
			Document doc = Jsoup.connect(search + manga.getTitle().trim().replaceAll("[$&+,:;=?@#|'<>.^*()%!-]", "_").replaceAll(" ", "_")).get();
			Element latestChapterElement = doc.select("div.story_item").first().selectFirst("em.story_chapter").selectFirst("a");
			
			String latestChapterTitle = latestChapterElement.ownText();
			String latestChapterUrl = latestChapterElement.attr("href");
			
			if (latestChapterTitle.equals(manga.getLatestTitle())) {
				return false;
			} else {
				manga.clone(new Manga(manga.getTitle(), manga.getUrl(), latestChapterTitle, latestChapterUrl));
				return true;
			}
		} catch (IOException e) {
			System.out.println("Fail to connect the website of " + manga.getTitle());
			System.out.println(search + manga.getTitle().trim().replaceAll("[$&+,:;=?@#|'<>.^*()%!-]", "_").replaceAll(" ", "_"));
			return false;
		}
	}

}
