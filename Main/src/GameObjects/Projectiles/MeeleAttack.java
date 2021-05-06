package GameObjects.Projectiles;

import Framework.*;
import GameObjects.Zombie;
import GameObjects.Items.Weapons.MeeleWeapon;
import Setup.Main;
import Threads.NearThread;

public class MeeleAttack extends Movables {
    float direction;
    float range;
    int timeAlive = maxTime;
    float dmg;
    String wpnType;
    Movables parent;

    private static final int maxTime = 10;

    public MeeleAttack(MeeleWeapon meeleWeapon, Movables parent) {
        super(parent.middleX(), parent.middleY(),
                meeleWeapon.range * (float) Math.sin(-parent.rotation + (float) Math.PI / 2),
                meeleWeapon.range * (float) Math.cos(-parent.rotation + (float) Math.PI / 2));
        this.parent = parent;
        direction = parent.rotation;
        this.range = meeleWeapon.range;
        this.dmg = meeleWeapon.damage;
        this.wpnType = meeleWeapon.wpnType;
        calculateAttack(parent);
    }

    private void calculateAttack(GameObject parent) {
        for (int i = 0; i < NearThread.zombies.size(); i++) {
            Zombie zombie = NearThread.zombies.get(i);
            if (zombie == parent)
                continue;

            // CHECK IF IN RANGE
            if ((GameMath.objectDistance(Main.player, zombie) > range)){
                continue;
            }
            // CHECK IF IN ANGLE

            float angleToZombie = GameMath.objectAngle(Main.player, zombie);
            

            float relAngle = (float) Math.abs(Math.abs(angleToZombie) - Math.abs(direction));
            if (relAngle > Math.toRadians(45)|| GameMath.lineCollision(Main.player,zombie, new String[] {"Wall"}).collision){
                continue;
            }

            zombie.reactGetHit(dmg, wpnType, null);
        }
    }

    @Override
    public void step() {
        timeAlive--;

        if (timeAlive <= 0)
            delete();
    }

    @Override
    public void draw() {
        // DRAWS the thingy
        Main.main.strokeWeight(2);
        Main.main.stroke(255,0,0);
        Main.main.pushMatrix();
        Main.main.translate(parent.middleX(),parent.middleY());
        Main.main.rotate((float)((-Math.PI*1/4)+timeAlive*Math.PI/2/maxTime));
        
        Main.main.line(0, 0, 0 + range * (float) Math.sin(-direction+Math.PI*1/2), 0 + range * (float) Math.cos(-direction+Math.PI*1/2));
        Main.main.popMatrix();
    }

}
