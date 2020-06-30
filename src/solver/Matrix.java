package solver;

import java.io.PrintWriter;
import java.util.Scanner;

public interface Matrix {
    void read(Scanner scanner);
    void print();
    void solve();
    void write(PrintWriter writer);
}
