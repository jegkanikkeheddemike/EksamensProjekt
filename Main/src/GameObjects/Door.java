package GameObjects;

import Framework.GameMath;
import Framework.GameObject;
import Setup.Main;
import Threads.NearThread;

public class Door extends Wall{
    boolean open = false;

    public Door(int x, int y, int w, int h) {
        super(x, y, w, h);
        color = Main.main.color(160,82,45);
        classID = "ClosedDoor";
    }

    @Override
    public void draw(){
        if (!open){
            super.draw();
        }else{
            Main.main.noFill();
            Main.main.strokeWeight(1);
            Main.main.stroke(color);
            Main.main.rect(x, y, w, h);
        }
    }

    @Override
    public void step(){
        Zombie nearestZombie = null;

        if ((GameMath.objectDistance(this, Main.player) <= Math.min(w, h)+20 && GameMath.objectDistance(this, Main.player) > Math.min(w, h)) && Main.keyTapped('e')){
            for (Zombie zombie : NearThread.zombies) {
                if (nearestZombie == null){
                    nearestZombie = zombie;
                }else if(GameMath.objectDistance(zombie, this)<GameMath.objectDistance(nearestZombie, this)){
                    nearestZombie = zombie;
                }
            }
            if ((GameMath.objectDistance(this, nearestZombie) > Math.min(w, h))){
                if (!open)
                    open = true;
                else
                    open = false;
            }
            
        }
        if (open)
            classID = "OpenDoor";
        else
            classID = "ClosedDoor";
    }

    

    
}
