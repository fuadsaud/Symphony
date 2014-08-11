package synphony.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

import log.SOLogger;

public class Synchronizer {

	private static final Logger LOGGER = Logger
			.getLogger(Synchronizer.class.getPackage().getName());

	private Path source;

	private Path target;

	public Synchronizer(Path source, Path target) {
		setSource(source);
		setTarget(target);
	}

	public Path getSource() {
		return source;
	}

	public Path getTarget() {
		return target;
	}

	public void setSource(Path source) {
		if (Files.notExists(source)) {
			throw new IllegalArgumentException("source must exists");
		}

		if (!Files.isDirectory(source)) {
			throw new IllegalArgumentException("source must be a directory");
		}

		this.source = source.toAbsolutePath();
	}

	public void setTarget(Path target) {
		if (Files.exists(target) && !Files.isDirectory(target)) {
			throw new IllegalArgumentException("target must be a directory");
		}

		this.target = target.toAbsolutePath();
	}

	public void sync() throws IOException {
		long millis = System.currentTimeMillis();

		LOGGER.info("Syncing... (FROM: " + source.toAbsolutePath() + " TO: "
				+ target.toAbsolutePath() + ")");

		SOLogger.log("Syncing... (FROM: " + source.toAbsolutePath() + " TO: "
				+ target.toAbsolutePath() + ")");

		Files.walkFileTree(source, new CopyFileVisitor<Path>(source, target));

		LOGGER.info("Sync finished (" + (System.currentTimeMillis() - millis) + " milliseconds)");
		SOLogger.info("Sync finished (" + (System.currentTimeMillis() - millis) + " milliseconds)");
	}
}
