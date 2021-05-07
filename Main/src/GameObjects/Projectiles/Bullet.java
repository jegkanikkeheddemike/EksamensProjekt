package GameObjects.Projectiles;

import java.util.*;

import Framework.*;
import Setup.Main;

public class Bullet extends GameObject {
    float damage = Main.player.getWeapon().damage;
    float range = Main.player.getWeapon().range;
    float length;
    static int lifeSpan = 10;
    int timeAlive = 0;
    float xEnd;
    float yEnd;
    GameObject collidedWith;

    public Bullet(float rotation) {
        super(Main.player.middleX(), Main.player.middleY(), 0, 0);
        classID = "Bullet";
        Random r = new Random();
        rotation = (-rotation + (float) Math.PI / 2)
                + (Main.player.getWeapon().spread / 2 - ((float) r.nextDouble()) * Main.player.getWeapon().spread);
        xEnd = Main.player.middleX() + (float) Math.sin(rotation) * range;
        yEnd = Main.player.middleY() + (float) Math.cos(rotation) * range;
        checkCollide();
    }

    @Override
    public void draw() {
        Main.main.stroke(255, 0, 0, 255f - 255f * ((float) timeAlive / (float) lifeSpan));
        Main.main.line(x, y, xEnd, yEnd);
    }

    @Override
    public void step() {
        if (timeAlive >= lifeSpan)
            delete();
        timeAlive++;

    }

    void checkCollide() {
        LineData data = GameMath.lineCollision(x, y, xEnd, yEnd, new String[] {"Wall", "ClosedDoor", "Zombie"});
        if (data.collision) {
            collidedWith = data.gameObject;
            collidedWith.reactGetHit(damage, "Bullet", null);
            xEnd = data.x;
            yEnd = data.y;
        }
    }
}
