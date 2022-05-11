package entity.manga;

import entity.database.MangaDatabase;
import javafx.scene.image.Image;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Manga {

    private int mangaId;
    private final String mangaTitle;
    private final String mangaUrl;
    private final String mangaImageUrl;
    private boolean isBookmarked;
    private List<MangaChapter> chapterList = new ArrayList<MangaChapter>();

    public Manga(String title, String url, String imageUrl, List<String> latestChapters, List<String> latestChapterUrls) {
        this.mangaTitle = title;
        this.mangaUrl = url;
        this.mangaImageUrl = imageUrl;
        for (int i = latestChapters.size() - 1; i >= 0; i--) {
            MangaChapter chapter = new MangaChapter(latestChapters.get(i), latestChapterUrls.get(i));
            chapterList.add(chapter);
        }
    }

    public Manga(int id, String title, String url, String imageUrl, boolean isBookmarked) {
        this.mangaId = id;
        this.mangaTitle = title;
        this.mangaUrl = url;
        this.mangaImageUrl = imageUrl;
        this.isBookmarked = isBookmarked;
        chapterList = new MangaChapter().getAllChapter();
    }

    public static List<Manga> getAllManga() {
        List<Manga> mangaLst = new ArrayList<Manga>();

        try {
            String sql = "SELECT * FROM mangas ORDER BY manga_title;";
            Statement stm = MangaDatabase.getConnection().createStatement();
            ResultSet res = stm.executeQuery(sql);
            while (res.next()) {
                mangaLst.add(new Manga(res.getInt(1), res.getString(2), res.getString(3), res.getString(4), res.getBoolean(5)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mangaLst;
    }

    public static List<Manga> getAllBookmarkedManga() {
        List<Manga> mangaLst = new ArrayList<Manga>();

        try {
            String sql = "SELECT * FROM mangas WHERE is_bookmark = 1 ORDER BY manga_title;";
            Statement stm = MangaDatabase.getConnection().createStatement();
            ResultSet res = stm.executeQuery(sql);
            while (res.next()) {
                mangaLst.add(new Manga(res.getInt(1), res.getString(2), res.getString(3), res.getString(4), res.getBoolean(5)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mangaLst;
    }

    public static List<Manga> getMangaByKeyword(String keyword) {
        List<Manga> mangaLst = new ArrayList<Manga>();
        if (keyword == null) {
            return Manga.getAllManga();
        }
        keyword = keyword.trim();
        try {
            String sql = "SELECT * FROM mangas "
                    + "WHERE manga_title LIKE " + "'%" + keyword.replaceAll("'", "''") + "%'"
                    + " ORDER BY manga_title;";
            Statement stm = MangaDatabase.getConnection().createStatement();
            ResultSet res = stm.executeQuery(sql);
            while (res.next()) {
                mangaLst.add(new Manga(res.getInt(1), res.getString(2), res.getString(3), res.getString(4), res.getBoolean(5)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mangaLst;
    }

    public void save() {
        try {
            String sql = "INSERT INTO mangas (\n" +
                    "                       manga_title,\n" +
                    "                       manga_url,\n" +
                    "                       manga_image_url\n" +
                    "                   ) VALUES ("
                    + "'" + this.getTitle().replaceAll("'", "''") + "', "
                    + "'" + this.getUrl().replaceAll("'", "''") + "', "
                    + "'" + this.getImageUrl().replaceAll("'", "''")
                    + "');";
            Statement stm = MangaDatabase.getConnection().createStatement();
            stm.execute(sql);
            for (MangaChapter chapter: chapterList) {
                chapter.save();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void remove() {
        try {
            for (MangaChapter chapter: chapterList) {
                chapter.remove();
            }

            String sql = "DELETE FROM mangas WHERE "
                    + "manga_id = " + this.mangaId
                    + ";";
            Statement stm = MangaDatabase.getConnection().createStatement();
            stm.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update() {
        for (MangaChapter chapter: chapterList) {
            chapter.update();
        }
    }

    public void bookmark() {
        isBookmarked = true;
        try {
            Connection conn = MangaDatabase.getConnection();
            String sql = "UPDATE mangas\n" +
                    "        SET is_bookmark = 1\n" +
                    "        WHERE manga_id = " + this.getMangaId() + ";";
            Statement stm = conn.createStatement();
            ResultSet res = stm.executeQuery(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void unBookmark() {
        isBookmarked = false;
        try {
            Connection conn = MangaDatabase.getConnection();
            String sql = "UPDATE mangas\n" +
                    "        SET is_bookmark = 0\n" +
                    "        WHERE manga_id = " + this.getMangaId() + ";";
            Statement stm = conn.createStatement();
            ResultSet res = stm.executeQuery(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isExist() {
        try {
            Connection conn = MangaDatabase.getConnection();
            String sql = "SELECT * FROM mangas "
                    + "WHERE manga_title LIKE " + "'" + mangaTitle.replaceAll("'", "''") + "';";
            Statement stm = conn.createStatement();
            ResultSet res = stm.executeQuery(sql);
            return res.next();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean isBookmarked() {
        return this.isBookmarked;
    }

    @Override
    public String toString() {
        return "title:'" + mangaTitle + "' imageUrl: '" + mangaImageUrl + "'";
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Manga) {
            Manga aManga = (Manga) o;
            if (mangaTitle.equalsIgnoreCase(aManga.getTitle())) {
                return this.chapterList.get(chapterList.size() - 1).isExist()
                        && aManga.chapterList.get(chapterList.size() - 1).isExist();
            }
            return (mangaTitle.equals(aManga.getTitle())
                    && this.getLatestTitles().get(0).equals(aManga.getLatestTitles().get(0)));
        } else {
            return false;
        }
    }

    public int getMangaId() {
        if (this.mangaId == 0) {
            this.mangaId = Manga.getMangaByKeyword(this.getTitle()).get(0).mangaId;
        }

        return this.mangaId;
    }

    public String getTitle() {
        return this.mangaTitle;
    }

    public String getUrl() {
        return this.mangaUrl;
    }

    public String getImageUrl() { return this.mangaImageUrl;}

    public Image getMangaImage() {
        return new Image(this.getImageUrl());
    }

    public List<String> getLatestTitles() {
        List<String> latestChapters = new ArrayList<>();
        for (MangaChapter chapter: chapterList) {
            latestChapters.add(chapter.chapterTitle);
        }
        return latestChapters;
    }

    public List<String> getLatestUrls() {
        List<String> latestUrls = new ArrayList<>();
        for (MangaChapter chapter: chapterList) {
            latestUrls.add(chapter.chapterUrl);
        }
        return latestUrls;
    }

    private class MangaChapter {
        private int chapterId;
        private String chapterTitle;
        private String chapterUrl;

        private MangaChapter() {
        }

        private MangaChapter(String chapterTitle, String chapterUrl) {
            this.chapterTitle = chapterTitle;
            this.chapterUrl = chapterUrl;
        }

        private MangaChapter(int chapterId, String chapterTitle, String chapterUrl) {
            this.chapterId = chapterId;
            this.chapterTitle = chapterTitle;
            this.chapterUrl = chapterUrl;
        }

        private List<MangaChapter> getAllChapter() {
            List<MangaChapter> chapterList = new ArrayList<>();

            try {
                String sql = "SELECT * FROM manga_chapters WHERE manga_id = " + mangaId +";";
                Statement stm = MangaDatabase.getConnection().createStatement();
                ResultSet res = stm.executeQuery(sql);
                while (res.next()) {
                    chapterList.add(new MangaChapter(res.getInt(1), res.getString(2), res.getString(3)));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return chapterList;
        }

        private void save() {
            try {
                String sql = "INSERT INTO manga_chapters (\n" +
                        "                               chapter_title,\n" +
                        "                               chapter_url,\n" +
                        "                               manga_id\n" +
                        "                           ) VALUES ("
                        + "'" + chapterTitle.replaceAll("'", "''") + "', "
                        + "'" + chapterUrl.replaceAll("'", "''") + "', "
                        + getMangaId()
                        + ");";
                Statement stm = MangaDatabase.getConnection().createStatement();
                stm.execute(sql);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void remove() {
            try {
                String sql = "DELETE FROM manga_chapters WHERE "
                        + "manga_id = " + getMangaId()
                        + ";";
                Statement stm = MangaDatabase.getConnection().createStatement();
                stm.execute(sql);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void update() {
            if (!this.isExist()) {
                this.save();
            }
        }

        private boolean isExist() {
            try {
                Connection conn = MangaDatabase.getConnection();
                String sql = "SELECT * FROM manga_chapters "
                        + "WHERE chapter_url LIKE " + "'" + this.chapterUrl.replaceAll("'", "''") + "';";
                Statement stm = conn.createStatement();
                ResultSet res = stm.executeQuery(sql);
                return res.next();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }
    }
}
