package Framework;

public class GameMath {
    public static float pointDistance(float x1, float y1, float x2, float y2) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    public static float objectDistance(GameObject o1, GameObject o2) {
        float x1 = o1.x + o1.w / 2;
        float y1 = o1.y + o1.h / 2;

        float x2 = o2.x + o2.w / 2;
        float y2 = o2.y + o2.h / 2;

        float dx = x2 - x1;
        float dy = y2 - y1;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }
}
