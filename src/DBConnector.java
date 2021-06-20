import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DBConnector {

	private static Connection c;
	private static Statement stmt;
	
	public DBConnector(String password) {
		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager
					.getConnection("jdbc:postgresql://localhost:5432/MangaDB",
							"postgres", password);
			stmt = c.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	public void insertManga(Manga newManga) {
		String sql = "INSERT INTO manga VALUES "
				+ "('" + newManga.getTitle().replaceAll("'", "''") + "', "
				+ "'" + newManga.getUrl().replaceAll("'", "''") + "', "
				+ "'" + newManga.getLatestTitle().replaceAll("'", "''") + "', "
				+ "'" + newManga.getLatestUrl().replaceAll("'", "''") + "')";
		try {
			stmt.executeUpdate(sql);
		} catch (Exception e) {
			if (!e.getMessage().contains("duplicate key value")) {
				e.printStackTrace();
				System.out.println("Fail to insert new manga");
			}
		}
	}

	public List<Manga> fetchManga() {
		List<Manga> interestedMangaLst = new ArrayList<Manga>();
		String sql = "SELECT * FROM manga";
		
		try {
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				interestedMangaLst.add(new Manga(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)));
			}
		} catch(Exception e) {
			e.printStackTrace();
		}

		return interestedMangaLst;
	}
	
	public void updateManga(Manga manga) {
		String sql = "UPDATE manga SET "
				+ "latest_chapter = " + "'" + manga.getLatestTitle() + "', "
				+ "latest_chapter_url = " + "'" + manga.getLatestUrl() + "'"
				+ "WHERE manga_title = " + "'" + manga.getTitle() + "'";
		try {
			stmt.executeUpdate(sql);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			stmt.close();
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Fail to close the db properly");
		}
	}
}