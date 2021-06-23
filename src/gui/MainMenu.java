package gui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.Semaphore;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;

import database.DBConnector;
import manga.Manga;
import scrapper.Scrapper;

public class MainMenu {

	private static JFrame frame;
	private static Container contentPane;
	private static JMenuBar menuBar;
	private static JMenu menu;
	private static JPanel upperPanel;
	private static JEditorPane belowPanel;
	private static boolean newReleaseExists = false;
	private static ListMenu listMenu;
	private static JButton updateButton;
	private List<Manga> mangaLst;
	private DBConnector dbms;
	private Color newColor = new Color(135,206,250);

	private static final int MAXIMUM_NUMBER_OF_PERMIT = 10;
	private Semaphore semaphore = new Semaphore(MAXIMUM_NUMBER_OF_PERMIT);

	static {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}

		frame = new JFrame();
		contentPane = frame.getContentPane();
		menuBar = new JMenuBar();
		menu = new JMenu("List");
		menu.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				listMenu.show();
			}
			public void mouseExited(MouseEvent e) {

			}
			public void mouseReleased(MouseEvent e) {

			}
			public void mousePressed(MouseEvent e) {

			}
			public void mouseEntered(MouseEvent e) {

			}
		}); 
		upperPanel = new JPanel();
		belowPanel = new JEditorPane();
		belowPanel.addComponentListener(new ComponentListener() {
			public void componentResized(ComponentEvent e) { }
			public void componentMoved(ComponentEvent e) { }
			public void componentShown(ComponentEvent e) { }
			public void componentHidden(ComponentEvent e) { }
		});

		menuBar.add(menu);
		frame.setJMenuBar(menuBar);


		frame.setTitle("SirMangaNox");
		frame.setSize(1200, 800);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
	}

	public MainMenu(DBConnector dbms, List<Manga> mangaLst) {	
		Thread t = new Thread() {
			@Override
			public void run() {
				listMenu = new ListMenu(dbms, mangaLst);
			}
		};
		t.setDaemon(true);
		t.run();

		this.dbms = dbms;
		this.mangaLst = mangaLst;

		JLabel label;
		ImageIcon icon = new ImageIcon();

		icon = new ImageIcon(getClass().getResource("/resources/MainMenuIcons/Icon1.jpg"));
		addIconToPanel(upperPanel, icon);

		icon = new ImageIcon(getClass().getResource("/resources/MainMenuIcons/Icon2.jpg"));
		addIconToPanel(upperPanel, icon);

		label = new JLabel("WELCOME!", JLabel.CENTER);
		label.setFont(new Font("Freestyle Script", Font.PLAIN, 100));
		label.setForeground(Color.WHITE);
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		upperPanel.add(label);

		icon = new ImageIcon(getClass().getResource("/resources/MainMenuIcons/Icon3.jpg"));
		addIconToPanel(upperPanel, icon);

		icon = new ImageIcon(getClass().getResource("/resources/MainMenuIcons/Icon4.jpg"));
		addIconToPanel(upperPanel, icon);

		upperPanel.setBackground(newColor);
		contentPane.add(upperPanel, BorderLayout.PAGE_START);

		belowPanel.setLayout(new BoxLayout(belowPanel, BoxLayout.Y_AXIS));
		belowPanel.setEditable(false);
		label = new JLabel("CHECK OUT NEW UPDATES:", JLabel.LEFT);
		label.setFont(new Font("Times News Romans", Font.ROMAN_BASELINE, 40));
		label.setAlignmentX(Component.LEFT_ALIGNMENT);
		belowPanel.add(label);

		JScrollPane scrollPane = new JScrollPane(belowPanel);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		contentPane.add(scrollPane, BorderLayout.CENTER);

		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		icon = new ImageIcon(getClass().getResource("/resources/MainMenuIcons/Icon5.jpg"));
		addIconToPanel(leftPanel, icon);
		icon = new ImageIcon(getClass().getResource("/resources/MainMenuIcons/Icon6.jpg"));
		addIconToPanel(leftPanel, icon);
		icon = new ImageIcon(getClass().getResource("/resources/MainMenuIcons/Icon7.jpg"));
		addIconToPanel(leftPanel, icon);
		icon = new ImageIcon(getClass().getResource("/resources/MainMenuIcons/Icon8.jpg"));
		addIconToPanel(leftPanel, icon);
		icon = new ImageIcon(getClass().getResource("/resources/MainMenuIcons/Icon9.jpg"));
		addIconToPanel(leftPanel, icon);
		icon = new ImageIcon(getClass().getResource("/resources/MainMenuIcons/Icon10.jpg"));
		addIconToPanel(leftPanel, icon);
		leftPanel.setBackground(newColor);
		contentPane.add(leftPanel, BorderLayout.LINE_START);

		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		icon = new ImageIcon(getClass().getResource("/resources/MainMenuIcons/Icon11.jpg"));
		addIconToPanel(rightPanel, icon);
		icon = new ImageIcon(getClass().getResource("/resources/MainMenuIcons/Icon12.jpg"));
		addIconToPanel(rightPanel, icon);
		icon = new ImageIcon(getClass().getResource("/resources/MainMenuIcons/Icon13.jpg"));
		addIconToPanel(rightPanel, icon);
		icon = new ImageIcon(getClass().getResource("/resources/MainMenuIcons/Icon14.jpg"));
		addIconToPanel(rightPanel, icon);
		icon = new ImageIcon(getClass().getResource("/resources/MainMenuIcons/Icon15.jpg"));
		addIconToPanel(rightPanel, icon);
		icon = new ImageIcon(getClass().getResource("/resources/MainMenuIcons/Icon16.jpg"));
		addIconToPanel(rightPanel, icon);
		rightPanel.setBackground(newColor);
		contentPane.add(rightPanel, BorderLayout.LINE_END);

		JPanel blankPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		blankPanel.setBackground(newColor);
		updateButton = new JButton("Update");
		updateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				checkForUpdates();
				frame.setVisible(true);
			}
		});
		blankPanel.add(updateButton);
		contentPane.add(blankPanel, BorderLayout.SOUTH);

		checkForUpdates();

		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener( new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dbms.close();
				System.exit(0);
			}
		});
		frame.setVisible(true);
	}

	public static synchronized void announceNewRelease(Manga aManga) {
		belowPanel.add(new JLabel("Check out the latest release of " + aManga.getTitle() + "!" ));
		JLabel hyperlink = new JLabel(aManga.getLatestTitle());
		hyperlink.setForeground(Color.BLUE.darker());
		hyperlink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		hyperlink.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					Desktop.getDesktop().browse(new URI(aManga.getLatestUrl()));     
				} catch (IOException | URISyntaxException e1) {
					e1.printStackTrace();
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				hyperlink.setText("<html><a href=''>" + aManga.getLatestTitle() +"</a></html>");
				// the mouse has entered the label
			}

			@Override
			public void mouseExited(MouseEvent e) {
				hyperlink.setText(aManga.getLatestTitle());
				// the mouse has exited the label
			}
		});
		belowPanel.add(hyperlink);
	}

	public void addIconToPanel(Container panel, ImageIcon icon) {
		Image resizedIcon = icon.getImage();
		resizedIcon = resizedIcon.getScaledInstance(95, 95, java.awt.Image.SCALE_SMOOTH);
		icon.setImage(resizedIcon);
		JLabel label = new JLabel(icon);
		panel.add(label);
	}

	public void checkForUpdates() {
		for (Manga aManga: mangaLst) { 
			Thread t = new Thread() {
				public void run() {
					boolean permit = true;
					try {
						semaphore.acquire();
					} catch (Exception e) {
						e.printStackTrace();
						permit = false;
					}
					if (permit) {
						if (Scrapper.fetchManga(aManga)) {
							announceNewRelease(aManga);
							newReleaseExists = true;
							dbms.updateManga(aManga);
						}
						semaphore.release();
					}
				}
			};
			t.setDaemon(true);
			t.start();
		}
		
		while (semaphore.availablePermits() != MAXIMUM_NUMBER_OF_PERMIT);

		if (!newReleaseExists) {
			belowPanel.add(new JLabel("You are all up-to-date!"));
		}
	}
}
