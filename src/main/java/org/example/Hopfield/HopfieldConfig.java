package org.example.Hopfield;

public class HopfieldConfig {

    public double A = 300;
    public double B = 300;
    public double D = 300;
    public double convergence = 0.05;
    public double step = 1e-6;

    public HopfieldConfig() {
    }

    public HopfieldConfig(double a, double b, double d, double convergence, double step) {
        A = a;
        B = b;
        D = d;
        this.convergence = convergence;
        this.step = step;
    }

    public double getA() {
        return A;
    }

    public double getB() {
        return B;
    }

    public double getD() {
        return D;
    }

    public double getConvergence() {
        return convergence;
    }

    public double getStep() {
        return step;
    }
}
