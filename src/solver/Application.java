package solver;

import java.io.PrintWriter;
import java.util.Scanner;
import java.util.stream.DoubleStream;

public class Application implements Runnable {
    private final Scanner scanner;
    private final PrintWriter writer;

    public Application(Scanner scanner, PrintWriter writer) {
        this.scanner = scanner;
        this.writer = writer;
    }

    public void run() {
        final int cols = scanner.nextInt() + 1;
        final int rows = scanner.nextInt();
        final var cells = DoubleStream.generate(scanner::nextDouble).limit(rows * cols).toArray();

        final var matrix = new LinearEquation(rows, cols, cells);
        System.out.println(matrix);

        matrix.solve();
        matrix.write(writer);
    }
}
