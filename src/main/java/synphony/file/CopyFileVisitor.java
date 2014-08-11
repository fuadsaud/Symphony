package synphony.file;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.logging.Logger;

import javax.swing.ProgressMonitor;

import log.SOLogger;
import synphony.Synphony;

public class CopyFileVisitor<T extends Path> extends SimpleFileVisitor<T> {

	private static final Logger LOGGER = Logger.getLogger(CopyFileVisitor.class.getPackage()
			.getName());

	private Path source;

	private Path target;

	private long totalSize;

	private long currentSize;

	public CopyFileVisitor(Path source, Path target) throws IOException {
		this.source = source;
		this.target = target;

		this.totalSize = Files.size(source);
		this.currentSize = 0;

		LOGGER.setUseParentHandlers(true);

		LOGGER.info("Source dir: " + source + " Target dir: " + target + " Size: " + totalSize);

		SOLogger.info("Source dir: " + source + " Target dir: " + target);
	}

	@Override
	public FileVisitResult preVisitDirectory(T dir, BasicFileAttributes attrs) throws IOException,
			SecurityException {
		Path relative = source.relativize(dir);

		SOLogger.debug("Incoming dir: " + relative);

		Files.createDirectories(target.resolve(relative));

		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException,
			SecurityException {

		Path entry = source.relativize(file);
		Path dest = target.resolve(entry);

		if (Files.notExists(dest)
				|| Files.getLastModifiedTime(file).compareTo(Files.getLastModifiedTime(dest)) > 0) {
			LOGGER.info("Copying entry: " + entry);
			SOLogger.info("Copying entry: " + entry);

			Files.copy(file, dest, StandardCopyOption.COPY_ATTRIBUTES,
					StandardCopyOption.REPLACE_EXISTING);
		} else {
			LOGGER.info("Entry skipped: " + entry);
			SOLogger.info("Entry skipped: " + entry);
		}

		ProgressMonitor monitor = Synphony.getUI().getMainFrame().getProgressMonitor();

		currentSize += Files.size(file) / ((float) totalSize / 100);

		System.out.println(currentSize);
		monitor.setProgress((int) currentSize);
		monitor.setNote(entry.toString());

		return FileVisitResult.CONTINUE;
	}
}
