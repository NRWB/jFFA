import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.stream.Stream;

public class FFA {
	
	/**
	 * FFA = Find Files App
	 * 
	 * A simple java application that lists out contents on a file system.
	 * 
	 * Uses minimal logic and provides several minimum features (as seen
	 * in the main method args).
	 * 
	 * Also, the program uses Java features such as 1.7 nio and 1.8 stream.
	 * 
	 * Example Usage:
	 * 
	 * java FFA "path/to/target/" -1 false "path/to/log/file/output.txt"
	 * 
	 * @param args
	 * args[0] = start directory string path
	 * args[1] = max depth integer to search. -1 / ... aka n < 0 = infinite, 0 = none, 1 = dive one folder down, etc.
	 * args[2] = default search boolean (true by default)
	 * args[3] = output string path to log file
	 * 
	 * @throws IOException Usage of Files class and respective methods
	 */
	public static void main(String[] args) throws IOException {

		final String startDir = args[0];
		final Path startPath = Paths.get(startDir);

		int maxDepth = Integer.parseInt(args[1]);
		if (maxDepth < 0)
			maxDepth = Integer.MAX_VALUE;

		final boolean defSearch = Boolean.parseBoolean(args[2]);

		Stream<Path> result = null;

		if (defSearch) {
			result = Files.find(startPath, maxDepth, (p, bfa) -> bfa.isRegularFile());
		} else {
			result = Files.find(startPath, maxDepth,
					(p, bfa) -> bfa.isRegularFile() || bfa.isDirectory() || bfa.isSymbolicLink(),
					FileVisitOption.FOLLOW_LINKS);
		}

		final String outputLog = args[3];
		final Path toPath = Paths.get(outputLog);

		if (!Files.exists(toPath))
			Files.createFile(toPath);
		result.forEachOrdered((p) -> {
			try {
				final String line = p.toString() + System.lineSeparator();
				Files.write(toPath, line.getBytes(), StandardOpenOption.APPEND);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}
}
