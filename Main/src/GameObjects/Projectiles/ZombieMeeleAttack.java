package GameObjects.Projectiles;

import Framework.GameMath;
import Framework.GameObject;
import Framework.Movables;
import GameObjects.Zombie;
import Setup.Main;

public class ZombieMeeleAttack extends Movables{

    float direction;
    float range;
    int timeAlive = maxTime;
    float dmg;
    String wpnType;
    Movables parent;

    private static final int maxTime = 10;

    public ZombieMeeleAttack(Zombie parent, float dmg) {
        super(parent.middleX(), parent.middleY(),
                parent.range * (float) Math.sin(-parent.rotation + (float) Math.PI / 2),
                parent.range * (float) Math.cos(-parent.rotation + (float) Math.PI / 2));
        this.parent = parent;
        range = parent.range;
        direction = parent.rotation;
        //direction = (-direction + (float) Math.PI / 2);
        this.dmg = dmg;
        calculateAttack(parent);
    }

    private void calculateAttack(GameObject parent) {
        
        float angleToZombie = GameMath.objectAngle(parent, Main.player);
        //angleToZombie = (-angleToZombie + (float) Math.PI / 2);
        

        float relAngle = (float) Math.abs(Math.abs(angleToZombie) - Math.abs(direction));
        if (relAngle > Math.toRadians(45)|| GameMath.lineCollision(parent, Main.player, new String[] {"Wall"}).collision){
            return;
        }

        Main.player.reactGetHit(dmg, "", (Zombie)parent);
        
    }

    @Override
    public void step() {
        timeAlive--;

        if (timeAlive <= 0)
            delete();
    }

    @Override
    public void draw() {
        //Main.println(range);
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
