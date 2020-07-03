package solver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.logging.*;

public class Main {
    private static final Logger log = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        try (final var input = new Scanner(new File(args[1]));
             final var output = new PrintWriter(new File(args[3]))) {
            new Application(input, output).run();
        } catch (FileNotFoundException e) {
            log.log(Level.SEVERE, "File not found", e);
        }
    }
}

