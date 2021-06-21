import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

	private static DBConnector dbms;
	private static List<Manga> mangaLst = new ArrayList<Manga>();
	private static Scanner sc = new Scanner(System.in);
	private static boolean newReleaseExists = false;

	public Main() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String args[]) {
		dbms = new DBConnector(args[0]);
		boolean isExit = false;

		mangaLst = dbms.fetchManga();
		
		class checkNewUpdates implements Runnable {
			private Manga mangaToCheck;
			
			public checkNewUpdates(Manga mangaToCheck) {
				this.mangaToCheck = mangaToCheck;
			}
			
			public void run() {
				if (Scrapper.fetchManga(mangaToCheck)) {
					announceNewRelease(mangaToCheck);
					newReleaseExists = true;
					dbms.updateManga(mangaToCheck);
				}
			}
		}
		
		for (Manga aManga: mangaLst) { 
			Thread th = new Thread(new checkNewUpdates(aManga));
			th.setDaemon(true);
			th.start();
		}
		
		while (Thread.activeCount() != 1);	
		if (!newReleaseExists) {
			System.out.println("You are all up-to-date!");
		}

		while(!isExit) {
			showMenu();
			String buffer = sc.nextLine();
			try {
				int choice = Integer.parseInt(buffer.trim());
				switch (choice) {
				case 1:
					System.out.print("Input manga keyword: ");
					buffer = sc.nextLine();
					List<Manga> foundMangaLst = Scrapper.fetchManga(buffer.trim());
					if (!foundMangaLst.isEmpty()) {
						System.out.println("There are " + foundMangaLst.size() + " results matches your keyword");
						for (Manga aManga: foundMangaLst) {
							System.out.println("Is '" + aManga.getTitle() + "' is the title you interested in? (Y/N)");
							buffer = sc.nextLine();
							if (buffer.trim().toUpperCase().equals("Y")) {
								if (!mangaLst.contains(aManga)) {
									mangaLst.add(aManga);
									dbms.insertManga(aManga);
									System.out.println(aManga.getTitle() + " has been added!");
									announceNewRelease(aManga);
								} else {
									System.out.println("That manga is already in interested list");
									announceNewRelease(aManga);
								}
							} else if (!buffer.trim().toUpperCase().equals("N")) {
								System.out.println("Invalid input! Skipped");
								continue;
							}
						}
					} else {
						System.out.println("There are no results matches your keyword");
					}
					break;
				case 2:
					showMangaList(mangaLst);
					break;
				case 3:
					showMangaList(mangaLst);
					System.out.print("Input index to remove: ");
					buffer = sc.nextLine();
					try {
						int indexToRemove = Integer.parseInt(buffer.trim());
						if (indexToRemove <= mangaLst.size() && indexToRemove > 0) {
							Manga mangaToRemove = mangaLst.remove(indexToRemove - 1);
							dbms.deleteManga(mangaToRemove);
							System.out.println("Manga remove successfully");
						} else {
							showError();
						}
					} catch (Exception e) {
						showError();
					}
					break;
				case 0:
					isExit = true;
					System.out.println("Exiting! Hava a nice day!");
					break;
				default:
					showError();
				};
			} catch (Exception e) {
				showError();
			}
		}

		dbms.close();
		sc.close();
	}

	/*
	public static synchronized void checkNewUpdates(Manga aManga) {
		if (Scrapper.fetchManga(aManga)) {
			announceNewRelease(aManga);
			newReleaseExists = true;
			dbms.updateManga(aManga);
		}
	}
	*/

	public static void announceNewRelease(Manga aManga) {
		System.out.println("Check out the latest release of " + aManga.getTitle() + "! " + aManga.getLatestTitle());
		System.out.println(aManga.getLatestUrl());
	}

	public static void showMenu() {
		System.out.println("\n***************************************************");
		System.out.println("MENU");
		System.out.println("1. Add new manga");
		System.out.println("2. List all currently watching manga");
		System.out.println("3. Remove manga (By list index)");
		System.out.println("0. Exit");
		System.out.println("***************************************************");
		System.out.print("Input: ");
	}

	public static void showError() {
		System.out.println("Invalid input! Please try again");
	}

	public static void showMangaList(List<Manga> mangaLst) {
		int count = 1;
		System.out.println("\nCurrent list: ");
		for (Manga aManga: mangaLst) {
			System.out.println(count + ". " + aManga);
			count++;
		}
	}

}
