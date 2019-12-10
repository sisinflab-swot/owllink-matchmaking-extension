package it.poliba.sisinflab.swottools.owllinkmatchmaking.builtin.response;

public class NormImpl implements Norm {
    private float value;

    public NormImpl(float value) {
        this.value = value;
    }

    public NormImpl(double value) {
        this.value = (float) value;
    }

    @Override
    public float getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
