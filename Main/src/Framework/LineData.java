package Framework;

public class LineData {
    public boolean collision;
    public float x;
    public float y;
    float length;
    public GameObject gameObject;

    public LineData(float x, float y) {
        collision = true;
        this.x = x;
        this.y = y;
        ;
    }

    LineData() {
        collision = false;
        x = Float.NaN;
        y = Float.NaN;
    }

    public static final LineData noCollision = new LineData();
}