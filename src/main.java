import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import database.DBConnector;
import gui.MainMenu;
import manga.Manga;

public class main {

	private static DBConnector dbms;
	private static List<Manga> mangaLst = new ArrayList<Manga>();
	private static MainMenu mainMenu;

	public main() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String args[]) {
		dbms = new DBConnector();

		mangaLst = dbms.fetchManga();

		mainMenu = new MainMenu(dbms, mangaLst);
	}
}
