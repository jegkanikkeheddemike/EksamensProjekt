import java.util.ArrayList;

public abstract class Movables extends GameObject {
    public float xSpeed, ySpeed, rotation;

    protected Movables() {
        super();
    }

    GameObject[] getCollisions() {
        ArrayList<GameObject> collisions = new ArrayList<GameObject>();

        for (GameObject g : Main.nearObjects) {
            if (g == this)
                continue;
            //TOPLEFT
            if (x >= g.x && g.x + g.w >= x) {
                if (y >= g.y && g.y + g.w >= y) {
                    collisions.add(g);
                }
            }
            //TOPRIGHT
            if (x+w >= g.x && g.x + g.w >= x+w) {
                if (y >= g.y && g.y + g.w >= y) {
                    collisions.add(g);
                }
            }
            //BOTLEFT
            if (x >= g.x && g.x + g.w >= x) {
                if (y+h >= g.y && g.y + g.w >= y+h) {
                    collisions.add(g);
                }
            }
            //BOTRIGHT
            if (x+w >= g.x && g.x + g.w >= x+w) {
                if (y+h >= g.y && g.y + g.w >= y+h) {
                    collisions.add(g);
                }
            }
        }
        GameObject[] collArray = new GameObject[collisions.size()];
        return collArray;
    }
}
