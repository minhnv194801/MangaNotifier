package gui;

import java.util.ArrayList;
import java.util.List;

import manga.Manga;
import scrapper.Scrapper;

public class ResultListMenu extends ListMenu {

	public ResultListMenu(String keyword) {
		super();
		buttonPanel.remove(removeButton);
		buttonPanel.remove(visitButton);
		
		List<Manga> resultMangaLst = Scrapper.fetchManga(keyword);
		listPanel.setListData(resultMangaLst.toArray());
		
	}
	
	public static void main(String args[]) {
		ResultListMenu test = new ResultListMenu("Arifureta");
		test.show();
	}
	
}
