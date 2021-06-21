import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

public class MainMenu {
	
	private static final JFrame frame = new JFrame();
	private static final Container contentPane = frame.getContentPane();
	private static final JMenuBar menuBar = new JMenuBar();
	private static final JMenu menu = new JMenu("List");
	private static final JPanel upperPanel = new JPanel();
	private static final JEditorPane belowPanel = new JEditorPane();
	private static boolean newReleaseExists = false;
	private static List<Thread> threads = new ArrayList<Thread>();
	
	static {
		menuBar.add(menu);
		frame.setJMenuBar(menuBar);
	
		JLabel label;
		URL iconUrl = null;

		try {
			iconUrl = new URL("https://i.pinimg.com/originals/b7/1a/61/b71a61a81142af274b1e03c053df0bdf.jpg");
		} catch (Exception e) {
			System.out.println("Fail to get the image");
		}
		ImageIcon icon = new ImageIcon(iconUrl);
		Image resizedIcon = icon.getImage();
		resizedIcon = resizedIcon.getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(resizedIcon);
		label = new JLabel(icon);
		upperPanel.add(label);
		
		label = new JLabel("WELCOME!", JLabel.CENTER);
		label.setFont(new Font("Times News Romans", Font.ROMAN_BASELINE, 60));
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		upperPanel.add(label);
		
		try {
			iconUrl = new URL("https://pbs.twimg.com/profile_images/1082020318523412480/E87sUSUc_400x400.jpg");
		} catch (Exception e) {
			System.out.println("Fail to get the image");
		}
		icon = new ImageIcon(iconUrl);
		resizedIcon = icon.getImage();
		resizedIcon = resizedIcon.getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(resizedIcon);
		label = new JLabel(icon);
		upperPanel.add(label);
		contentPane.add(upperPanel, BorderLayout.PAGE_START);
		
		belowPanel.setLayout(new BoxLayout(belowPanel, BoxLayout.Y_AXIS));
		belowPanel.setEditable(false);
		label = new JLabel("CHECK OUT NEW UPDATES:", JLabel.LEFT);
		label.setFont(new Font("Times News Romans", Font.ROMAN_BASELINE, 40));
		label.setAlignmentX(Component.LEFT_ALIGNMENT);
		belowPanel.add(label);
		
		JScrollPane scrollPane = new JScrollPane(belowPanel);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		frame.setTitle("SirMangaNox");
		frame.setSize(1200, 800);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
	}
	
	public MainMenu(DBConnector dbms, List<Manga> mangaLst) {	
		for (Manga aManga: mangaLst) { 
			Thread th = new Thread() {
				@Override
				public void run() {
					if (Scrapper.fetchManga(aManga)) {
						announceNewRelease(aManga);
						newReleaseExists = true;
						dbms.updateManga(aManga);
					}
				}
			};
			threads.add(th);
			th.setDaemon(true);
			th.start();
		}
		
		for (Thread thread: threads) {
			try {
				thread.join();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (!newReleaseExists) {
			belowPanel.add(new JLabel("You are all up-to-date!"));
		}
		
		show();
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
	
	public static void show() {
		frame.setVisible(true);
	}
	
	public boolean isVisible() {
		return frame.isVisible();
	}
}
