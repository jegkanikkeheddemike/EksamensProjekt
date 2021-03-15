package Objects;

import Framework.GameObject;
import processing.core.PApplet;
import Setup.*;

public class Player extends GameObject {

    public Player() {
        super();
        x = 1920 / 2;// Temporary
        y = 1080 / 2; 
        w = 20;
        h = 50;
    }

    @Override
    public void draw() {
        Main.main.rect(x, y, w, h);

    }

    @Override
    public void step() {

    }

    void checkMove() {
        //if(){

        //}
    }

}
