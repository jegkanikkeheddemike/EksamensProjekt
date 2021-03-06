package GameObjects;

import java.util.ArrayList;
import java.util.Random;

import Framework.*;
import Framework.GeneticAlgorithm.Group;
import Framework.GeneticAlgorithm.ZombieGenerator;
import GameObjects.Projectiles.ZombieMeleeAttack;
import GameObjects.Projectiles.ZombieShot;
import Setup.Main;
import processing.core.PVector;

public class Zombie extends Movables {
    private static final long serialVersionUID = 1L;
    float walkSpeed = 0.8f;
    float sprintSpeed = 2;
    public float[] genes;
    Group group;

    public Zombie(float x, float y, float[] genes, Group group) {
        super(x, y, 40, 40);
        classID = "Zombie";
        targetX = x;
        targetY = y;
        hasHealth = true;

        this.genes = genes;
        this.group = group;

        if (genes[GENE_IS_RANGED] == 1)
            range = genes[GENE_RANGED_RANGE];
        else
            range = 100;
        if (genes[GENE_IS_SPRINTER] == 1)
            sprintSpeed = 2 * sprintSpeed;

        maxHealth = genes[GENE_HEALTH];
        health = maxHealth;
    }

    @Override
    public void draw() {
        Main.main.textAlign(Main.LEFT);
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
        Main.main.fill(255);
        /*String geneDescription = "\n";
        for (int i = 0; i < geneDescriptions.length; i++) {
            if (i != GENE_PRESET_NAME)
                geneDescription += geneDescriptions[i] + genes[i] + "\n";
            else {
                geneDescription += geneDescriptions[i] + ZombieGenerator.presetNames[(int) genes[i]] + "\n";
            }
        }*/
        Main.main.textSize(12);
        // Main.main.text(, x + w + 10, y);
        Main.main.text(ZombieGenerator.presetNames[(int) genes[GENE_PRESET_NAME]]/* + "\nGID:" + group.getID() + "\nVal: "
                + group.getBudget() * group.q[(int) genes[GENE_PRESET_NAME]] * group.n[(int) genes[GENE_PRESET_NAME]]
                + geneDescription + state*/, x + w + 10, y);
        drawAwarenessbar();
        if (!Main.onWindows || !Shaders.shouldDrawShaders())
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

    static float triangles = 100;

    void drawFOVCone() {
        ArrayList<PVector> points = new ArrayList<PVector>();

        for (int i = 0; i < triangles; i++) {
            double angle = -rotation + Math.PI * 2f + (2f * Math.PI * (float) i / triangles) * (fov / 360f)
                    + (fov / 360f) * Math.PI / 2f;
            PVector v = new PVector(middleX() + seeRange * (float) Math.sin(angle),
                    middleY() + seeRange * (float) Math.cos(angle));
            LineData vData = GameMath.lineCollision(middleX(), middleY(), v.x, v.y, new String[] { "Wall" , "ClosedDoor"});
            if (vData.collision) {
                v.x = vData.x;
                v.y = vData.y;
            }
            points.add(v);
        }
        Main.main.fill(0, 0, 255, (int) (255 * 0.1));
        Main.main.noStroke();
        Main.main.beginShape();
        Main.main.vertex(middleX(), middleY());

        for (int i = 0; i < points.size(); i++) {
            Main.main.vertex(points.get(i).x, points.get(i).y);
        }
        Main.main.vertex(middleX(), middleY());

        Main.main.endShape(Main.CLOSE);

        // Viser hvor zombierne er på vej hen. Bruges ikke.
        // Main.main.fill(255, 0, 0);
        // Main.main.circle(targetX, targetY, 20);
    }

    float timeSinceLastPatrolChange = 0;

    @Override
    public void step() {
        if(!attacking){
            lookForPlayer();
            checkIfAddSpottedToScore();
            walk();
            fight();
        }
    }

    boolean hasSpottedThisFight = false;

    void checkIfAddSpottedToScore() {
        if (state == "Chase") {
            if (!hasSpottedThisFight) {
                hasSpottedThisFight = true;
                group.addSpottedToScore();
            }
        } else if (state == "Patrol")
            hasSpottedThisFight = false;
    }

    float dmg = 40;
    public float range;
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

    public boolean attacking = false;

    void attack() {
        cooldown = maxCooldown;
        if (genes[GENE_IS_RANGED] == 0) {
            new ZombieMeleeAttack(this, genes[GENE_DAMAGE]);
            // Main.player.reactGetHit(genes[GENE_DAMAGE], "", this);

        }

        else if (genes[GENE_IS_RANGED] == 1) {
            new ZombieShot(middleX(), middleY(), rotation, genes[GENE_DAMAGE], this);
        }

    }

    public float targetX;
    public float targetY;
    String state = "Patrol";
    float targetRotation;

    void rotateToAngle(float targetRotation, int multiplier) {

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

        float rotateSpeed = 0.02f * multiplier;
        if (state == "Chase" || state == "Find")
            rotateSpeed = 0.05f * multiplier;

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

    boolean avoid;

    void walk() {
        targetRotation = GameMath.pointAngle(middleX(), middleY(), targetX, targetY);
        rotateToAngle(targetRotation, 1);

        if (state == "Find") {
            timeSinceLastPatrolChange++;
            if (GameMath.pointDistance(x, y, targetX, targetY) < 80 || timeSinceLastPatrolChange > 600) {
                timeSinceLastPatrolChange = 0;
                state = "Patrol";
            }
        } else if (state == "Look") {
            targetRotation = GameMath.pointAngle(middleX(), middleY(), Main.player.middleX(), Main.player.middleY());
            targetX = Main.player.x;
            targetY = Main.player.y;
        } else if (state == "Patrol") {
            timeSinceLastPatrolChange++;
            if (GameMath.pointDistance(x, y, targetX, targetY) < 80 || timeSinceLastPatrolChange > 600) {
                timeSinceLastPatrolChange = 0;
                float randomDir = new Random().nextFloat() * 2f * (float) Math.PI;
                float length = new Random().nextFloat() * 1000;

                LineData newLine = GameMath.lineCollision(x, y, x + length * (float) Math.sin(randomDir),
                        y + length * (float) Math.cos(randomDir), new String[] { "Wall", "ClosedDoor" });
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

        if (state == "Chase") {
            cSpeed = sprintSpeed;
            if (genes[GENE_IS_RANGED] == 1) {
                if (GameMath.objectDistance(this, Main.player) < range)
                    cSpeed = 0;
                if (GameMath.objectDistance(this, Main.player) < range / 2f)
                    cSpeed = -sprintSpeed;
            }

        } else if (state == "Find") {
            cSpeed = sprintSpeed;
        } else if (state == "Patrol")

        {
            cSpeed = walkSpeed;
        } else if (state == "Look") {
            cSpeed = 0;
        }
        xSpeed = (float) Math.sin(walkdir) * cSpeed;
        ySpeed = (float) Math.cos(walkdir) * cSpeed;

        if (speed() < cSpeed && (state == "Chase" || state == "Find")) {
            GameObject[] collisions = getCollisions(xSpeed, ySpeed, collisionEntities);
            GameObject nearest = null;
            for (int i = 0; i < collisions.length; i++) {
                if (nearest == null
                        || GameMath.objectDistance(this, collisions[i]) < GameMath.objectDistance(this, nearest)) {
                    nearest = collisions[i];
                }
            }
            if (nearest != null) {
                angleToNearest = -GameMath.objectAngle(this, nearest) + (float) Math.PI / 2;
                float deltaAngle = walkdir - angleToNearest;
                mirrorAngle = walkdir + deltaAngle;
                xSpeed += walkSpeed * Math.sin(mirrorAngle);
                ySpeed += walkSpeed * Math.cos(mirrorAngle);
                avoid = true;
            } else {
                avoid = false;
            }
        } else {
            avoid = false;
        }

        runStandardCollisions();

        x += xSpeed;
        y += ySpeed;
    }

    float mirrorAngle = 0.5f;
    float angleToNearest;

    float awareness;

    static final float fov = 120;
    static final float seeRange = 600;
    static final float seeSense = 15;
    static final float awarenessMulitplier = 0.997f;
    static final float soundSense = 30;

    float fovReal = (fov / 360) * (float) Math.PI * 2;

    void lookForPlayer() {
        awareness *= awarenessMulitplier;
        // INCREASE AWARENESS BASED ON SOUND

        GameObject[] near = Main.getNear();

        for (int i = 0; i < near.length; i++) {
            GameObject g = near[i];
            if (g.classID == "Sound") {
                awareness += ((Sound) g).volume * (soundSense * genes[GENE_HEAR_SKILL])
                        / (Math.pow(GameMath.objectDistance(this, g)/10 + 1,2));
            }
        }

        LineData lineToPlayer = GameMath.lineCollision(middleX(), middleY(), Main.player.middleX(),
                Main.player.middleY(), new String[] { "Wall", "ClosedDoor" });

        if (!lineToPlayer.collision) {
            float dist = GameMath.objectDistance(this, Main.player);
            if (dist < seeRange) {
                float angToPlayer = GameMath.objectAngle(this, Main.player);
                float relAngle = rotation - angToPlayer;

                relAngle = (float) Math.abs(relAngle % (Math.PI * 2));
                float fovReal = (float) ((fov / 360f) * Math.PI);

                if (relAngle < fovReal / 2) {
                    awareness += (genes[GENE_SEE_SKILL] * seeSense)
                            / Math.sqrt(GameMath.objectDistance(this, Main.player));
                    if (awareness > 100f / 3f)
                        state = "Look";
                }
            }
        } else {
            if (awareness > 100f / 1.5f) {
                awareness = 100f / 1.5f;
                state = "Find";
            } else if (awareness < 100f / 1.5f && state != "Find") {
                state = "Patrol";
                hasScreeched = false;
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

                if (!hasScreeched && genes[GENE_CAN_SCREECH] == 1) {
                    new Sound(middleX(), middleY(), 100, Sound.screech);
                    hasScreeched = true;
                }
            }
        }
    }

    boolean hasScreeched;

    @Override
    public void reactGetHit(float dmg, String wpnType, Movables attacker) {
        health -= dmg;
        awareness += 30;
        if (health <= 0) {
            delete();
            //Adds value of zombie to total score
            Main.addToScore(group.getBudget() * group.q[(int) genes[GENE_PRESET_NAME]] * group.n[(int) genes[GENE_PRESET_NAME]]);

            UI.killedFirst = true;
        }
    }

    public static float[] randomGenes() {
        Random r = new Random();
        float hearSkill = 0.5f + r.nextFloat();
        float seeSkill = 0.5f + r.nextFloat();
        float isRanged = r.nextInt(2); // IS 0 OR 1
        float canScreech = r.nextInt(2); // IS 0 OR 1;
        float rangedRange = 0;
        float health = 30 + r.nextFloat() * 30;
        float damage;

        if (isRanged == 0)
            damage = 30 + r.nextFloat() * 20;
        else {
            damage = 20 + r.nextFloat() * 20;
            rangedRange = 200 + r.nextFloat() * 500;
        }

        float isSprinter = 0;
        if (isRanged == 0)
            isSprinter = r.nextInt(2);

        return new float[] { //
                hearSkill, //
                seeSkill, //
                isRanged, //
                canScreech, //
                damage, //
                isSprinter, //
                rangedRange, //
                health, //
                ZombieGenerator.NOPRESET //
        };

    }

    public static final String[] geneDescriptions = { //
            "Hearing: ", //
            "Seeing: ", //
            "Is Ranged: ", //
            "Can Screech: ", //
            "Damage: ", //
            "Is Sprinter: ", //
            "Ranged Range: ", //
            "Health: ", //
            "Preset: " //
    };

    public static final int GENE_HEAR_SKILL = 0;
    public static final int GENE_SEE_SKILL = 1;
    public static final int GENE_IS_RANGED = 2;
    public static final int GENE_CAN_SCREECH = 3;
    public static final int GENE_DAMAGE = 4;
    public static final int GENE_IS_SPRINTER = 5;
    public static final int GENE_RANGED_RANGE = 6;
    public static final int GENE_HEALTH = 7;
    public static final int GENE_PRESET_NAME = 8;
}
