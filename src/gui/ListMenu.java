package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import javax.swing.DefaultListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import database.DBConnector;
import manga.Manga;

public class ListMenu {
	
	private static JFrame frame;
	private static Container contentPane;
	private static JButton addButton;
	private static JButton removeButton;
	private static JButton visitButton;
	private static JList listPanel;
	private static List<Manga> mangaLst;
	private static DBConnector dbms;
	
	static {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		frame = new JFrame();
		contentPane = frame.getContentPane();
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		addButton = new JButton("Add");
		buttonPanel.add(addButton);
		removeButton = new JButton("Remove");
		buttonPanel.add(removeButton);
		visitButton = new JButton("Visit");
		visitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
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
		});
		buttonPanel.add(visitButton);
		contentPane.add(buttonPanel, BorderLayout.NORTH);
		
		listPanel = new JList();
		listPanel.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);
		JScrollPane listScrollPane = new JScrollPane(listPanel);
		listScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		contentPane.add(listScrollPane, BorderLayout.CENTER);

		frame.setTitle("List");
		frame.setSize(600, 300);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	}

	public ListMenu(DBConnector dbms, List<Manga> mangaLst) {
		this.dbms = dbms;
		this.mangaLst = mangaLst;
		
		listPanel.setListData(mangaLst.toArray());
	}
	
	public void show() {
		frame.setVisible(true);
	}

}
