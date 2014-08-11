package synphony.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.UIManager;

import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;

public class ProgressDialog extends JDialog {

	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 2893673053109032815L;
	private JProgressBar progressBar;

	public ProgressDialog(Frame owner) {
		super(owner, "Syncing...");

		setLayout(new MigLayout(new LC().fill()));

		initComponents();

		setSize(475, 220);
		setResizable(true);
		setMinimumSize(new Dimension(475, 220));
		setMaximumSize(new Dimension(500, 350));
	}

	private void initComponents() {
		initLabels();
		initProgressBar();
	}

	private void initProgressBar() {
		JPanel panel = new JPanel(new BorderLayout());

		progressBar = new JProgressBar(0, 100);
		progressBar.setStringPainted(true);

		progressBar.setValue(70);
		progressBar.setString("dabr/common/browser/theme.php");
		progressBar.setFont(progressBar.getFont().deriveFont(20f));

		panel.add(progressBar);

		add(panel, "grow");
	}

	private void initLabels() {
		JPanel panel = new JPanel(new MigLayout(new LC().gridGapY("20")));

		JLabel title = new JLabel("Syncing...");
		title.setFont(title.getFont().deriveFont(18f));

		JLabel from = new JLabel("From: D:\\zsyncsource");
		from.setFont(from.getFont().deriveFont(16f));

		JLabel to = new JLabel("To: D:\\zsynctarget");
		to.setFont(to.getFont().deriveFont(16f));

		panel.add(title, "wrap, spanx, alignx center");
		panel.add(from, "wrap");
		panel.add(to);

		add(panel, "wrap, grow, align center");
	}

	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		ProgressDialog dialog = new ProgressDialog(null);
		dialog.setVisible(true);

		while (true) {
			Thread.sleep(1000);
			dialog.setValue((int) (System.currentTimeMillis() % 100));
			dialog.setCurrentEntry("dabr/browser/common" + (short) System.currentTimeMillis()
					+ ".php");
		}

	}

	public void setValue(int value) {
		progressBar.setValue(value);
	}

	public void setCurrentEntry(String entry) {
		progressBar.setString(entry);
	}
}
