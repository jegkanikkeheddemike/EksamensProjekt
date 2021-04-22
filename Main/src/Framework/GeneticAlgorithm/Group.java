package Framework.GeneticAlgorithm;

import java.util.ArrayList;
import java.util.function.Consumer;

import GameObjects.Zombie;

public class Group {
    public ArrayList<Zombie> zombies = new ArrayList<Zombie>();
    int score;
    int budget;

    public Group(Consumer<Group> generator, int budget){
        this.budget = budget;
        generator.accept(this);
    }

    

    public static void generateRandomGroup(Group self){
        
    }



    public void setCoordinates(int topleftX, int topleftY, int deltaX, int deltaY) {
    }

    
}
