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
        // INDTIL VIDERE HAR DE TILFÆLDIGE ATTRIBUTTER

        // FORDELING af budget
        float[] n = { //
                r.nextFloat(), //
                r.nextFloat(), //
                r.nextFloat(), //
                r.nextFloat(), //
                r.nextFloat()//
        };
        // Kvalitet eller kvantitet. lav q = høj kvalitet, høj q = høj kvantitet
        float[] q = { //
                r.nextFloat(), //
                r.nextFloat(), //
                r.nextFloat(), //
                r.nextFloat(), //
                r.nextFloat()//
        };
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
            if (n[i] * group.budget * q[i] < 1) {
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

        for (int i = 0; i < n.length; i++) {
            for (int j = 0; j < n[i] * group.budget; j++) {
                float allocatedBudget = n[i] * group.budget * q[i];
                group.zombies.add(randZombieFromPreset(i, allocatedBudget, group));
            }
        }
        updateBest();
    }

    private static Zombie randZombieFromPreset(int preset, float allocatedBudget, Group group) {
        float[] presetGenes = presets[preset].clone(); // SKAL VÆRE CLONE, ellers kommer reference

        float budgetFocusSum = 0;
        for (int i = 0; i < presetBudgetFocus.length; i++) {
            budgetFocusSum += presetBudgetFocus[preset][i];
        }
        // presetGenes.lenth bliver miusset med 1, da presetID er ligegyldigt ikke skal
        // regnes på
        for (int i = 0; i < presetGenes.length - 1; i++) {
            presetGenes[i] = presetGenes[i] + (presetBudgetFocus[preset][i] * allocatedBudget) / budgetFocusSum;
        }
        Zombie zombie = new Zombie(-1, -1, presetGenes, group);
        return zombie;
    }

    private static final float[][] presetBudgetFocus = {
            // Hvad budget kommer til at fokusere på som mængde
            // Bemærk at det her ganges med budget, og plusses på presettet.
            // Derfor er 0 ingen effekt på preset
            { //    SCREECHER
                    1f, 1f, 0f, 0f, 0f, 0f, 0f, 0.5f//
            }, { // SPRINTER
                    0f, 0, 0, 0, 1f, 0, 0, 0.5f//
            }, { // SNIPER
                    0.5f, 1f, 0, 0, 1, 0, 1, 0//
            }, { // Light ranged
                    0.5f, 0.5f, 0, 0, 1f, 0, 0, 0.5f//
            }, { // Bruiser
                    0, 0, 0, 0, 1f, 0, 0, 1//
            } //
    };

    private static final float[][] presets = { // presets for the zombies
            { // SCREECHER
                    1.5f, 1, 0, 1, 15, 0, 0, 20, 0 //
            }, { // SPRINTER
                    0.5f, 0, 5f, 0, 50, 1, 0, 40, 1 //
            }, { // Sniper
                    0.5f, 1, 1, 0, 40, 0, 700, 20, 2 //
            }, { // Light ranged
                    1, 1, 1, 0, 30, 0, 300, 30, 3 //
            }, { // Bruiser
                    0.5f, 0.5f, 0, 0, 30, 0, 0, 70, 4 //
            }
            // Thats all :) (denne kommentar er for ellers er autoformat irriterende)
    };
    public static final String[] presetNames = { // Preset names
            "Screecher", //
            "Sprinter", //
            "Sniper", //
            "Light ranged", //
            "Bruiser", //
            "NO PRESET" // Only if made through Zombie.randomGenes()
    };
    public static final int NOPRESET = 5;

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

    public static final float budgetPerAreaConstant = 1f / 100000;
}
