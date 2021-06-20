public class Manga {

	private String mangaTitle;
	private String mangaUrl;
	private String latestChapter;
	private String latestChapterUrl;

	public Manga(String title, String url, String latestChapter, String latestChapterUrl) {
		this.mangaTitle = title;
		this.mangaUrl = url;
		this.latestChapter = latestChapter;
		this.latestChapterUrl = latestChapterUrl;
	}
	
	public void clone(Manga manga) {
		this.mangaTitle = manga.getTitle();
		this.mangaUrl = manga.getUrl();
		this.latestChapter = manga.getLatestTitle();
		this.latestChapterUrl = manga.getLatestUrl();
	}
	
	@Override
	public String toString() {
		return mangaTitle + ": " + mangaUrl;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Manga) {
			Manga aManga = (Manga) o;
			return mangaTitle.equals(aManga.getTitle());
		} else {
			return false;
		}
	}
	
	public String getTitle() {
		return this.mangaTitle;
	}
	public String getUrl() {
		return this.mangaUrl;
	}
	public String getLatestTitle() {
		return this.latestChapter;
	}
	public String getLatestUrl() {
		return this.latestChapterUrl;
	}
}
