package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

public class WaitingFrame extends JFrame {

	public WaitingFrame() {
		super();
		
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Container cp = this.getContentPane();
		
		ImageIcon gif = new ImageIcon(getClass().getResource("/resources/WaitingGIF/waiting.gif"));
		cp.add(new JLabel(gif), BorderLayout.CENTER);
		JLabel label = new JLabel("Please wait while we checking for new updates");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("Times News Romans", Font.ITALIC, 18));
		cp.add(label, BorderLayout.SOUTH);
		cp.setBackground(Color.WHITE);
		
		this.setTitle("Loading...");
		this.setSize(450, 300);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
	}

}
