//package Objects;

//import Framework.GameMath;
//import Framework.Movables;
//import Setup.*;

public class Player extends Movables {
    float xAcc = 2;
    float yAcc = 2;
    float friction = 0.85f;
    float maxSpeed = 5;

    public Player() {
        super();
        x = 1920 / 2;// Temporary
        y = 1080 / 2;
        w = 20;
        h = 50;

        ySpeed = 0;
        xSpeed = 0;
    }

    @Override
    public void draw() {
        Main.main.pushMatrix();
        Main.main.translate(middleX(), middleY());
        Main.main.rotate(rotation);
        Main.main.rect(0 - w / 2, -h / 2, w, h);
        Main.main.popMatrix();

    }

    @Override
    public void step() {
        updateAngle();
        updateMove();
    }

    void updateAngle() {
        rotation = GameMath.pointAngle(middleX(), middleY(), Main.main.mouseX, Main.main.mouseY);
    }

    void updateMove() {
        boolean accelerating = false;
        if (Main.main.keyDown('s') || Main.main.keyDown('S')) {
            ySpeed += yAcc;
            accelerating = true;
        }
        if (Main.main.keyDown('w') || Main.main.keyDown('W')) {
            ySpeed -= yAcc;
            accelerating = true;
        }
        if (Main.main.keyDown('a') || Main.main.keyDown('A')) {
            xSpeed -= xAcc;
            accelerating = true;
        }
        if (Main.main.keyDown('d') || Main.main.keyDown('D')) {
            xSpeed += xAcc;
            accelerating = true;
        }
        x += xSpeed;
        y += ySpeed;

        if (Math.abs(xSpeed) >= maxSpeed)
            xSpeed = Math.signum(xSpeed) * maxSpeed;
        if (Math.abs(ySpeed) >= maxSpeed)
            ySpeed = Math.signum(ySpeed) * maxSpeed;

        if (!accelerating) {
            xSpeed *= friction;
            ySpeed *= friction;
        }

    }

}
