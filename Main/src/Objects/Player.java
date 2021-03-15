package Objects;

import Framework.GameObject;
import Framework.Movables;
import Setup.*;

public class Player extends Movables {

    public Player() {
        super();
        x = 1920 / 2;// Temporary
        y = 1080 / 2; 
        w = 20;
        h = 50;
        ySpeed = 10;xSpeed = 10;
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
            y += ySpeed;
            Main.main.text("You Dumb",100,100);
        }if(Main.main.keyDown('w')||Main.main.keyDown('W')){
            y -= ySpeed;
        }if(Main.main.keyDown('a')||Main.main.keyDown('A')){
            x += xSpeed;
        }if(Main.main.keyDown('d')||Main.main.keyDown('D')){
            x -= xSpeed;
        }
        
    }

}
