import java.util.ArrayList;
import java.util.List;

public class Main {

	private static DBConnector dbms;
	private static List<Manga> mangaLst = new ArrayList<Manga>();
	private static Object lock = new Object();

	public Main() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String args[]) {
		dbms = new DBConnector();

		mangaLst = dbms.fetchManga();
		
		MainMenu mainMenu = new MainMenu(dbms, mangaLst);
		
		Thread th = new Thread() {
			@Override
			public void run() {
				synchronized(lock) {
					while (mainMenu.isVisible()) {
						try {
							Thread.sleep(1000);
						} catch (Exception e) {
							break;
						}
					}
					dbms.close();
					System.exit(0);
				}
			}
		};
		th.start();
	}

}
