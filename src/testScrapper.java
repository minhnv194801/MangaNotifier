import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
public class testScrapper {

	public testScrapper() {
		// TODO Auto-generated constructor stub
	}

	public static void main( String[] args ) throws IOException{  

		String search = "https://mangakakalot.com/search/story/";
		String title = "Arifureta";

		Document doc = Jsoup.connect(search + title.trim()).get();  
		
		Elements storyItems = doc.select("div.story_item");
		
		for (Element anElement: storyItems) {
			String mangaTitle = anElement.select("h3.story_name").first().select("a").first().ownText();
			Element chapterElement = anElement.select("em.story_chapter").first().select("a").first();
			String mangaChapter = chapterElement.ownText();
			String mangaUrl = chapterElement.attr("href");
			System.out.print(mangaTitle + " ");
			System.out.println(mangaChapter);
			System.out.println(mangaUrl);
		}
	}  
}