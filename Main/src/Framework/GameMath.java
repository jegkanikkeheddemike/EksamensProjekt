package Framework;

public class GameMath {
    public static float pointDistance(float x1, float y1, float x2, float y2) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    public static float objectDistance(GameObject o1, GameObject o2) {
        float x1 = o1.middleX();
        float y1 = o1.middleY();
        float x2 = o2.middleX();
        float y2 = o2.middleY();;

        float dx = x2 - x1;
        float dy = y2 - y1;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    public static float pointAngle(float x1, float y1, float x2, float y2) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        return (float) Math.tanh(dy / dx);
    }

    public static float objectAngle(GameObject o1, GameObject o2) {
        float x1 = o1.middleX();
        float y1 = o1.middleY();
        float x2 = o2.middleX();
        float y2 = o2.middleY();

        float dx = x2 - x1;
        float dy = y2 - y1;
        return (float) Math.tanh(dy / dx);
    }
}
