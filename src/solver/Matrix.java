package solver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.DoubleStream;

import static java.util.stream.IntStream.range;

public class Matrix {
    double[][] matrix;
    private int numberOfVariables;
    private int numberOfEquations;

    void read(File file) throws FileNotFoundException {
        final var scanner = new Scanner(file);
        numberOfVariables = scanner.nextInt();
        numberOfEquations = scanner.nextInt();

        matrix = new double[numberOfEquations][];

        for (int row = 0; row < numberOfEquations; ++row) {
            matrix[row] = DoubleStream
                    .generate(scanner::nextDouble)
                    .limit(numberOfVariables + 1)
                    .toArray();
        }
        scanner.close();
    }

    double getVariable(int i) {
        return matrix[i][numberOfVariables];
    }

    void write(File file) {
        try (final var out = new PrintWriter(file)) {
            range(0, numberOfVariables)
                    .forEach(i -> out.println(matrix[i][numberOfVariables]));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void print() {
        range(0, numberOfVariables)
                .mapToObj(row -> Arrays.toString(matrix[row]))
                .forEach(System.out::println);
        System.out.println();
    }

    void solve() {
        range(0, numberOfEquations).forEach(this::stage1);

        for (int row = numberOfVariables - 1; row > 0; --row) {
            stage2(row);
        }
    }

    void stage1(int row) {
        if (matrix[row][row] == 0) {

        }

        range(row + 1, numberOfVariables + 1).forEach(col -> matrix[row][col] /= matrix[row][row]);
        matrix[row][row] = 1;

        range(row + 1, numberOfVariables).forEach(i -> {
            double k = matrix[i][row];
            range(row, numberOfVariables + 1).forEach(col -> matrix[i][col] -= k * matrix[row][col]);
        });

        print();
    }

    void stage2(int row) {
        for (int i = row; i-- > 0; ) {
            matrix[i][numberOfVariables] -= matrix[i][row] * matrix[row][numberOfVariables];
            matrix[i][row] = 0;
        }
        print();
    }
}