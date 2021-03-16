import java.util.ArrayList;

public abstract class Movables extends GameObject {
    public float xSpeed, ySpeed, rotation;

    protected Movables() {
        super();
    }

    GameObject[] getCollisions(float offsetX, float offsetY) {
        ArrayList<GameObject> collisions = new ArrayList<GameObject>();

        for (int i = 0; i < Main.nearObjects.size(); i++) {
            GameObject g = Main.nearObjects.get(i);
            if (g == this)
                continue;
            // TOPLEFT
            if (x + offsetX >= g.x && g.x + g.w >= x + offsetX) {
                if (y + offsetY >= g.y && g.y + g.w >= y + offsetY) {
                    collisions.add(g);
                    continue;
                }
            }
            // TOPRIGHT
            if (x + w + offsetX >= g.x && g.x + g.w >= x + w + offsetX) {
                if (y + offsetY >= g.y && g.y + g.w >= y + offsetY) {
                    collisions.add(g);
                    continue;
                }
            }
            // BOTLEFT
            if (x >= g.x + offsetX && g.x + g.w >= x + offsetX) {
                if (y + h + offsetY >= g.y && g.y + g.w >= y + h + offsetY) {
                    collisions.add(g);
                    continue;
                }
            }
            // BOTRIGHT
            if (x + w + offsetX >= g.x && g.x + g.w >= x + w + offsetX) {
                if (y + h + offsetY >= g.y && g.y + g.w >= y + h + offsetY) {
                    collisions.add(g);
                    continue;
                }
            }
        }
        GameObject[] collArray = new GameObject[collisions.size()];
        for (int i = 0; i < collisions.size(); i++) {
            collArray[i] = collisions.get(i);
        }
        return collArray;
    }

    boolean collisionWith(GameObject g, float offsetX, float offsetY) {

        // TOPLEFT
        if (x + offsetX > g.x && g.x + g.w > x + offsetX) {
            if (y + offsetY > g.y && g.y + g.w > y + offsetY) {
                return true;
            }
        }
        // TOPRIGHT
        if (x + w + offsetX > g.x && g.x + g.w > x + w + offsetX) {
            if (y + offsetY > g.y && g.y + g.w > y + offsetY) {
                return true;

            }
        }
        // BOTLEFT
        if (x > g.x + offsetX && g.x + g.w > x + offsetX) {
            if (y + h + offsetY > g.y && g.y + g.w > y + h + offsetY) {
                return true;
            }
        }
        // BOTRIGHT
        if (x + w + offsetX > g.x && g.x + g.w > x + w + offsetX) {
            if (y + h + offsetY > g.y && g.y + g.w > y + h + offsetY) {
                return true;
            }
        }
        return false;
    }
}