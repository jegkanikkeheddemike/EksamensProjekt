public class Player extends Movables {
    float xAcc = 2;
    float yAcc = 2;
    float friction = 0.85f;
    float maxSpeed = 5;

    public Player() {
        super();
        x = 1920 / 2;// Temporary
        y = 1080 / 2;
        w = 50;
        h = 50;
        ySpeed = 0;
        xSpeed = 0;
        classID = "Player";
    }

    @Override
    public void draw() {

        Main.main.noStroke();
        Main.main.fill(255);
        Main.main.pushMatrix();
        Main.main.translate(middleX(), middleY());
        Main.main.rotate(rotation);
        Main.main.rect(0 - w / 2, -h / 2, w, h);
        Main.main.popMatrix();
        drawBorder();
    }

    @Override
    public void step() {
        updateAngle();
        updateShoot();
        updateMove();
    }

    void updateAngle() {
        rotation = GameMath.pointAngle(middleX(), middleY(), Main.main.mouseX, Main.main.mouseY);
    }

    void updateMove() {
        boolean acceleratingX = false, acceleratingY = false;
        if (Main.main.keyDown('s') || Main.main.keyDown('S')) {
            ySpeed += yAcc;
            acceleratingY = true;
        }
        if (Main.main.keyDown('w') || Main.main.keyDown('W')) {
            ySpeed -= yAcc;
            acceleratingY = true;
        }
        if (Main.main.keyDown('a') || Main.main.keyDown('A')) {
            xSpeed -= xAcc;
            acceleratingX = true;
        }
        if (Main.main.keyDown('d') || Main.main.keyDown('D')) {
            xSpeed += xAcc;
            acceleratingX = true;
        }

        if (Math.abs(xSpeed) >= maxSpeed)
            xSpeed = Math.signum(xSpeed) * maxSpeed;
        if (Math.abs(ySpeed) >= maxSpeed)
            ySpeed = Math.signum(ySpeed) * maxSpeed;

        if (!acceleratingX)
            xSpeed *= friction;
        if (!acceleratingY)
            ySpeed *= friction;

        if (getCollisions().length > 0)
            System.out.println("COLL" + Main.gameTime);

        x += xSpeed;
        y += ySpeed;
    }

    void updateShoot() {
        if (Main.mousePressed) {
           new Bullet(rotation, x, y);

        }
    }

}