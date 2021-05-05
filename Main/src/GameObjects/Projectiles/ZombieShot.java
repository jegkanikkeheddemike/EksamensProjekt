package GameObjects.Projectiles;

import Framework.*;
import GameObjects.Zombie;
import Setup.Main;

public class ZombieShot extends Movables {
    float dmg;
    Zombie parent;
    float speed = 10;

    public ZombieShot(float x, float y, float rotation, float dmg, Zombie parent) {
        super(x, y, 10, 10);
        this.rotation = -rotation + (float) Math.PI / 2f;
        this.dmg = dmg;
        this.parent = parent;
    }

    @Override
    public void step() {
        GameObject[] collision = getCollisions(0, 0, new String[] { "Wall","Zombie","Player" });
        for (GameObject gameObject : collision) {
            if (gameObject != parent){
                gameObject.reactGetHit(dmg, "ZRanged", parent);
                System.out.println("ZOEG");
                delete();
            }
        }
        xSpeed = speed * (float) Math.sin(rotation);
        ySpeed = speed * (float) Math.cos(rotation);

        x += xSpeed;
        y += ySpeed;
    }

    @Override
    public void draw() {
        Main.main.fill(255);
        Main.main.noStroke();
        Main.main.circle(x, y, 10);
    }

}
