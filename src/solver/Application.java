package solver;

import java.io.File;
import java.io.FileNotFoundException;

public class Application {
    private final String inputFile;
    private final String outputFile;

    public Application(String inputFile, String outputFile) {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
    }

    public void run() {
        var matrix = new MatrixOld();
        try {
            matrix.read(new File(inputFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        matrix.print();
        matrix.solve();
        matrix.write(new File(outputFile));
    }
}
