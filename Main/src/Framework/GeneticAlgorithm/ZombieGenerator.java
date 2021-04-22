package Framework.GeneticAlgorithm;

import java.util.LinkedList;

public abstract class ZombieGenerator {
    private static LinkedList<Group> generations = new LinkedList<Group>();

    public static Group generateGeneration(int budget) {
        Group generation = new Group(Group::generateRandomGroup, budget);

        generations.add(generation);
        return generation;
    }

    private static final float[][] presets = {
        new float[] {   //SCREECHER
            1.5f,
            1,5f,
            0,
            1,
            15,
            0,
            0,
            20
        },
        new float[] {   //SPRINTER
            0.5f,
            0,5f,
            0,
            0,
            50,
            1,
            0,
            40
        },
        new float[] {   //Sniper
            1,
            1,
            1,
            0,
            40,
            0,
            700,
            20
        }, 
        new float[] {   //Light ranged
            0.5f,
            0.5f,
            1,
            0,
            30,
            0,
            300,
            30
        },
        new float[] {   //Bruiser 
            0.5f,
            0.5f,
            0,
            0,
            30,
            0,
            0,
            70
        }
    };
}
