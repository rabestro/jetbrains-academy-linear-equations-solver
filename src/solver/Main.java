package solver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        try (
                final var input = new Scanner(new File(args[1]));
                final var output = new PrintWriter(new File(args[3]))
        ) {
            new Application(input, output).run();
        }
    }
}

