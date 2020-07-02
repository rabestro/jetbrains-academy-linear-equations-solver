package solver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.logging.LogManager;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        try {
            LogManager
                    .getLogManager()
                    .readConfiguration(Main.class.getResourceAsStream("logging.properties"));
        } catch (IOException e) {
            System.err.println("Could not setup logger configuration: " + e.toString());
        }

        try (final var input = new Scanner(new File(args[1]));
             final var output = new PrintWriter(new File(args[3]))) {
            new Application(input, output).run();
        }
    }
}

