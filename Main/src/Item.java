
import processing.core.*;

public class Item extends GameObject {
    PImage sprite;
    Boolean held = false;
    String itemType = null;

    Item(float x, float y) {
        super(x, y, 50, 50);
        this.classID = "Item";
    }

    public void use() {
    }

    protected void deleteFromInventory() {
        int index = Main.player.getItemIndexFromInventory(this);
        Main.player.inventory[index] = null;
    }

    @Override
    public void step() {
        if (GameMath.pointDistance(this.x, this.y, Main.player.x, Main.player.y) < this.h) {
            reactPickedUp();
            held = true;
        }
        if (held) {
            x = Main.player.x;
            y = Main.player.y;
        }

    }

    public void draw() {
        if (!held) {
            if (sprite != null)
                Main.main.image(sprite, x, y, w, h);
            else
                super.draw();
        }

    }

    public void drawInInventory(int x, int y) {
        Main.main.fill(255, 0, 0);
        Main.main.rect(x, y, 80, 80);
    }

    public void reactPickedUp() {
    }

}

class Weapon extends Item {
    String wpnType;
    float damage;
    int shotCooldown;
    int cooldown;
    float range;
    float spread;
    String ammoType;

    Weapon(float x, float y, String wpnType) {
        super(x, y);
        this.wpnType = wpnType;
        classID = "Weapon";
        this.sprite = Main.main.loadImage("Data/Images/" + this.wpnType + ".png");
        this.w = (float) sprite.width / 3;
        this.h = (float) sprite.height / 3;
    }

    public void reactPickedUp() {
        if (!Main.player.cWNumber) {
            Main.player.cWeapon0 = this;
        } else {
            Main.player.cWeapon1 = this;
        }

        delete();
    }
}

class Starter extends Weapon {
    public Starter(float x, float y) {
        super(x, y, "Starter");
        this.wpnType = "Starter";
        damage = 5;
        shotCooldown = 30;
        range = 300;
        spread = 0.05f;
        held = true;
        ammoType = ".45 ACP";
    }

    public void use() {
        new Bullet(Main.player.rotation);
    }
}

class Pistol extends Weapon {
    public Pistol(float x, float y) {
        super(x, y, "Pistol");
        this.wpnType = "Pistol";
        damage = 5;
        shotCooldown = 20;
        range = 700;
        spread = 0.02f;
        ammoType = "9 mm";
    }

    public void use() {
        new Bullet(Main.player.rotation);
    }
}