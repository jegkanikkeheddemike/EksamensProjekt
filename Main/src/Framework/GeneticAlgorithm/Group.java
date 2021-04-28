package Framework.GeneticAlgorithm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.function.Consumer;

import GameObjects.Zombie;
import Threads.UpdateGroupsThread;

public class Group implements Comparable<Group>, Serializable {
    public ArrayList<Zombie> zombies = new ArrayList<Zombie>();
    private int score;
    int budget;
    private int groupID;

    public int getID() {
        return groupID;
    }

    public int getScore() {
        return score;
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
    }

    private void setCoordinates(int topleftX, int topleftY, int deltaX, int deltaY, Zombie zombie) {
        Random r = new Random();
        zombie.x = topleftX + r.nextInt(deltaX);
        zombie.y = topleftY + r.nextInt(deltaY);
        zombie.targetX = zombie.x;
        zombie.targetY = zombie.y;
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
