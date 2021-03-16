public abstract class GameObject {
    private static int idCounter = 0;
    public float x, y, w, h;
    public int ID;

    protected GameObject() {
        this.ID = idCounter;
        idCounter++;
        Main.toBeCreated.add(this);
    }

    public abstract void draw();

    public abstract void step();

    public float middleX() {
        return x + w / 2;
    }

    public float middleY() {
        return y + h / 2;
    }

    public void delete() {
        Main.toBeDelted.add(this);
    }


}
