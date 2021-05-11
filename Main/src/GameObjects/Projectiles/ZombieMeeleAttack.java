package GameObjects.Projectiles;

import Framework.GameMath;
import Framework.GameObject;
import Framework.Movables;
import GameObjects.Zombie;
import Setup.Main;

public class ZombieMeeleAttack extends GameObject{

    float direction;
    float range;
    int timeAlive = maxTime;
    float dmg;
    String wpnType;
    Zombie parent;
    static int attackTime = 20;

    private static final int maxTime = attackTime + 10;

    public ZombieMeeleAttack(Zombie parent, float dmg) {
        super(parent.middleX(), parent.middleY(),
                parent.range * (float) Math.sin(-parent.rotation + (float) Math.PI / 2),
                parent.range * (float) Math.cos(-parent.rotation + (float) Math.PI / 2));
        this.parent = parent;
        range = parent.range;
        direction = parent.rotation;
        this.dmg = dmg;
    }

    private void calculateAttack() {
        
        float angleToZombie = GameMath.objectAngle(parent, Main.player);

        float relAngle = (float) Math.abs(Math.abs(angleToZombie) - Math.abs(direction));
        if (relAngle > Math.toRadians(12)|| GameMath.lineCollision(parent, Main.player, new String[] {"Wall"}).collision){
            delete();
        }

        x = (float)(parent.middleX() + range*Math.cos(angleToZombie));
        y = (float)(parent.middleY() + range*Math.sin(angleToZombie));
        
    }
    
    private void performAttack(){
        float distToWall = -1;
        float distToPlayer = -1;
        if (GameMath.lineCollision(parent.middleX(), parent.middleY(), x, y,new String[]{"Wall"}).collision){
            distToWall = GameMath.objectDistance(GameMath.lineCollision(parent.middleX(), parent.middleY(), x, y,new String[]{"Wall"}).gameObject, parent);
        }
        if (GameMath.lineCollision(parent.middleX(), parent.middleY(), x, y,new String[]{"Player"}).collision){
            distToPlayer = GameMath.objectDistance(GameMath.lineCollision(parent.middleX(), parent.middleY(), x, y,new String[]{"Player"}).gameObject, parent);
        }

        if((distToPlayer < distToWall && distToPlayer != -1) || (distToWall == -1 && distToPlayer != -1) ){
            Main.player.reactGetHit(dmg, "zMeele", parent);
        }

    }

    @Override
    public void step() {
        if (parent.isDeleted){
            this.delete();
        }
        if (timeAlive == maxTime){
            
            parent.attacking = true;
            calculateAttack();
        }

        if (timeAlive == maxTime-attackTime)
            performAttack();
        
        if (timeAlive <= 0){
            parent.attacking = false;
            delete();
        }
        
        timeAlive--;
        if (Main.toBeDelted.contains(this))
            parent.attacking = false;
    }

    @Override
    public void draw() {
        if (timeAlive > maxTime-attackTime && !Main.toBeDelted.contains(this)){
            Main.main.strokeWeight(2);
            Main.main.stroke(0,255,0);
            Main.main.line(parent.middleX(),parent.middleY(),x,y);
        }
        if (timeAlive <= maxTime-attackTime){
            Main.main.strokeWeight(5);
            Main.main.stroke(200,0,0);
            Main.main.line(parent.middleX(),parent.middleY(),x,y);
        }
    }
}
