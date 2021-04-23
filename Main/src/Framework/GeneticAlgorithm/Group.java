package Framework.GeneticAlgorithm;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.Consumer;

import GameObjects.Zombie;

public class Group {
    public ArrayList<Zombie> zombies = new ArrayList<Zombie>();
    int score;
    int budget;

    public Group(Consumer<Group> generator, int budget) {
        this.budget = budget;
        generator.accept(this);
    }

    public void setCoordinates(int topleftX, int topleftY, int deltaX, int deltaY) {
        for (Zombie zombie : zombies) {
            while (zombie.getCollisions(0, 0, new String[] { "Wall" }).length > 0) {
                setCoordinates(topleftX, topleftY, deltaX, deltaY, zombie);
            }
        }
    }

    private void setCoordinates(int topleftX, int topleftY, int deltaX, int deltaY, Zombie zombie) {
        Random r = new Random();
        zombie.x = topleftX + r.nextInt(deltaX);
        zombie.y = topleftY + r.nextInt(deltaY);
    }

    // DE ER FUNKTIONER FORDI VI SKAL KUNNE ÆNDRE HVAD GIVER HVAD I SCORE
    public void addDmgToScore(float dmg) {
        score += dmg;
    }

}
