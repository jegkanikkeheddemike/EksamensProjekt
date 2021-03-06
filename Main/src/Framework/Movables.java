package Framework;

import java.util.ArrayList;

import Setup.Main;

public abstract class Movables extends GameObject {
    public float xSpeed, ySpeed, rotation;

    protected Movables(float x, float y, float w, float h) {
        super(x, y, w, h);
    }

    public float speed() {
        return (float) Math.sqrt(xSpeed * xSpeed + ySpeed * ySpeed);
    }
    public final static String[] collisionEntities = { "Wall", "Player", "ClosedDoor" };
    protected void runStandardCollisions() {

        GameObject[] horiColl = getCollisions(xSpeed, 0, collisionEntities);
        // DEN SIGER i++ IKKE GØR NOGET WTF DET ER IKKE SANDT??????
        for (int i = 0; i < horiColl.length; i++) {
            float preX = x;
            int attemps = 0;
            while (!collisionWith(horiColl[i], Math.signum(xSpeed), 0)) {
                x += Math.signum(xSpeed);
                attemps++;
                if (attemps > xSpeed) {
                    x = preX;
                    xSpeed = 0;
                    break;
                }

            }
            xSpeed = 0;
            break;
        }

        GameObject[] vertColl = getCollisions(0, ySpeed, collisionEntities);
        for (int i = 0; i < vertColl.length; i++) {
            float preY = y;
            int attempts = 0;
            while (!collisionWith(vertColl[i], 0, Math.signum(ySpeed))) {
                y += Math.signum(ySpeed);
                attempts++;
                if (attempts > ySpeed) {
                    y = preY;
                    ySpeed = 0;
                    break;
                }
            }
            ySpeed = 0;
            break;
        }

        GameObject[] diagColl = getCollisions(xSpeed, ySpeed, collisionEntities);
        // DEN SIGER i++ IKKE GØR NOGET WTF DET ER IKKE SANDT??????
        for (int i = 0; i < diagColl.length; i++) {
            float preX = x;
            float preY = y;
            int attemps = 0;
            while (!collisionWith(diagColl[i], Math.signum(xSpeed), Math.signum(ySpeed))) {
                x += Math.signum(xSpeed);
                y += Math.signum(ySpeed);
                attemps++;
                if (attemps > speed()*1.5) {
                    x = preX;
                    y = preY;
                    xSpeed = 0;
                    ySpeed = 0;
                    break;
                }

            }
            xSpeed = 0;
            ySpeed = 0;
            break;
        }
    }
    public GameObject[] getCollisions(float offsetX, float offsetY, String[] hitList) {
        ArrayList<GameObject> collisions = new ArrayList<GameObject>();

        GameObject[] near = Main.getNear();

        for (int i = 0; i < near.length; i++) {
            GameObject g = near[i];
            if (g == null) {
                System.out.println("For some reason g is null. nearSize is: " + near.length);
                continue;
            }
            if (g == this)
                continue;

            boolean isOnHitList = false;
            for (int j = 0; j < hitList.length; j++) {
                if (g.classID.equals(hitList[j])){
                    isOnHitList = true;
                    break;
                }
            }
            if (!isOnHitList)
                continue;
            
            // TOPLEFT
            if (x + offsetX >= g.x && g.x + g.w >= x + offsetX) {
                if (y + offsetY >= g.y && g.y + g.h >= y + offsetY) {
                    collisions.add(g);
                    continue;
                }
            }
            // TOPRIGHT
            if (x + w + offsetX >= g.x && g.x + g.w >= x + w + offsetX) {
                if (y + offsetY >= g.y && g.y + g.h >= y + offsetY) {
                    collisions.add(g);
                    continue;
                }
            }
            // BOTLEFT
            if (x >= g.x + offsetX && g.x + g.w >= x + offsetX) {
                if (y + h + offsetY >= g.y && g.y + g.h >= y + h + offsetY) {
                    collisions.add(g);
                    continue;
                }
            }
            // BOTRIGHT
            if (x + w + offsetX >= g.x && g.x + g.w >= x + w + offsetX) {
                if (y + h + offsetY >= g.y && g.y + g.h >= y + h + offsetY) {
                    collisions.add(g);
                    continue;
                }
            }
        }
        GameObject[] collArray = new GameObject[collisions.size()];
        for (int i = 0; i < collisions.size(); i++) {
            collArray[i] = collisions.get(i);
        }
        return collArray;
    }
    protected boolean collisionWith(GameObject g, float offsetX, float offsetY) {

        // TOPLEFT
        if (x + offsetX > g.x && g.x + g.w > x + offsetX) {
            if (y + offsetY > g.y && g.y + g.w > y + offsetY) {
                return true;
            }
        }
        // TOPRIGHT
        if (x + w + offsetX > g.x && g.x + g.w > x + w + offsetX) {
            if (y + offsetY > g.y && g.y + g.h > y + offsetY) {
                return true;

            }
        }
        // BOTLEFT
        if (x > g.x + offsetX && g.x + g.w > x + offsetX) {
            if (y + h + offsetY > g.y && g.y + g.h > y + h + offsetY) {
                return true;
            }
        }
        // BOTRIGHT
        if (x + w + offsetX > g.x && g.x + g.w > x + w + offsetX) {
            if (y + h + offsetY > g.y && g.y + g.h > y + h + offsetY) {
                return true;
            }
        }
        return false;
    }
}