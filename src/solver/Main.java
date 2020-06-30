package solver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        new Application(
                new Scanner(new File(args[1])),
                new PrintWriter(new File(args[3]))
        ).run();
    }
}

