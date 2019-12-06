package utils;

import java.io.Serializable;

public abstract class Function implements Serializable {
    protected double min;
    protected double max;
    protected double delta;

    public Function(double min, double max, double delta) {
        this.min = min;
        this.max = max;
        this.delta = delta;
    }

    public abstract double f(double x);

    public double eval() {
        double returnedValue = 0.0;
        for (double i = min; i < max; i += delta) {
            returnedValue += (f(i)*delta);
        }
        return returnedValue;
    }
}
