import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import database.DBConnector;
import exceptions.ConnectionException;
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
		try {
			dbms = new DBConnector();
		} catch (ConnectionException e) {
			JOptionPane.showMessageDialog(null, "Fail to connect to the database! Program will exit",
					"ERROR!", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}

		mangaLst = dbms.fetchManga();

		SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
		    	mainMenu = new MainMenu(dbms, mangaLst);
		    }
		});
	}
}
