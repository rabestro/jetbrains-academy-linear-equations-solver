package solver;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.logging.*;

import static java.util.stream.IntStream.*;

public final class LinearEquation {
    private static final Logger log = Logger.getLogger(LinearEquation.class.getName());

    private final int rows;
    private final int cols;
    private final double[] cells;

    public LinearEquation(final int equations, final int cols, final double[] cells) {
        this.rows = equations;
        this.cols = cols;
        this.cells = cells;
        log.config(this::toString);
    }

    private double get(final int row, final int col) {
        return cells[row * cols + col];
    }

    public String getSolution() {
        final var mainDiagonal = iterate(0, i -> i < rows * cols, i -> i + cols + 1);

        mainDiagonal
                .takeWhile(this::findNonZero)
                .forEach(this::stageOne);

        if (isNoSolution()) {
            return "No solutions";
        }
        if (isManySolutions()) {
            return "Infinitely many solutions";
        }

        for (int row = cols - 2; row > 0; --row) {
            stageTwo(row);
        }
        log.info(Arrays.toString(getVariables()));

        return range(0, cols - 1)
                .mapToObj(i -> cells[i * cols + cols - 1] + "\n")
                .collect(Collectors.joining());
    }

    private boolean isNoSolution() {
        final var nonZeroConstant = iterate(cells.length - 1, i -> i > 0, i -> i - cols)
                .filter(i -> cells[i] != 0)
                .findFirst();
        return nonZeroConstant.isPresent()
                && range(nonZeroConstant.getAsInt() - cols + 1, nonZeroConstant.getAsInt())
                .allMatch(i -> cells[i] == 0);
    }

    private boolean isManySolutions() {
        final var significantEquations = range(0, rows)
                .filter(row -> range(0, cols - 1)
                        .anyMatch(i -> cells[row * cols + i] != 0))
                .count();
        final var significantVariables = cols - 1;
        log.fine("Significant Equations: " + significantEquations);
        log.fine("Significant Variables: " + significantVariables);

        return significantEquations < significantVariables;
    }

    private void stageOne(int index) {
        log.finest("Stage One for index " + index);

        range(index + 1, index / cols * cols + cols)
                .forEach(i -> cells[i] /= cells[index]);
        cells[index] = 1;
        log.finest(this::toString);

        iterate(index + cols, i -> i < cells.length, i -> i + cols).forEach(i -> {
            range(i + 1, i / cols * cols + cols)
                    .forEach(j -> cells[j] -= cells[i] * cells[index / cols * cols + j % cols]);
            cells[i] = 0;
        });
        log.finest(this::toString);
    }

    private boolean findNonZero(int index) {
        final var findInRows = iterate(index, i -> i < cells.length, i -> i + cols);

        final var findInCols = range(index + 1, cells.length)
                .filter(i -> i % cols > index / cols && i % cols < cols - 1);

        final var nonZeroCell = concat(findInRows, findInCols)
                .filter(i -> cells[i] != 0)
                .findFirst();

        if (nonZeroCell.isPresent()) {
            swap(index, nonZeroCell.getAsInt());
            return true;
        }
        return false;
    }

    private void swap(int i, int nonZeroCell) {
        if (i / cols != nonZeroCell / cols) {
            swapRows(i / cols, nonZeroCell / cols);
        }
        if (i % cols != nonZeroCell % cols) {
            swapCols(i % cols, nonZeroCell % cols);
        }
    }

    private void swapCols(int col1, int col2) {
        log.info("Swapping cols...");
        range(0, rows).forEach(row -> {
            final var tmp = get(row, col1);
            cells[row * cols + col1] = get(row, col2);
            cells[row * cols + col2] = tmp;
        });

    }

    private void swapRows(int row1, int row2) {
        log.info("Swapping rows...");
        range(0, cols).forEach(col -> {
            final var tmp = get(row1, col);
            cells[row1 * cols + col] = get(row2, col);
            cells[row2 * cols + col] = tmp;
        });
    }

    private void stageTwo(int row) {
        for (int i = row; i-- > 0; ) {
            cells[i * cols + cols - 1] -= cells[i * cols + row] * cells[row * cols + cols - 1];
            cells[i * cols + row] = 0;
        }
        log.fine(this::toString);
    }

    public double[] getVariables() {
        return range(0, cols - 1).mapToDouble(i -> cells[i * cols + cols - 1]).toArray();
    }

    @Override
    public String toString() {
        return "\n" + range(0, cells.length)
                .mapToObj(i -> String.format("%8.2f%s", cells[i], (i + 1) % cols == 0 ? "\n" : " "))
                .collect(Collectors.joining());
    }

}
