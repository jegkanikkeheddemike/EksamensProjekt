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
        return (float) Math.atan2(dy,dx);
    }

    public static float objectAngle(GameObject o1, GameObject o2) {
        float x1 = o1.middleX();
        float y1 = o1.middleY();
        float x2 = o2.middleX();
        float y2 = o2.middleY();

        float dx = x2 - x1;
        float dy = y2 - y1;
        return (float) Math.atan2(dy,dx);
    }

    // LINE/RECTANGLE
public static LineCollisionData lineRect(float x1, float y1, float x2, float y2, float rx, float ry, float rw, float rh) {

    // check if the line has hit any of the rectangle's sides
    // uses the Line/Line function below
    LineCollisionData left =   lineLine(x1, y1, x2, y2, rx-rw/2, ry-rh/2, rx-rw/2, ry+rh/2);
    LineCollisionData right =  lineLine(x1, y1, x2, y2, rx+rw/2, ry-rh/2, rx+rw/2, ry+rh/2);
    LineCollisionData top =    lineLine(x1, y1, x2, y2, rx-rw/2, ry-rh/2, rx+rw/2, ry-rh/2);
    LineCollisionData bottom = lineLine(x1, y1, x2, y2, rx-rw/2, ry+rh/2, rx+rw/2, ry+rh/2);
  
    // if ANY of the above are true, the line
    // has hit the rectangle
    LineCollisionData closestCol = new LineCollisionData(false, 0, 0);
    if (left.hasCollided || right.hasCollided || top.hasCollided || bottom.hasCollided) {
        if (left.hasCollided)
            closestCol=left;
        if (right.hasCollided && (closestCol == null||Main.dist(x1, y1, right.xCol, right.yCol)<Main.dist(x1, y1, closestCol.xCol, closestCol.yCol)))
            closestCol=right;
        if (top.hasCollided && (closestCol == null||Main.dist(x1, y1, top.xCol, top.yCol)<Main.dist(x1, y1, closestCol.xCol, closestCol.yCol)))
            closestCol=top;
        if (bottom.hasCollided && (closestCol == null||Main.dist(x1, y1, bottom.xCol, bottom.yCol)<Main.dist(x1, y1, closestCol.xCol, closestCol.yCol)))
            closestCol=bottom;
    }
    return closestCol;
  }
  
  
  // LINE/LINE
  public static LineCollisionData lineLine(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
  
    // calculate the direction of the lines
    float uA = ((x4-x3)*(y1-y3) - (y4-y3)*(x1-x3)) / ((y4-y3)*(x2-x1) - (x4-x3)*(y2-y1));
    float uB = ((x2-x1)*(y1-y3) - (y2-y1)*(x1-x3)) / ((y4-y3)*(x2-x1) - (x4-x3)*(y2-y1));
  
    // if uA and uB are between 0-1, lines are colliding
    if (uA >= 0 && uA <= 1 && uB >= 0 && uB <= 1) {
  
      // optionally, draw a circle where the lines meet
      
       float intersectionX = x1 + (uA * (x2-x1));
       float intersectionY = y1 + (uA * (y2-y1));
    
       LineCollisionData data = new LineCollisionData(true,intersectionX,intersectionY);

       
      return data;
    }
    return new LineCollisionData(false, 0, 0);
  }
}

class LineCollisionData{
    public boolean hasCollided;
    public float xCol;
    public float yCol;

    LineCollisionData(boolean hasCollided, float xCol, float yCol){
        this.hasCollided = hasCollided;
        this.xCol = xCol;
        this.yCol = yCol;
    }


}
