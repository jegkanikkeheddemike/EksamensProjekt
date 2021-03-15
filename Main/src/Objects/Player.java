package Objects;

import Framework.GameObject;
import processing.core.PApplet;
import processing.core.PVector;
import Setup.*;

public class Player extends GameObject {
    PVector sp = new PVector(0,0);
    PVector acc = new PVector(0,0);
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
        UpdateMove();
    }

    void UpdateMove() {
        if(Main.main.keyDown('s')||Main.main.keyDown('S')){
            this.acc.y -= 1;
            Main.main.text("You Dumb",100,100);
        }if(Main.main.keyDown('w')||Main.main.keyDown('W')){
            this.acc.y += 1;
        }if(Main.main.keyDown('a')||Main.main.keyDown('A')){
            this.acc.x -= 1;
        }if(Main.main.keyDown('d')||Main.main.keyDown('D')){
            this.acc.y += 1;
        }
        this.sp.add(this.acc);
        this.x += this.sp.x;
        this.y += this.sp.y;
        
    }

}
