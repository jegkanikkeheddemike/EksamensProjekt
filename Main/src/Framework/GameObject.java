package Framework;

import Setup.Main;

public abstract class GameObject implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private static int idCounter = 0;
    public float x, y, w, h;
    public int ID;
    public String classID = "NO CLASS ID PLZ FIX";
    public boolean hasHealth;
    public float health;
    public float maxHealth;
    public boolean isDeleted;

    protected GameObject(float x, float y, float w, float h) {
        this.ID = idCounter;
        idCounter++;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        Main.addObjectToLists(this);
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

    public void reactGetHit(float dmg, String wpnType, Movables attacker) {
        if (hasHealth)
            health -= dmg;
    }

    public void delete() {
        isDeleted = true;
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
