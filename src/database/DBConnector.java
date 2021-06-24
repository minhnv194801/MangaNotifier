package database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import exceptions.ConnectionException;
import manga.Manga;

public class DBConnector {

	private static Connection c;
	private static Statement stmt;
	
	public DBConnector() throws ConnectionException {
		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager
					.getConnection("jdbc:postgresql://localhost:5432/",
							"postgres", "123");
			String createDB = "CREATE DATABASE mangadb";
			stmt = c.createStatement();
			try {
				stmt.executeUpdate(createDB);
			} catch (Exception e) { }
			
			c = DriverManager
					.getConnection("jdbc:postgresql://localhost:5432/mangadb",
							"postgres", "123");
			String createSql = "CREATE TABLE IF NOT EXISTS manga("
					+ "	manga_title text PRIMARY KEY,"
					+ "	manga_url text,"
					+ "	latest_chapter text,"
					+ "	latest_chapter_url text"
					+ ");";
			stmt = c.createStatement();
			stmt.execute(createSql);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ConnectionException("Fail to connect the database");
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
	
	public void deleteManga(Manga mangaToRemove) {
		String sql = "DELETE FROM manga WHERE manga_title = '" + mangaToRemove.getTitle().replaceAll("'", "''") + "'";
		try {
			stmt.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<Manga> fetchManga() {
		List<Manga> interestedMangaLst = new ArrayList<Manga>();
		String sql = "SELECT * FROM manga ORDER BY manga_title";
		
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
	
	public synchronized void updateManga(Manga manga) {
		String sql = "UPDATE manga SET "
				+ "latest_chapter = " + "'" + manga.getLatestTitle().replaceAll("'", "''") + "', "
				+ "latest_chapter_url = " + "'" + manga.getLatestUrl().replaceAll("'", "''") + "'"
				+ "WHERE manga_title = " + "'" + manga.getTitle().replaceAll("'", "''") + "';";
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
