package hexlet.code;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.IOException;

@Command(name = "gendiff", mixinStandardHelpOptions = true, version = "gendiff 1.0",
         description = "Compares two configuration files and shows a difference.")
public class App implements Runnable {

    @Option(names = {"-f", "--format"}, paramLabel = "format", description = "output format",
            defaultValue = "stylish", showDefaultValue = CommandLine.Help.Visibility.ALWAYS)
    private String format;

    @Parameters(index = "0", paramLabel = "filepath1", description = "path to the first file")
    private String filepath1;

    @Parameters(index = "1", paramLabel = "filepath2", description = "path to the second file")
    private String filepath2;

    @Override
    public void run() {
        try {
            String diff = Differ.generate(filepath1, filepath2);
            System.out.println(diff);
        } catch (IOException e) {
            System.out.println("Error when generating difference: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new App()).execute(args);
        System.exit(exitCode);
    }
}
