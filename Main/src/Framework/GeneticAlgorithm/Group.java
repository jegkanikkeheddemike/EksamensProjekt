package Framework.GeneticAlgorithm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.function.Consumer;

import Framework.GameObject;
import GameObjects.Zombie;
import GameObjects.Items.Item;
import Threads.UpdateGroupsThread;

public class Group implements Comparable<Group>, Serializable {
    public ArrayList<Zombie> zombies = new ArrayList<Zombie>();
    public ArrayList<Item> items = new ArrayList<Item>();

    public float[] n, q;

    private int score;
    int budget;
    private int groupID;

    // -1 = randomly generated and thus no parent
    private int parentID;

    public int getID() {
        return groupID;
    }

    public int getScore() {
        return score;
    }

    public int getParentID() {
        return parentID;
    }

    boolean parentIDHasBeenAssigned = false;

    public void assignParentID(int parentID) {
        if (!parentIDHasBeenAssigned) {
            parentIDHasBeenAssigned = true;
            this.parentID = parentID;
        }
    }

    public Group(Consumer<Group> generator, int budget) {
        this.budget = budget;
        groupID = getNextID();
        generator.accept(this);
    }

    public void setCoordinates(int topleftX, int topleftY, int deltaX, int deltaY) {
        for (Zombie zombie : zombies) {
            setCoordinates(topleftX, topleftY, deltaX, deltaY, zombie);
            while (zombie.getCollisions(0, 0, new String[] { "Wall" }).length > 0) {
                setCoordinates(topleftX, topleftY, deltaX, deltaY, zombie);
            }
        }
        for (Item item : items) {
            setCoordinates(topleftX, topleftY, deltaX, deltaY, item);
            while (item.getCollisions(0, 0, new String[] { "Wall" }).length > 0) {
                setCoordinates(topleftX, topleftY, deltaX, deltaY, item);
            }
        }
    }

    private void setCoordinates(int topleftX, int topleftY, int deltaX, int deltaY, GameObject object) {
        Random r = new Random();
        object.x = topleftX + r.nextInt(deltaX);
        object.y = topleftY + r.nextInt(deltaY);
        if (object.classID.equals("Zombie")) {
            Zombie zombie = (Zombie) object;
            zombie.targetX = zombie.x;
            zombie.targetY = zombie.y;
        }

    }

    // DE ER FUNKTIONER FORDI VI SKAL KUNNE ÆNDRE HVAD GIVER HVAD I SCORE
    public void addDmgToScore(float dmg) {
        score += dmg;
        UpdateGroupsThread.update();
    }

    public void addSpottedToScore() {
        score += 50;
        UpdateGroupsThread.update();
    }

    @Override
    public int compareTo(Group other) {
        return other.getScore() - getScore();
    }

    private static int nextID = 0;

    private int getNextID() {
        nextID++;
        return nextID;
    }
}
