package Framework;

import Setup.Main;

public abstract class GameObject {
    private static int idCounter = 0;
    public float x, y, w, h;
    public int ID;

    protected GameObject() {
        this.ID = idCounter;
        idCounter++;
        Main.allObjects.add(this);
    }

    public abstract void draw();

    public abstract void step();
}
