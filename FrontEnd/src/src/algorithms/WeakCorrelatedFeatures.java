package algorithms;

public class WeakCorrelatedFeatures {
    public final String feature1;
    public final String feature2;
    public final float correlation;
    public float deviation;
    public float exp;
    public float zMax;

    public WeakCorrelatedFeatures(String feature1, String feature2, float correlation, float deviation,float exp, float zMax) {
        this.feature1 = feature1;
        this.feature2 = feature2;
        this.correlation = correlation;
        this.deviation = deviation;
        this.exp = exp;
        this.zMax = zMax;
    }
}
