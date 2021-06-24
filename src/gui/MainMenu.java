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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingWorker;
import javax.swing.UIManager;

import database.DBConnector;
import exceptions.ConnectionException;
import manga.Manga;
import scrapper.Scrapper;

public class MainMenu {

	private JFrame frame;
	private Container contentPane;
	private JMenuBar menuBar;
	private JMenu menu;
	private JPanel upperPanel;
	private JPanel belowPanel;
	private JButton updateButton;
	
	private WaitingFrame wf;
	private ListMenu listMenu;
	
	private List<Manga> mangaLst;
	private DBConnector dbms;
	
	private Color newColor = new Color(135,206,250);
	private boolean newReleaseExists = false;
	private static final int MAXIMUM_NUMBER_OF_PERMIT = 10;
	private Semaphore semaphore = new Semaphore(MAXIMUM_NUMBER_OF_PERMIT);

	public MainMenu() {
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
		belowPanel = new JPanel();

		menuBar.add(menu);
		frame.setJMenuBar(menuBar);


		frame.setTitle("SirMangaNox");
		frame.setSize(1200, 800);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
	}

	public MainMenu(DBConnector dbms, List<Manga> mangaLst) {	
		this();
		Thread t = new Thread() {
			@Override
			public void run() {
				listMenu = new ListMenu(dbms, mangaLst);
				wf = new WaitingFrame();
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
		label = new JLabel("CHECK OUT NEW UPDATES:", JLabel.LEFT);
		label.setFont(new Font("Times News Romans", Font.ROMAN_BASELINE, 40));
		label.setAlignmentX(Component.LEFT_ALIGNMENT);
		belowPanel.add(label);

		belowPanel.setBackground(Color.WHITE);
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
				SwingWorker sw = new SwingWorker() {
					@Override
					protected String doInBackground() throws Exception {
						frame.setVisible(false);
						wf.setVisible(true);
						for (int i = 1; i < belowPanel.getComponentCount(); i++) {
							belowPanel.remove(1);
						}
						checkForUpdates();
						return "Finish";
					}
					
					@Override
					protected void done() {
						wf.setVisible(false);
						JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
						scrollBar.setValue(scrollBar.getMaximum());
						frame.setVisible(true);
					}
				};
				sw.execute();
			}
		});
		blankPanel.add(updateButton);
		contentPane.add(blankPanel, BorderLayout.SOUTH);

		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener( new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dbms.close();
				System.exit(0);
			}
		});
		
		SwingWorker sw = new SwingWorker() {
			@Override
			protected String doInBackground() throws Exception {
				wf.setVisible(true);
				checkForUpdates();
				return "Finish";
			}
			
			@Override
			protected void done() {
				wf.setVisible(false);
				frame.setVisible(true);
			}
		};
		sw.execute();
	}

	public synchronized void announceNewRelease(Manga aManga) {
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
		newReleaseExists = false;
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
						try {
							if (Scrapper.fetchManga(aManga)) {
								announceNewRelease(aManga);
								newReleaseExists = true;
								dbms.updateManga(aManga);
							}
						} catch (ConnectionException e) {
							belowPanel.add(new JLabel(e.getMessage()));
						} finally {
							semaphore.release();
						}
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
