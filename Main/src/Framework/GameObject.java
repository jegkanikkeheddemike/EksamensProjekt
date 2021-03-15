package Framework;

public abstract class GameObject {
    private static int idCounter = 0;
    public float x, y, width, height;
    public int ID;

    public GameObject() {
        this.ID = idCounter;
        idCounter++;
    }

    public abstract void draw();

    public abstract void step();
}
