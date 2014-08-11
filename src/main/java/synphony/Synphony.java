package synphony;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.XMLFormatter;

import javax.swing.UIManager;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import log.InlineFormatter;
import log.SOLogger;
import synphony.ui.SynphonyUI;

public class Synphony {

	public static final String VERSION = "0.0.1";

	public static final String HOME_PAGE = "http://twitter.com/fuadsaud";

	private static final Logger LOGGER;

	private static final Properties DEFAULT_PROPERTIES;

	public static final Properties PREFERENCES;

	// Paths
	public static final String USER_NAME = System.getProperty("user.name");

	public static final Path SETTINGS_DIR = Paths.get("settings/" + USER_NAME);

	public static final Path PREFERENCES_FILE = SETTINGS_DIR.resolve("synphony.properties");

	public static final Path LOG_DIR = Paths.get("log");

	public static final Path LOG_FILE = LOG_DIR.resolve("synphony.log");

	private static SynphonyUI ui;

	static {
		createDirectories();

		DEFAULT_PROPERTIES = new Properties();
		DEFAULT_PROPERTIES.setProperty("synphony.synchronizer.keep", "newer");
		DEFAULT_PROPERTIES.setProperty("synphony.ui.laf", NimbusLookAndFeel.class.getName());

		PREFERENCES = new Properties(DEFAULT_PROPERTIES);
		try {
			PREFERENCES.load(new FileReader(PREFERENCES_FILE.toFile()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Initializes logger

		LOGGER = setUpLogging();

		String headLog = new StringBuilder()
				.append("--- ")
				.append(new Date())
				.append(" - ")
				.append("Synphony " + VERSION)
				.append(" - ")
				.append("JRE " + System.getProperty("java.version"))
				.append(" - ")
				.append(System.getProperty("os.name") + " " + System.getProperty("os.version")
						+ " " + System.getProperty("os.arch")).append(" - ")
				.append(System.getProperty("user.name")).toString();

		LOGGER.config(headLog);

		SOLogger.log(headLog);
	}

	public static void exit() {
		createDirectories();
		try {
			PREFERENCES.store(new FileWriter(PREFERENCES_FILE.toFile()),
					"Synphony general settings");
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.exit(0);
	}

	public static SynphonyUI getUI() {
		return ui;
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(PREFERENCES.getProperty("synphony.ui.laf"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		ui = new SynphonyUI();

		getUI().getMainFrame().setVisible(true);
	}

	private static void createDirectories() {
		try {
			Files.createDirectories(SETTINGS_DIR);
			Files.createDirectories(LOG_DIR);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static Logger setUpLogging() {
		Logger logger = null;
		try {
			SOLogger.setLoggingActivated(false);

			Files.createDirectories(Paths.get("log"));
			Handler handler = new FileHandler("log/synphony.log", true);
			handler.setFormatter(new InlineFormatter());
			handler.setLevel(Level.ALL);

			FileHandler xml = new FileHandler("log/synphony.xml", false);
			xml.setFormatter(new XMLFormatter());
			xml.setLevel(Level.INFO);
			xml.setEncoding("UTF-8");

			logger = Logger.getLogger(Synphony.class.getPackage().getName());

			logger.addHandler(handler);
			logger.addHandler(xml);
			logger.setLevel(Level.ALL);
			logger.setLevel(Level.ALL);

			Logger def = Logger.getLogger("");
			def.getHandlers()[0].setFormatter(new InlineFormatter());
		} catch (Exception e) {
			logger.warning(e.getMessage());
			e.printStackTrace();
		}

		try {
			SOLogger.addLogHandler(new PrintStream(new FileOutputStream(new File(
					"log/synphony-old.log"), true)));
			SOLogger.setLoggingActivated(true);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return logger;
	}
}
