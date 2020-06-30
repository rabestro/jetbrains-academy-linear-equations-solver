package solver;

import java.io.PrintWriter;
import java.util.stream.Collectors;

import static java.util.stream.IntStream.range;

public class MatrixNew implements Matrix {
    private final int rows;
    private final int cols;
    private final double[] cells;

    public MatrixNew(final int rows, final int cols, final double[] cells) {
        this.rows = rows;
        this.cols = cols;
        this.cells = cells;
    }

    @Override
    public void solve() {
        range(0, rows).forEach(this::stage1);
    }

    void stage1(int row) {
        final var diagonal = row * cols + row;
        range(row + 1, cols).forEach(i -> cells[row * cols + i] /= cells[diagonal]);
        cells[diagonal] = 1;

        range(row + 1, cols - 1).forEach(i -> {
            double k = cells[i * cols + row];
            range(row, cols).forEach(col -> cells[i * cols + col] -= k * cells[row * cols + col]);
        });

        System.out.println(this);
    }

    @Override
    public void write(PrintWriter writer) {
        range(1, rows).forEach(i -> writer.println(cells[i * cols - 1]));
    }

    @Override
    public String toString() {
        return range(0, cells.length)
                .mapToObj(i -> String.format("%8.2f%s", cells[i], (i + 1) % cols == 0 ? "\n" : " "))
                .collect(Collectors.joining());
    }

}
