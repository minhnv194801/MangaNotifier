package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;

import database.DBConnector;
import manga.Manga;

public class ListMenu {

	protected JFrame frame;
	protected Container contentPane;
	protected JPanel buttonPanel;
	protected JButton addButton;
	protected JButton removeButton;
	protected JButton visitButton;
	protected static JList listPanel;
	protected static List<Manga> mangaLst;
	protected static DBConnector dbms;
	private ResultListMenu resultListMenu;

	public ListMenu() {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}

		frame = new JFrame();
		contentPane = frame.getContentPane();

		buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		addButton = new JButton("Add");
		
		buttonPanel.add(addButton);
		removeButton = new JButton("Remove");
		removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeItem();
			}
		});
		buttonPanel.add(removeButton);
		visitButton = new JButton("Visit");
		visitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				visitItem();
			}
		});
		buttonPanel.add(visitButton);
		contentPane.add(buttonPanel, BorderLayout.NORTH);

		frame.setTitle("List");
		frame.setSize(600, 300);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	public ListMenu(DBConnector dbms, List<Manga> mangaLst) {
		this();
		this.dbms = dbms;
		this.mangaLst = mangaLst;
		
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addItem();
			}
		});
		
		listPanel = new JList();
		listPanel.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
		JScrollPane listScrollPane = new JScrollPane(listPanel);
		listScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		contentPane.add(listScrollPane, BorderLayout.CENTER);

		resultListMenu = new ResultListMenu();
		listPanel.setListData(mangaLst.toArray());
	}

	public void addItem() {
		if (!resultListMenu.isVisible()) {
			String keyword = JOptionPane.showInputDialog("Please input keyword of the manga you wished to add");
			if (keyword != null) {
				resultListMenu.setKeyword(keyword);
				resultListMenu.show();
			}
		} else {
			resultListMenu.show();
		}
	}

	public void removeItem() {
		int indexToRemove = listPanel.getSelectedIndex();
		if (indexToRemove < 0) {
			JOptionPane.showMessageDialog(null, "You have to select a manga!",
					"ERROR!", JOptionPane.ERROR_MESSAGE);
		} else {
			Manga mangaToRemove = mangaLst.remove(indexToRemove);
			dbms.deleteManga(mangaToRemove);
			listPanel.setListData(mangaLst.toArray());
		}
	}

	public void visitItem() {
		int selectedItem = listPanel.getSelectedIndex();
		if (selectedItem < 0) {
			JOptionPane.showMessageDialog(null, "You have to select a manga!",
					"ERROR!", JOptionPane.ERROR_MESSAGE);
		} else {
			try {
				Desktop.getDesktop().browse(new URI(mangaLst.get(selectedItem).getUrl())); 
			} catch (IOException | URISyntaxException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void show() {
		frame.setVisible(true);
	}
}
