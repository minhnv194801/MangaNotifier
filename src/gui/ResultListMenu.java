package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import exceptions.ConnectionException;
import manga.Manga;
import scrapper.Scrapper;

public class ResultListMenu extends ListMenu {

	private List<Manga> resultMangaLst;
	private JLabel resultLabel = new JLabel();
	private JList resultList = new JList();

	public ResultListMenu() {
		super();
		addButton.removeAll();
		buttonPanel.remove(removeButton);
		buttonPanel.remove(visitButton);
		buttonPanel.add(resultLabel);

		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int indexToAdd = resultList.getSelectedIndex();
				if (indexToAdd < 0) {
					JOptionPane.showMessageDialog(null, "You have to select a manga!",
							"ERROR!", JOptionPane.ERROR_MESSAGE);
				} else {
					Manga mangaToAdd = resultMangaLst.remove(indexToAdd);
					if (!mangaLst.contains(mangaToAdd)) {
						mangaLst.add(mangaToAdd);
						dbms.insertManga(mangaToAdd);

						JPanel hyperlinkPane = new JPanel();
						hyperlinkPane.setLayout(new FlowLayout());
						hyperlinkPane.add(new JLabel("Manga registered successfully! Please check out the latest release of "));
						JLabel hyperlink = new JLabel(mangaToAdd.getTitle());
						hyperlink.setForeground(Color.BLUE.darker());
						hyperlink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
						hyperlink.addMouseListener(new MouseAdapter() {
							@Override
							public void mouseClicked(MouseEvent e) {
								try {
									Desktop.getDesktop().browse(new URI(mangaToAdd.getLatestUrl()));     
								} catch (IOException | URISyntaxException e1) {
									e1.printStackTrace();
								}
							}

							@Override
							public void mouseEntered(MouseEvent e) {
								hyperlink.setText("<html><a href=''>" + mangaToAdd.getTitle() +"</a></html>");
							}

							@Override
							public void mouseExited(MouseEvent e) {
								hyperlink.setText(mangaToAdd.getTitle());
							}
						});
						hyperlinkPane.add(hyperlink);
						JOptionPane.showMessageDialog(null, hyperlinkPane,
								"Successful!", JOptionPane.INFORMATION_MESSAGE);
						listPanel.setListData(mangaLst.toArray());
					} else {
						JOptionPane.showMessageDialog(null, "The manga you try to add is already in the registered list",
								"ERROR!", JOptionPane.ERROR_MESSAGE);
					}
					if (resultMangaLst.size() == 0) {
						frame.dispose();
					} else {
						resultLabel.setText("Found " + resultMangaLst.size() + " results based on your keyword");
						resultList.setListData(resultMangaLst.toArray());
					}
				}
			}
		});

		resultList = new JList();
		resultList.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
		JScrollPane listScrollPane = new JScrollPane(resultList);
		listScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		contentPane.add(listScrollPane, BorderLayout.CENTER);

		frame.setTitle("Result List");
	}

	public void setKeyword(String keyword) {
		try {
			List<Manga> mangaLst = Scrapper.fetchManga(keyword);
			resultMangaLst = mangaLst;
			resultLabel.setText("Found " + resultMangaLst.size() + " results based on your keyword");
			resultList.setListData(resultMangaLst.toArray());
		} catch (ConnectionException e) {
			JOptionPane.showMessageDialog(null, "Fail to connect the website! Please check your connection then try again",
					"ERROR!", JOptionPane.ERROR_MESSAGE);
		}
	}

	public boolean isVisible() {
		return frame.isVisible();
	}
}
