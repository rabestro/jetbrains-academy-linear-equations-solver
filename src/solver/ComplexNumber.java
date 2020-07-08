package solver;

import java.util.logging.Logger;
import java.util.regex.Pattern;

import static java.lang.Double.parseDouble;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public final class ComplexNumber {
    public static final ComplexNumber ZERO = new ComplexNumber(0, 0);
    public static final ComplexNumber ONE = new ComplexNumber(1, 0);
    private static final Pattern ONLY_REAL = Pattern.compile("[-+]?\\d+(\\.\\d*)?");
    private static final Pattern ONLY_IMAGINARY = Pattern.compile("[-+]?\\d*(\\.\\d*)?i");
    private static final Pattern RE_IM = Pattern.compile("(?<=\\d)(?=[-+])");
    private static final Logger log = Logger.getLogger(LinearEquation.class.getName());

    private double real;
    private double imaginary;

    public ComplexNumber(final double real, final double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public ComplexNumber(final String complexNumber) {
        log.info(complexNumber);
        if (ONLY_IMAGINARY.matcher(complexNumber).matches()) {
            real = 0;
            imaginary = parseDouble(complexNumber
                    .replace('i', complexNumber.matches("[-+]?i") ? '1' : ' '));
        } else if (ONLY_REAL.matcher(complexNumber).matches()) {
            real = parseDouble(complexNumber);
            imaginary = 0;
        } else {
            real = parseDouble(RE_IM.split(complexNumber)[0]);
            final var second = RE_IM.split(complexNumber)[1];
            imaginary = parseDouble((second.replace('i', second.matches("[-+]?i") ? '1' : ' ')));
        }
    }

    public ComplexNumber add(final ComplexNumber other) {
        return new ComplexNumber(this.real + other.real, this.imaginary + other.imaginary);
    }

    public ComplexNumber subtract(final ComplexNumber other) {
        return new ComplexNumber(real - other.real, imaginary - other.imaginary);
    }

    public ComplexNumber divide(final ComplexNumber other) {
        ComplexNumber output = this.multiply(other.conjugate());
        double div = pow(other.mod(), 2);
        return new ComplexNumber(output.real / div, output.imaginary / div);
    }

    public double mod() {
        return sqrt(pow(real, 2) + pow(imaginary, 2));
    }

    public ComplexNumber multiply(final ComplexNumber other) {
        return new ComplexNumber(
                real * other.real - imaginary * other.imaginary,
                real * other.imaginary + imaginary * other.real);
    }

    public ComplexNumber conjugate() {
        return new ComplexNumber(real, -imaginary);
    }

    @Override
    public String toString() {
        final String im;
        if (imaginary == 0) {
            im = "";
        } else if (imaginary < 0)
            im = imaginary + "i";
        else
            im = "+" + imaginary + "i";
        return real + im;
    }

    @Override
    public final boolean equals(Object z) {
        if (!(z instanceof ComplexNumber))
            return false;
        ComplexNumber a = (ComplexNumber) z;
        return (real == a.real) && (imaginary == a.imaginary);
    }

}
