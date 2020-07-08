package solver;

import java.io.PrintWriter;
import java.util.Scanner;

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
        final var cells = scanner.tokens()
                .limit(rows * cols)
                .map(ComplexNumber::new)
                .toArray(ComplexNumber[]::new);

        final var equation = new LinearEquation(rows, cols, cells);
        writer.print(equation.getSolution());
    }
}
