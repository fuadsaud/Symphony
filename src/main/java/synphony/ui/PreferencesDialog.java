package synphony.ui;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;
import synphony.Synphony;

public class PreferencesDialog extends JDialog {

	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 7606012814007479876L;

	private JRadioButton keepNewer;

	private JRadioButton keepOriginal;

	private JRadioButton replaceAlways;

	public PreferencesDialog(Frame owner) {
		super(owner, "Synphony Preferences");

		initReplacePolitic();
		initButtons();

		setSize(400, 200);
		setResizable(true);
		setLocationRelativeTo(owner);
	}

	private void initButtons() {
		JPanel panel = new JPanel(new MigLayout());

		JButton ok = new JButton("OK");
		JButton cancel = new JButton("Cancel");

		ok.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ok();

			}
		});
		cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();

			}
		});

		panel.add(ok, "newline push, skip 3, split, align right");
		panel.add(cancel);

		add(panel, BorderLayout.SOUTH);
	}

	private void initReplacePolitic() {
		JPanel panel = new JPanel(new MigLayout(new LC().wrapAfter(1)));

		JLabel replacePolitic = new JLabel("Replace politic:");

		keepNewer = new JRadioButton("Keep Newer");
		keepOriginal = new JRadioButton("Keep Original");
		replaceAlways = new JRadioButton("Replace Always");

		keepNewer.setName("newer");
		keepOriginal.setName("original");
		replaceAlways.setName("always");

		ButtonGroup group = new ButtonGroup();

		group.add(keepNewer);
		group.add(keepOriginal);
		group.add(replaceAlways);

		String keep = Synphony.PREFERENCES.getProperty("synphony.synchronizer.keep");
		if (keep.equals("incoming"))
			replaceAlways.setSelected(true);
		else if (keep.equals("original"))
			keepOriginal.setSelected(true);
		else
			keepNewer.setSelected(true);

		panel.add(replacePolitic);
		panel.add(keepNewer);
		panel.add(keepOriginal);
		panel.add(replaceAlways);

		add(panel);
	}

	private void ok() {
		updateProperties();
		dispose();
	}

	private void updateProperties() {
		String keep = null;

		if (keepNewer.isSelected())
			keep = "newer";
		else if (keepOriginal.isSelected())
			keep = "original";
		else if (replaceAlways.isSelected())
			keep = "incoming";

		Synphony.PREFERENCES.setProperty("synphony.synchronizer.keep", keep);
	}
}
