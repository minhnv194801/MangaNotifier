import java.util.List;

public class Main {

	public Main() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String args[]) {
		DBConnector dbms = new DBConnector(args[0]);
		List<Manga> testList = dbms.fetchManga();
		boolean newReleaseExists = false;
		
		for (Manga aManga: testList) {
			if (Scrapper.fetchManga(aManga)) {
				announceNewRelease(aManga);
				newReleaseExists = true;
				dbms.updateManga(aManga);
			}
		}
		
		if (!newReleaseExists) {
			System.out.println("You are all up-to-date!");
		}
		
		dbms.close();
	}
	
	public static void announceNewRelease(Manga aManga) {
		System.out.println("Check out the latest release of " + aManga.getTitle() + "! " + aManga.getLatestTitle());
		System.out.println(aManga.getLatestUrl());
	}

}
