package Objects;

import Framework.GameObject;
import Framework.Movables;
import Setup.*;

public class Player extends Movables {
    float xAcc = 1;
    float yAcc = 1;
    float friction = (float)0.92;
    float maxXSpeed = 10, maxYSpeed = 10;
    public Player() {
        super();
        x = 1920 / 2;// Temporary
        y = 1080 / 2; 
        w = 20;
        h = 50;

        ySpeed = 0;xSpeed = 0;
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
            ySpeed +=yAcc;
            if (ySpeed>maxYSpeed){ySpeed = maxYSpeed;}
        }if(Main.main.keyDown('w')||Main.main.keyDown('W')){
            ySpeed -=yAcc;
            if (ySpeed<-maxYSpeed){ySpeed = -maxYSpeed;}
        }if(Main.main.keyDown('a')||Main.main.keyDown('A')){
            xSpeed -= xAcc;
            if (xSpeed<-maxXSpeed){xSpeed = -maxXSpeed;}
        }if(Main.main.keyDown('d')||Main.main.keyDown('D')){
            xSpeed += xAcc;
            if (xSpeed>maxXSpeed){xSpeed = maxXSpeed;}
        }
        Main.main.text(xSpeed,100,100);
        x+=xSpeed;
        y+=ySpeed;
        xSpeed *= friction;
        ySpeed *= friction;
    }

}
