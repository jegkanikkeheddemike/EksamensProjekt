package Objects;

import Framework.GameObject;
import processing.core.PApplet;
import Setup.*;

public class Player extends GameObject {

    Player() {
        super();
        x = PApplet.DEFAULT_WIDTH/2; y = PApplet.DEFAULT_HEIGHT/2;
        w = 20; h = 50;
    }

    @Override
    public void draw() {
        Main.main.rect(x,y,h,w);

    }

    @Override
    public void step() {
        
    }
    
    void checkMove(){
        

    }

}
