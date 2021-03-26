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
        float y2 = o2.middleY();
        

        float dx = x2 - x1;
        float dy = y2 - y1;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    public static float pointAngle(float x1, float y1, float x2, float y2) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        return (float) Math.atan2(dy, dx);
    }

    public static float objectAngle(GameObject o1, GameObject o2) {
        float x1 = o1.middleX();
        float y1 = o1.middleY();
        float x2 = o2.middleX();
        float y2 = o2.middleY();

        float dx = x2 - x1;
        float dy = y2 - y1;
        return (float) Math.atan2(dy, dx);
    }

    public static LineData lineCollision(float x1, float y1, float x2, float y2, String[] ignoreList) {
        LineData data = LineData.noCollision;
        for (int i = 0; i < Main.nearObjects.size(); i++) {
            GameObject g = Main.nearObjects.get(i);
            boolean doContinue = false;
            for (int j = 0; j < ignoreList.length; j++) {
                if (ignoreList[j] == g.classID) {
                    doContinue = true;
                    break;
                }
            }
            if (doContinue)
                continue;

            LineData newData = GameMath.lineRect(x1, y1, x2, y2, g.x, g.y, g.w, g.h);
            if (newData.collision) {
                if (data.equals(LineData.noCollision) || newData.length < data.length) {
                    data = newData;
                    data.gameObject = g;
                }
            }
        }
        return data;
    }

    // LINE/RECTANGLE
    private static LineData lineRect(float x1, float y1, float x2, float y2, float rx, float ry, float rw, float rh) {

        // check if the line has hit any of the rectangle's sides
        // uses the Line/Line function below
        LineData[] lineArr = new LineData[4];

        lineArr[0] = lineLine(x1, y1, x2, y2, rx, ry, rx, ry + rh); // LEFT
        lineArr[1] = lineLine(x1, y1, x2, y2, rx + rw, ry, rx + rw, ry + rh); // RIGHT
        lineArr[2] = lineLine(x1, y1, x2, y2, rx, ry, rx + rw, ry); // TOP
        lineArr[3] = lineLine(x1, y1, x2, y2, rx, ry + rh, rx + rw, ry + rh); // BOTTOM

        LineData closest = LineData.noCollision;
        for (int i = 0; i < 4; i++) {
            lineArr[i].length = GameMath.pointDistance(x1, y1, lineArr[i].x, lineArr[i].y);

            if (lineArr[i].collision) {
                if (closest.equals(LineData.noCollision) || lineArr[i].length < closest.length) {
                    closest = lineArr[i];
                }
            }
        }
        return closest;
    }

    // LINE/LINE
    private static LineData lineLine(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {

        // calculate the direction of the lines
        float uA = ((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3)) / ((y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1));
        float uB = ((x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3)) / ((y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1));

        // if uA and uB are between 0-1, lines are colliding
        if (uA >= 0 && uA <= 1 && uB >= 0 && uB <= 1) {

            // optionally, draw a circle where the lines meet
            float intersectionX = x1 + (uA * (x2 - x1));
            float intersectionY = y1 + (uA * (y2 - y1));

            return new LineData(intersectionX, intersectionY);
        }
        return LineData.noCollision;
    }
}

class LineData {
    boolean collision;
    float x, y, length;
    GameObject gameObject;

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