import java.util.ArrayList;
import java.util.Random;

import processing.core.PVector;

public class Zombie extends Movables {
    float walkSpeed = 0.8f;
    float sprintSpeed = 2;

    public Zombie(float x, float y) {
        super();
        classID = "Zombie";
        this.x = x;
        this.y = y;
        targetX = x;
        targetY = y;
        w = 40;
        h = 40;
        hasHealth = true;
        health = 50;
    }

    @Override
    public void draw() {

        Main.main.noStroke();
        Main.main.fill(50, 255, 50);
        Main.main.pushMatrix();
        Main.main.translate(middleX(), middleY());
        Main.main.rotate(rotation);
        Main.main.rect(0 - w / 2, -h / 2, w, h);
        Main.main.stroke(255, 0, 0);
        Main.main.strokeWeight(5);
        Main.main.line(0, 0, 35, 0);
        Main.main.popMatrix();

        String awarenesIcon = "";
        if (state == "Find" || state == "Look") {
            Main.main.textSize(50);
            awarenesIcon = "?";
            Main.main.fill(255, 255, 0);
        }
        if (state == "Chase") {
            Main.main.textSize(50);
            awarenesIcon = "!";
            Main.main.fill(255, 0, 0);
        }
        Main.main.text(awarenesIcon, middleX(), middleY() - h);
        Main.main.textSize(20);
        drawAwarenessbar();
        drawFOVCone();
    }

    void drawAwarenessbar() {
        Main.main.noFill();
        Main.main.stroke(0);
        Main.main.strokeWeight(2);
        Main.main.rect(middleX() - 5, middleY() - 50, 10, 40);
        Main.main.noStroke();
        Main.main.fill(255 * (awareness / 100f), 255 - 255 * (awareness / 100f), 0);
        Main.main.rect(middleX() - 5, middleY() - 10, 10, -40 * (awareness / 100f));
    }

    float triangles = 100;

    void drawFOVCone() {

        ArrayList<PVector> points = new ArrayList<PVector>();

        for (int i = 0; i < triangles; i++) {
            double angle = -rotation + Math.PI * 2f + (2f * Math.PI * (float) i / triangles) * (fov / 360f)
                    + (fov / 360f) * Math.PI / 2f;
            PVector v = new PVector(middleX() + seeRange * (float) Math.sin(angle),
                    middleY() + seeRange * (float) Math.cos(angle));
            LineData vData = GameMath.lineCollision(middleX(), middleY(), v.x, v.y,
                    new String[] { "Player", "Zombie" });
            if (vData.collision) {
                v.x = vData.x;
                v.y = vData.y;
            }
            points.add(v);
        }

        Main.main.beginShape();
        Main.main.vertex(middleX(), middleY());
        for (int i = 0; i < points.size(); i++) {
            Main.main.vertex(points.get(i).x, points.get(i).y);
        }
        Main.main.vertex(middleX(), middleY());

        Main.main.fill(100, 100, 255, 50);
        Main.main.noStroke();
        Main.main.endShape();

        Main.main.fill(255, 0, 0);
        Main.main.circle(targetX, targetY, 20);
    }

    float timeSinceLastPatrolChange = 0;

    @Override
    public void step() {
        lookForPlayer();
        walk();
        fight();
    }

    float dmg = 40;
    float range = 100;
    float maxCooldown = 120;
    float cooldown = 0;

    void fight() {
        cooldown--;
        if (state != "Chase")
            return;
        if (GameMath.objectDistance(this, Main.player) <= range) {
            if (cooldown < 0) {
                attack();
            }
        }
    }

    void attack() {
        cooldown = maxCooldown;
        Main.player.reactGetHit(dmg, "ZMeele");
    }

    float targetX;
    float targetY;
    String state = "Patrol";
    float targetRotation;

    void rotateToTargetAngle() {
        targetRotation = GameMath.pointAngle(middleX(), middleY(), targetX, targetY);
        // find which way to rotate
        // get surrounding universe equivalents of desiredAngle
        float desiredAngleM1 = (float) (targetRotation - 2 * Math.PI);
        float desiredAngleP1 = (float) (targetRotation + 2 * Math.PI);

        // find closest universe equivalent of desiredAngle
        float dADiff = Math.abs(targetRotation - rotation);
        float dAM1Diff = Math.abs(desiredAngleM1 - rotation);
        float dAP1Diff = Math.abs(desiredAngleP1 - rotation);

        float closestUniverse = targetRotation;
        float closestDiffToZero = dADiff;
        if (dAM1Diff < closestDiffToZero) {
            closestDiffToZero = dAM1Diff;
            closestUniverse = desiredAngleM1;
        }
        if (dAP1Diff < closestDiffToZero) {
            closestDiffToZero = dAP1Diff;
            closestUniverse = dAM1Diff;
        }

        float rotateSpeed = 0.02f;
        if (state == "Chase" || state == "Find")
            rotateSpeed = 0.05f;

        // rotate towards it
        if (closestUniverse > rotation) {
            rotation += rotateSpeed;
        } else {
            rotation -= rotateSpeed;
        }
        // ensure angle stays under PI
        if (rotation > Math.PI) {
            rotation -= 2 * Math.PI;
        }

        // Lock to target if within this
        if (Math.abs(rotation - closestUniverse) < .05f) {
            rotation = targetRotation;
        }
    }

    void walk() {
        rotateToTargetAngle();

        if (state == "Find") {
            timeSinceLastPatrolChange++;
            if (GameMath.pointDistance(x, y, targetX, targetY) < 80 || timeSinceLastPatrolChange > 600) {
                timeSinceLastPatrolChange = 0;
                state = "Patrol";
            }
        } else if (state == "Look") {
            targetRotation = GameMath.pointAngle(middleX(), middleY(), Main.player.middleX(), Main.player.middleY());
        } else if (state == "Patrol") {
            timeSinceLastPatrolChange++;
            if (GameMath.pointDistance(x, y, targetX, targetY) < 80 || timeSinceLastPatrolChange > 600) {
                timeSinceLastPatrolChange = 0;
                float randomDir = new Random().nextFloat() * 2f * (float) Math.PI;
                float length = new Random().nextFloat() * 1000;

                LineData newLine = GameMath.lineCollision(x, y, x + length * (float) Math.sin(randomDir),
                        y + length * (float) Math.cos(randomDir), new String[] { "Player", "Zombie" });
                if (newLine.collision) {
                    targetX = newLine.x;
                    targetY = newLine.y;
                } else {
                    targetX = x + length * (float) Math.sin(randomDir);
                    targetY = y + length * (float) Math.cos(randomDir);
                }
            }
        }

        float walkdir = -rotation + (float) Math.PI / 2;

        float cSpeed = 0;

        if (state == "Chase" || state == "Find") {
            cSpeed = sprintSpeed;
        } else if (state == "Patrol") {
            cSpeed = walkSpeed;
        } else if (state == "Look") {
            cSpeed = 0;
        }
        xSpeed = (float) Math.sin(walkdir) * cSpeed;
        ySpeed = (float) Math.cos(walkdir) * cSpeed;

        runStandardCollisions();

        x += xSpeed;
        y += ySpeed;
    }

    float awareness;

    static final float fov = 120;
    static final float seeRange = 800;
    static final float seeSense = 15;
    static final float awarenessMulitplier = 0.997f;
    static final float soundSense = 30;

    float fovReal = (fov / 360) * (float) Math.PI * 2;

    void lookForPlayer() {
        awareness *= awarenessMulitplier;
        // INCREASE AWARENESS BASED ON SOUND
        for (int i = 0; i < Main.nearObjects.size(); i++) {
            GameObject g = Main.nearObjects.get(i);
            if (g.classID == "Sound") {
                awareness += ((Sound) g).volume * soundSense / GameMath.objectDistance(this, g);
            }
        }

        LineData lineToPlayer = GameMath.lineCollision(middleX(), middleY(), Main.player.middleX(),
                Main.player.middleY(), new String[] { "Zombie", "Player" });

        if (!lineToPlayer.collision) {
            float dist = GameMath.objectDistance(this, Main.player);
            if (dist < seeRange) {
                float angToPlayer = GameMath.objectAngle(this, Main.player);
                float relAngle = Math.abs(rotation - angToPlayer);

                if (relAngle >= Math.PI * 2f)
                    relAngle -= Math.PI * 2f;

                if (relAngle < (fov / 360f) * Math.PI) {
                    awareness += seeSense / Math.sqrt(GameMath.objectDistance(this, Main.player));
                    if (awareness > 100f / 3f)
                        state = "Look";
                }
            }
        } else {
            if (awareness > 100f / 1.5f) {
                awareness = 100f / 1.5f;
                state = "Find";
                timeSinceLastPatrolChange = 0;
            } else if (awareness < 100f / 1.5f && state != "Find") {
                state = "Patrol";
                timeSinceLastPatrolChange = 0;
            }
        }

        if (awareness < 0)
            awareness = 0;
        if (awareness > 100)
            awareness = 100;

        // IF SEES PLAYER
        if (awareness > 100f / 1.5f) {
            {
                // Minusser med speed for at finde hvor spilleren stod før for Bedre til at
                // finde vej
                state = "Chase";
                targetX = Main.player.middleX() - 5 * Main.player.xSpeed;
                targetY = Main.player.middleY() - 5 * Main.player.ySpeed;
            }
        }
    }

    @Override
    public void reactGetHit(float dmg, String vpnType) {
        health -= dmg;
        awareness += 30;
        if (health <= 0)
            delete();
    }
}
