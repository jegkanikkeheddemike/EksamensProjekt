import java.util.ArrayList;

public abstract class Movables extends GameObject {
    public float xSpeed, ySpeed, rotation;

    protected Movables() {
        super();
    }

    GameObject[] getCollisions(float offsetX, float offsetY) {
        ArrayList<GameObject> collisions = new ArrayList<GameObject>();

        for (GameObject g : Main.nearObjects) {
            if (g == this)
                continue;
            // TOPLEFT
            if (x + offsetX >= g.x && g.x + g.w >= x + offsetX) {
                if (y + offsetY >= g.y && g.y + g.w >= y + offsetY) {
                    collisions.add(g);
                }
            }
            // TOPRIGHT
            if (x + w + offsetX >= g.x && g.x + g.w >= x + w + offsetX) {
                if (y + offsetY >= g.y && g.y + g.w >= y + offsetY) {
                    collisions.add(g);
                }
            }
            // BOTLEFT
            if (x >= g.x + offsetX && g.x + g.w >= x + offsetX) {
                if (y + h + offsetY >= g.y && g.y + g.w >= y + h + offsetY) {
                    collisions.add(g);
                }
            }
            // BOTRIGHT
            if (x + w + offsetX >= g.x && g.x + g.w >= x + w + offsetX) {
                if (y + h + offsetY >= g.y && g.y + g.w >= y + h + offsetY) {
                    collisions.add(g);
                }
            }
        }
        GameObject[] collArray = new GameObject[collisions.size()];
        for (int i = 0; i < collisions.size(); i++) {
            collArray[i] = collisions.get(i);
        }
        return collArray;
    }
}
