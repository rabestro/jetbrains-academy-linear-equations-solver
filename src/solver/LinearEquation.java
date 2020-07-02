package solver;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.logging.*;
import java.util.stream.IntStream;

import static java.lang.Math.min;
import static java.util.stream.IntStream.range;

public final class LinearEquation {
    private static final Logger log = Logger.getLogger(LinearEquation.class.getName());

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
    private int solutions = -1;

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

    public void solve() {
        if (rows < cols - 1) {
            solutions = 2;
            return;
        }
        range(0, min(rows, cols - 1)).takeWhile(this::isNonZero).forEach(this::stage1);

        for (int row = cols - 2; row > 0; --row) {
            stage2(row);
        }
        log.info(Arrays.toString(getSolution()));
    }

    private boolean isNonZero(int i) {
        if (cells[i * cols + i] != 0) {
            return true;
        }
        final var r = range(i + 1, rows).filter(j -> cells[j * cols + i] != 0).findFirst();
        if (r.isPresent()) {
            swapRows(i, r.getAsInt());
            return true;
        }
        return false;
    }

    private void swapRows(int row1, int row2) {
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
            double k = cells[i * cols + row];
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

    public double[] getSolution() {
        return range(0, rows).mapToDouble(i -> cells[i * cols + cols - 1]).toArray();
    }

    @Override
    public String toString() {
        return "\n" + range(0, cells.length)
                .mapToObj(i -> String.format("%8.2f%s", cells[i], (i + 1) % cols == 0 ? "\n" : " "))
                .collect(Collectors.joining());
    }

}
