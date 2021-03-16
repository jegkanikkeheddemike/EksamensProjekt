public abstract class GameObject {
    private static int idCounter = 0;
    public float x, y, w, h;
    public int ID;

    protected GameObject() {
        this.ID = idCounter;
        idCounter++;
        Main.allObjects.add(this);
        if (Main.player != null) {
            if (GameMath.objectDistance(this, Main.player) < 2000)
                Main.nearObjects.add(this);
        }
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
