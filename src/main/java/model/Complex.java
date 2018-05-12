package model;

import lombok.Getter;
import lombok.Setter;

public class Complex {
    @Setter
    @Getter
    private double real;
    @Setter
    @Getter
    private double imaginary;

    public Complex(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public double abs() {
        return Math.hypot(real, imaginary);
    }

    public double phase() {
        return Math.atan2(imaginary, real);
    }

    public Complex plus(Complex second) {
        Complex first = this;
        double realPart = first.real + second.real;
        double imaginaryPart = first.imaginary + second.imaginary;
        return new Complex(realPart, imaginaryPart);
    }

    public Complex minus(Complex second) {
        Complex first = this;
        double realPart = first.real - second.real;
        double imaginaryPart = first.imaginary - second.imaginary;
        return new Complex(realPart, imaginaryPart);
    }

    public Complex times(Complex second) {
        Complex first = this;
        double realPart = first.real * second.real - first.imaginary * second.imaginary;
        double imaginaryPart = first.real * second.imaginary + first.imaginary * second.real;
        return new Complex(realPart, imaginaryPart);
    }

    public Complex times(double alpha) {
        return new Complex(alpha * real, alpha * imaginary);
    }

    public Complex conjugate() {
        return new Complex(real, -imaginary);
    }

    public static Complex fromPolar(double r, double theta) {
        double imaginary = (r * Math.sin(theta));
        double real = (r * Math.cos(theta));

        return new Complex(real, imaginary);
    }

}
