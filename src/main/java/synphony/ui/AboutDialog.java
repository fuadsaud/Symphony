package synphony.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JDialog;
import javax.swing.JLabel;

import net.miginfocom.swing.MigLayout;
import synphony.Synphony;

public class AboutDialog extends JDialog {

	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = -2144768064095158212L;

	public AboutDialog(Frame owner) {
		super(owner, "About Synphony", true);

		setLayout(new MigLayout("wrap 1", "[center][right][left][c]", "[top][center][b]"));

		initLabels();

		pack();

		Dimension d = getSize();
		setSize(d.width - 20, d.height);
		setResizable(false);
		setLocationRelativeTo(owner);
	}

	private void browserCall() {
		try {
			Desktop.getDesktop().browse(new URI(Synphony.HOME_PAGE));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initLabels() {
		// add(new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().getImage(
		// this.getClass().getResource("/resources/timer-icon256.png")))),
		// "align center");

		JLabel synphony = new JLabel("Synphony");
		synphony.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
		add(synphony, "align center");

		JLabel version = new JLabel(Synphony.VERSION);
		version.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
		add(version, "align center");

		add(new JLabel("Copyleft (c) 2012, Fuad Saud. Couple of rights reserved :)"),
				"align center");

		JLabel url = new JLabel(Synphony.HOME_PAGE);
		url.setForeground(Color.BLUE);
		url.setCursor(new Cursor(Cursor.HAND_CURSOR));
		url.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				browserCall();
			}

		});

		add(url, "align center");
	}
}