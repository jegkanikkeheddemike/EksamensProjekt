package GameObjects;

import Framework.GameMath;
import Setup.Main;

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
            Main.main.strokeWeight(5);
            Main.main.rect(x, y, w, h);
        }
    }

    @Override
    public void step(){
        if ((GameMath.objectDistance(this, Main.player) <= Main.player.w+10 && GameMath.objectDistance(this, Main.player) > Main.player.w) && Main.keyTapped('e')){
            if (!open)
                open = true;
            else
                open = false;
        }
        if (open)
            classID = "OpenDoor";
        else
            classID = "ClosedDoor";
    }

    

    
}
