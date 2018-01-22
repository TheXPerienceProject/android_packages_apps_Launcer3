package com.google.research.reflection.predictor;

public class f implements Comparable {
    public float MK;
    public String ML;

    public f(final String ml, final float mk) {
        this.ML = ml;
        this.MK = mk;
    }

    public int compareTo(Object o) {
        return Float.compare(this.MK, ((f) o).MK);
    }
}
