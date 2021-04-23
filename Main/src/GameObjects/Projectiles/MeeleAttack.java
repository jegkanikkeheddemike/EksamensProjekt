package GameObjects.Projectiles;

import Framework.GameMath;
import Framework.GameObject;
import Framework.Movables;
import Framework.Shaders;
import GameObjects.Zombie;
import GameObjects.Items.Weapons.MeeleWeapon;
import Setup.Main;

public class MeeleAttack extends Movables {
    float direction;
    float range;
    int timeAlive = maxTime;
    float dmg;
    String wpnType;

    private static final int maxTime = 30;

    public MeeleAttack(MeeleWeapon meeleWeapon, Movables parent) {
        super(parent.middleX(), parent.middleY(),
                meeleWeapon.range * (float) Math.sin(-parent.rotation + (float) Math.PI / 2),
                meeleWeapon.range * (float) Math.cos(-parent.rotation + (float) Math.PI / 2));
        direction = parent.rotation;
        //direction = (-direction + (float) Math.PI / 2);
        this.range = meeleWeapon.range;
        this.dmg = meeleWeapon.damage;
        this.wpnType = meeleWeapon.wpnType;
        calculateAttack(parent);
    }

    private void calculateAttack(GameObject parent) {
        for (int i = 0; i < Shaders.zombies.size(); i++) {
            Zombie zombie = Shaders.zombies.get(i);
            if (zombie == parent)
                continue;

            // CHECK IF IN RANGE
            if (!(GameMath.objectDistance(Main.player, zombie) < range)) {
                continue;
            }
            // CHECK IF IN ANGLE

            float angleToZombie = GameMath.objectAngle(Main.player, zombie);
            //angleToZombie = (-angleToZombie + (float) Math.PI / 2);
            

            float relAngle = (float) Math.abs(Math.abs(angleToZombie) - Math.abs(direction));
            if (relAngle > Math.toRadians(45))
                continue;

            zombie.reactGetHit(dmg, wpnType);
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
        super.draw();
        // DRAWS the thingy
        Main.main.fill(255, 0, 0);
        Main.main.pushMatrix();
        Main.main.translate(Main.player.middleX(),Main.player.middleY());
        Main.main.rotate((float)((-Math.PI*1/4)+timeAlive*Math.PI/2/30));
        Main.main.line(0, 0, 0 + range * (float) Math.sin(-direction+Math.PI*1/2), 0 + range * (float) Math.cos(-direction+Math.PI*1/2));
        Main.main.popMatrix();
    }

}
