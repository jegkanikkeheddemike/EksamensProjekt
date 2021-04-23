package Framework.GeneticAlgorithm;

import java.util.LinkedList;
import java.util.Random;

import GameObjects.Zombie;

public abstract class ZombieGenerator {
    private static LinkedList<Group> generations = new LinkedList<Group>();

    public static Group generateGeneration(int budget) {
        //BRUGER INDTIL VIDERE BARE FUNKTIONEN generateRandomGroup
        Group generation = new Group(ZombieGenerator::generateRandomGroup, budget);

        generations.add(generation);
        return generation;
    }

    public static void generateRandomGroup(Group group) {
        Random r = new Random();

        //INDTIL VIDERE HAR DE TILFÆLDIGE ATTRIBUTTER

        //FORDELING
        float[] n = {
            r.nextFloat(),
            r.nextFloat(),
            r.nextFloat(),
            r.nextFloat(),
            r.nextFloat()
        };
        // NOMALIZE
        float sum = 0;
        for (float f : n) {
            sum += f;
        }
        for (float f : n) {
            f=f/sum;
        }

        for (float f : n) {
            for (int i = 0; i< f*group.budget; i ++) {
                group.zombies.add(new Zombie(0, 0, Zombie.randomGenes(),group));
            }
        }

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
