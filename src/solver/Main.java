package solver;

import java.io.*;
import java.util.Scanner;
import java.util.logging.*;

public class Main {
    private static final Logger log = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        try {
            LogManager.getLogManager().readConfiguration(
                    new FileInputStream("logging.properties"));
        } catch (IOException e) {
            System.err.println("Could not setup logger configuration: " + e.toString());
        }

        try (final var input = new Scanner(new File(args[1]));
             final var output = new PrintWriter(args[3])) {
            new Application(input, output).run();
        } catch (FileNotFoundException e) {
            log.log(Level.SEVERE, "File not found", e);
        }
    }
}

