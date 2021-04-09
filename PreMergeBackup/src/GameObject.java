public abstract class GameObject {
    private static int idCounter = 0;
    public float x, y, w, h;
    public int ID;
    public String classID = "NO CLASS ID PLZ FIX";
    public boolean hasHealth;
    public float health;
    public float maxHealth;

    protected GameObject(float x, float y, float w, float h) {
        this.ID = idCounter;
        idCounter++;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        Main.allObjects.add(this);
        Main.nearObjects.add(this);
    }

    public void draw() {
        drawBorder();
    }

    public abstract void step();

    public void drawBorder() {
        Main.main.noFill();
        Main.main.stroke(255, 0, 0);
        Main.main.strokeWeight(3);
        Main.main.rect(x, y, w, h);
    }

    public float middleX() {
        return x + w / 2;
    }

    public float middleY() {
        return y + h / 2;
    }

    public void reactGetHit(float dmg, String wpnType) {
    }

    public void delete() {
        Main.toBeDelted.add(this);
    }

    public void heal(float amount) {
        if (hasHealth) {
            health += amount;
            if (health > maxHealth) {
                health = maxHealth;
            }
        } else {
            System.out.println("Tried to heal " + classID + " which does not use health");
        }
    }
}