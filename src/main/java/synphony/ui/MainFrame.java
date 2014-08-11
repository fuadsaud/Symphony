package synphony.ui;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextField;
import javax.swing.ProgressMonitor;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;

import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;
import synphony.Synphony;
import synphony.file.Synchronizer;

public class MainFrame extends JFrame {

	/**
	 * Serial version ID.
	 */
	private static final long serialVersionUID = 1078667199320875764L;

	private static final byte SOURCE = 0;

	private static final byte TARGET = 1;

	private ProgressMonitor progressMonitor;

	private JFileChooser fileChooser;

	private JPanel controls;

	private JTextField sourceTextField;

	private JTextField targetTextField;

	public MainFrame() {
		super("Synphony by @fuadsaud");

		initComponents();
		initProgressMonitor();

		setSize(530, 210);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setAlwaysOnTop(Boolean.parseBoolean(Synphony.PREFERENCES
				.getProperty("synphony.ui.always.on.top")));

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				exit();
			}
		});
	}

	public ProgressMonitor getProgressMonitor() {
		return progressMonitor;
	}

	private void about() {
		Synphony.getUI().getAboutDialog(this).setVisible(true);
	}

	private void browse(byte what) {
		int result = fileChooser.showOpenDialog(this);

		if (result == JFileChooser.APPROVE_OPTION) {
			File dir = fileChooser.getSelectedFile().getAbsoluteFile();

			switch (what) {
			case SOURCE:
				sourceTextField.setText(dir.getAbsolutePath());
				break;
			case TARGET:
				targetTextField.setText(dir.getAbsolutePath());
				break;
			}
		}

	}

	private void exit() {
		Synphony.PREFERENCES.setProperty("synphony.source", sourceTextField.getText());
		Synphony.PREFERENCES.setProperty("synphony.target", targetTextField.getText());

		Synphony.exit();
	}

	private void go() {
		String source = sourceTextField.getText();
		String target = targetTextField.getText();

		if (source == null || source.isEmpty() || target == null || target.isEmpty()) {
			return;
		}

		Synchronizer temp = null;
		try {
			temp = new Synchronizer(Paths.get(source), Paths.get(target));
		} catch (IllegalArgumentException e) {
			Synphony.getUI().showErrorMessage(sourceTextField, "Invalid paths");
			e.printStackTrace();
		}

		final Synchronizer syncr = temp;

		new SwingWorker<Object, Object>() {

			@Override
			protected Object doInBackground() throws Exception {
				try {
					setEnabled(false);
					syncr.sync();
					setEnabled(true);
				} catch (SecurityException e) {
					Synphony.getUI().showErrorMessage(Synphony.getUI().getMainFrame(),
							"Not able to access the files\n" + e.getMessage());
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					Synphony.getUI().showErrorMessage(Synphony.getUI().getMainFrame(),
							"An error has occurred");
					e.printStackTrace();
				} catch (IOException e) {
					Synphony.getUI().showErrorMessage(Synphony.getUI().getMainFrame(),
							"An I/O error has occurred");
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void done() {
				syncFinished();
			}
		}.execute();
	}

	private void initComponents() {
		initMenu();
		initControls();
		initGo();
		initFileChooser();
	}

	private void initControls() {
		controls = new JPanel(new MigLayout());

		initPaths();

		add(controls, BorderLayout.CENTER);
	}

	private void initFileChooser() {
		fileChooser = new JFileChooser();

		fileChooser.setName("fileChooser");
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setCurrentDirectory(new File("D:/"));
	}

	private void initGo() {
		JPanel buttons = new JPanel(new MigLayout(new LC().fillX()));

		JButton go = new JButton("Go!");
		go.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				go();

			}
		});

		buttons.add(go, "newline, grow, span 2 2");

		add(buttons, BorderLayout.SOUTH);
	}

	private void initMenu() {
		JMenuBar menuBar = new JMenuBar();

		JMenu synphony = new JMenu("Synphony");

		JMenuItem log = new JMenuItem("Log");
		log.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				MainFrame.this.log();
			}
		});

		JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		JMenu options = new JMenu("Options");

		final JRadioButtonMenuItem alwaysOnTop = new JRadioButtonMenuItem("Always on top");
		alwaysOnTop.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setAlwaysOnTop(alwaysOnTop.isSelected());
				Synphony.PREFERENCES.setProperty("synphony.ui.always.on.top", "true");
			}
		});

		alwaysOnTop.setSelected(Boolean.parseBoolean(Synphony.PREFERENCES
				.getProperty("synphony.ui.always.on.top")));

		JMenu laf = new JMenu("Look and Feel");

		JMenuItem metal = new JRadioButtonMenuItem("Metal");
		metal.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				changeLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
			}
		});

		JMenuItem nimbus = new JRadioButtonMenuItem("Nimbus");
		nimbus.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				changeLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
			}
		});

		JMenuItem system = new JRadioButtonMenuItem("System");
		system.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				changeLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			}
		});

		ButtonGroup group = new ButtonGroup();
		group.add(metal);
		group.add(nimbus);
		group.add(system);

		system.setSelected(true);

		JMenuItem preferences = new JMenuItem("Preferences");
		preferences.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				preferences();
			}

		});

		JMenu help = new JMenu("Help");

		JMenuItem about = new JMenuItem("About Synphony...");
		about.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				about();
			}
		});

		synphony.add(log);
		synphony.addSeparator();
		synphony.add(exit);
		menuBar.add(synphony);

		options.add(alwaysOnTop);
		options.addSeparator();
		laf.add(metal);
		laf.add(nimbus);
		laf.add(system);
		options.add(laf);
		options.addSeparator();
		options.add(preferences);
		menuBar.add(options);

		help.add(about);
		menuBar.add(help);

		setJMenuBar(menuBar);
	}

	private void initPaths() {
		sourceTextField = new JTextField(256);
		sourceTextField.setText(Synphony.PREFERENCES.getProperty("synphony.source"));
		sourceTextField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					browse(SOURCE);
				}
			}
		});
		JButton browseSource = new JButton("Browse...");
		browseSource.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				browse(SOURCE);
			}
		});

		targetTextField = new JTextField(256);
		targetTextField.setText(Synphony.PREFERENCES.getProperty("synphony.target"));
		targetTextField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					browse(TARGET);
				}
			}
		});
		JButton browseTarget = new JButton("Browse...");
		browseTarget.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				browse(TARGET);
			}
		});

		JButton switchButton = new JButton("Switch");
		switchButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				switchFiles();
			}
		});

		controls.add(sourceTextField);
		controls.add(browseSource);
		controls.add(targetTextField, "newline");
		controls.add(browseTarget);
		controls.add(switchButton, "newline, skip, grow");

		add(controls, BorderLayout.CENTER);

	}

	private void initProgressMonitor() {
		progressMonitor = new ProgressMonitor(this, "Syncing...", null, 0, 100);
		getProgressMonitor().setMillisToDecideToPopup(0);
	}

	private void log() {
		try {
			Desktop.getDesktop().open(Synphony.LOG_FILE.toFile());
		} catch (Exception e) {
			Synphony.getUI().showErrorMessage(this, "Unable to open log file");
			e.printStackTrace();
		}
	}

	private void preferences() {
		Synphony.getUI().getPreferecesDialog(this).setVisible(true);
	}

	private void switchFiles() {
		String temp = sourceTextField.getText();
		sourceTextField.setText(targetTextField.getText());
		targetTextField.setText(temp);
	}

	private void syncFinished() {
		int result = JOptionPane.showOptionDialog(this,
				"Sync finished.\nSee log file for detailed information", "Sync finished",
				JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[] {
						"OK", "Log" }, "OK");
		if (result == 1) {
			log();
		}
	}

	private void changeLookAndFeel(String lookAndFeelClassName) {
		try {
			UIManager.setLookAndFeel(lookAndFeelClassName);
			SwingUtilities.updateComponentTreeUI(Synphony.getUI().getMainFrame());
			fileChooser.updateUI();
			Synphony.PREFERENCES.setProperty("synphony.ui.laf", lookAndFeelClassName);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
