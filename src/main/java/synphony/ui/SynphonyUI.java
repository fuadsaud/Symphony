package synphony.ui;

import java.awt.Component;
import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class SynphonyUI {

	private final MainFrame mainFrame;

	public SynphonyUI() {
		mainFrame = new MainFrame();
	}

	public MainFrame getMainFrame() {
		return mainFrame;
	}

	public JDialog getPreferecesDialog(Frame owner) {
		return new PreferencesDialog(owner);
	}

	public JDialog getAboutDialog(Frame owner) {
		return new AboutDialog(owner);
	}

	public void showErrorMessage(Component owner, Object message) {
		JOptionPane.showMessageDialog(owner, message, "Error", JOptionPane.ERROR_MESSAGE);
	}

	public void showInformationMessage(Component owner, String message) {
		JOptionPane.showMessageDialog(owner, message, null, JOptionPane.INFORMATION_MESSAGE);
	}
}
