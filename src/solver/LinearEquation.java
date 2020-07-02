package solver;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.logging.*;

import static java.lang.Math.min;
import static java.util.stream.IntStream.*;

public final class LinearEquation {
    private static final Logger log = Logger.getLogger(LinearEquation.class.getName());
    public static final int NO_SOLUTIONS = 0;
    public static final int ONE_SOLUTION = 1;
    public static final int MANY_SOLUTIONS = 2;


    static {
        try {
            final var fileHandler = new FileHandler("default%u.log");
            log.addHandler(fileHandler);
            fileHandler.setFormatter(new SimpleFormatter());
            log.setLevel(Level.ALL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

//    private void set(final int row, final int col, final double value) {
//        cells[row * cols + col] = value;
//    }

    public int solve() {
        if (rows < cols - 1) {
            return MANY_SOLUTIONS;
        }
        range(0, min(rows, cols - 1)).takeWhile(this::isNonZero).forEach(this::stage1);

        for (int row = cols - 2; row > 0; --row) {
            stage2(row);
        }
        log.info(Arrays.toString(getVariables()));
        return ONE_SOLUTION;
    }

    private boolean isNonZero(int row) {
//        if (cells[row * cols + row] != 0) {
//            return true;
//        }

        final var findInRows = iterate(row * cols + row, i -> i < cells.length, i -> i + cols);

        final var findInCols = range(row * cols + row + 1, cells.length)
                .filter(i -> i % cols > row && i % cols < cols - 1);

        final var nonZeroCell = concat(findInRows, findInCols)
                .filter(j -> cells[j * cols + row] != 0)
                .findFirst();

        if (nonZeroCell.isPresent()) {
            swap(row * cols + row, nonZeroCell.getAsInt());
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
        log.warning("Swapping cols...");

    }

    private void swapRows(int row1, int row2) {
        log.warning("Swapping rows...");
        range(0, cols).forEach(col -> {
            final var tmp = get(row1, col);
            cells[row1 * cols + col] = get(row2, col);
            cells[row2 * cols + col] = tmp;
        });
    }

    private void stage1(int row) {
        final var diagonal = row * cols + row;
        range(row + 1, cols).forEach(i -> cells[row * cols + i] /= cells[diagonal]);
        cells[diagonal] = 1;

        range(row + 1, cols - 1).forEach(i -> {
            final var k = cells[i * cols + row];
            range(row, cols).forEach(col -> cells[i * cols + col] -= k * cells[row * cols + col]);
        });
        log.fine(this::toString);
    }

    private void stage2(int row) {
        for (int i = row; i-- > 0; ) {
            cells[i * cols + cols - 1] -= cells[i * cols + row] * cells[row * cols + cols - 1];
            cells[i * cols + row] = 0;
        }
        log.fine(this::toString);
    }

    public double[] getVariables() {
        return range(0, rows).mapToDouble(i -> cells[i * cols + cols - 1]).toArray();
    }

    @Override
    public String toString() {
        return "\n" + range(0, cells.length)
                .mapToObj(i -> String.format("%8.2f%s", cells[i], (i + 1) % cols == 0 ? "\n" : " "))
                .collect(Collectors.joining());
    }

}
