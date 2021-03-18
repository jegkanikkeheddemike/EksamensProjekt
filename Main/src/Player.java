public class Player extends Movables {
    float xAcc = 2;
    float yAcc = 2;
    float friction = 0.85f;
    float sprintSpeed = 5;
    float walkSpeed = 2f;

    public Player() {
        super();
        x = 1920 / 1.5f;// Temporary
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
        Main.main.stroke(255, 0, 0);
        Main.main.strokeWeight(5);
        Main.main.line(0, 0, 35, 0);
        Main.main.popMatrix();
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
        float cMaxSpeed;
        if (Main.main.keyDown(-1/* SHIFT */)) {
            cMaxSpeed = sprintSpeed;
        } else {
            cMaxSpeed = walkSpeed;
        }

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

        if (Math.abs(xSpeed) >= cMaxSpeed)
            xSpeed = Math.signum(xSpeed) * cMaxSpeed;
        if (Math.abs(ySpeed) >= sprintSpeed)
            ySpeed = Math.signum(ySpeed) * cMaxSpeed;

        if (!acceleratingX)
            xSpeed *= friction;
        if (!acceleratingY)
            ySpeed *= friction;

        runStandardCollisions();
        makeSound();

        x += xSpeed;
        y += ySpeed;
    }

    int timeSinceLastWalkSound = 0;
    int timePerWalkSound = 10;

    void makeSound() {
        timeSinceLastWalkSound++;
        if (Main.main.keyDown(-1) && timeSinceLastWalkSound > timePerWalkSound) {
            timeSinceLastWalkSound = 0;
            new Sound(middleX(), middleY(), 20, Sound.footsteps);
        }
    }

    void updateShoot() {
        if (Main.mousePressed) {
            new Bullet(rotation);
        }
    }

}
