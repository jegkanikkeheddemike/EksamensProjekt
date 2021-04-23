package Framework.GeneticAlgorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

import GameObjects.Zombie;

public abstract class ZombieGenerator {
    private static LinkedList<Group> generations = new LinkedList<Group>();
    private static Group[] best20 = new Group[20];

    public static Group generateGeneration(int budget) {
        // BRUGER INDTIL VIDERE BARE FUNKTIONEN generateRandomGroup
        Group generation = new Group(ZombieGenerator::generateRandomGroup, budget);

        generations.add(generation);
        return generation;
    }

    public static void generateRandomGroup(Group group) {
        Random r = new Random();
        System.out.println("Budget: " + group.budget);
        // INDTIL VIDERE HAR DE TILFÆLDIGE ATTRIBUTTER

        // FORDELING
        float[] n = { r.nextFloat(), r.nextFloat(), r.nextFloat(), r.nextFloat(), r.nextFloat() };
        // NOMALIZE
        float sum = 0;
        for (int i = 0; i < n.length; i++) {
            sum += n[i];
        }
        for (int i = 0; i < n.length; i++) {
            n[i] = n[i] / sum;
        }

        // Round if not enough to create zombie and if round down give n to other types
        int amountSucceded = 5;
        float valueFailed = 0;
        for (int i = 0; i < n.length; i++) {
            if (n[i] * group.budget < 1) {
                amountSucceded--;
                valueFailed += n[i];
                n[i] = -1; // MEANS DONT CREATE
            }
        }

        for (int i = 0; i < n.length; i++) {
            if (n[i] != -1) {
                n[i] += valueFailed / amountSucceded;
            }
        }

        for (float f : n) {
            for (int i = 0; i < f * group.budget; i++) {
                group.zombies.add(new Zombie(0, 0, Zombie.randomGenes(), group));
            }
        }

    }

    public static final float budgetPerAreaConstant = 1f / 100000;

    private static final float[][] presets = { new float[] { // SCREECHER
            1.5f, 1, 5f, 0, 1, 15, 0, 0, 20 },
            new float[] { // SPRINTER
                    0.5f, 0, 5f, 0, 0, 50, 1, 0, 40 },
            new float[] { // Sniper
                    1, 1, 1, 0, 40, 0, 700, 20 },
            new float[] { // Light ranged
                    0.5f, 0.5f, 1, 0, 30, 0, 300, 30 },
            new float[] { // Bruiser
                    0.5f, 0.5f, 0, 0, 30, 0, 0, 70 } };

    public static String getScoreBoard() {
        String scoreBoard = "";

        for (Group group : best20) {
            if (group != null)
                scoreBoard += group.getID() + ": " + group.getScore() + "\n";
        }

        return scoreBoard;
    }

    public static synchronized void updateBest() {
        ArrayList<Group> listCopy = new ArrayList<Group>();
        for (int i = 0; i < generations.size(); i++) {
            listCopy.add(generations.get(i));
        }
        Collections.sort(listCopy);
        for (int i = 0; i < 20 && i < listCopy.size(); i++) {
            best20[i] = listCopy.get(i);
        }
    }
}
