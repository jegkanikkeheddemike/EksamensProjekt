package Framework.GeneticAlgorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;
import java.util.function.Consumer;

import GameObjects.Zombie;
import GameObjects.Items.Item;
import GameObjects.Items.AmmoItems.*;
import GameObjects.Items.HealthItems.*;
import GameObjects.Items.Weapons.*;
import Setup.Main;

public abstract class ZombieGenerator {
    public static LinkedList<Group> generations = new LinkedList<Group>();
    private static Group[] best20 = new Group[20];

    private static final float itemSpawnFactor = 0.4f;
    private static final float mutationfactor = 0.1f;
    private static final float reproductionSpread = 0.3f;

    public static Group generateGeneration(float budget) {
        // Hvis mængden af generationer er over 20, er de genetiske, ellers er ge
        // tilfældige
        Consumer<Group> generator = generations.size() > 50 ? ZombieGenerator::generateGeneticGroup
                : ZombieGenerator::generateRandomGroup;

        Group generation = new Group(generator, budget);

        generations.add(generation);
        return generation;
    }

    private static void generateGeneticGroup(Group group) {
        // Vælger en tilfælding fra best20 listen. Jo højere på listen (lavere index) jo
        // højere er sandsynligheden for at den bliver valgt til at formere.

        // Det tilfældige index starter 0 og har 70% (reproductionSpread)
        // sandynlighed for at blive større.
        // Den capper ved 20

        int index = 0;
        Random r = new Random();
        while (r.nextFloat() > reproductionSpread && index <= 19 && best20[index + 1] != null)
            index++;

        Group parent = best20[index];

        float[] n = parent.n;
        float[] q = parent.q;

        for (int i = 0; i < q.length; i++) {
            q[i] += (-mutationfactor + 2 * r.nextFloat() * mutationfactor) * q[i];
        }
        for (int i = 0; i < n.length; i++) {
            n[i] += (-mutationfactor + 2 * r.nextFloat() * mutationfactor) * n[i];
        }

        // Clamp q
        for (int i = 0; i < q.length; i++) {
            if (q[i] > 1)
                q[i] = 1;
            else if (q[i] < 0)
                q[i] = 0;
        }

        group.q = q;
        group.n = n;
        group.assignParentID(parent.getID());
        generateGroup(group);
    }

    private static void generateRandomGroup(Group group) {
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
                r.nextFloat() * 0.5f + 0.25f, //
                r.nextFloat() * 0.5f + 0.25f, //
                r.nextFloat() * 0.5f + 0.25f, //
                r.nextFloat() * 0.5f + 0.25f, //
                r.nextFloat() * 0.5f + 0.25f//
        };
        group.n = n;
        group.q = q;
        group.assignParentID(-1);

        generateGroup(group);

    }

    private static void generateGroup(Group group) {
        // NOMALIZE
        float sum = 0;
        for (int i = 0; i < group.n.length; i++) {
            sum += group.n[i];
        }
        for (int i = 0; i < group.n.length; i++) {
            group.n[i] = group.n[i] / sum;
        }

        // Round if not enough to create zombie and if round down give n to other types

        // Generates random items
        while (new Random().nextFloat() > itemSpawnFactor) {
            int itemIndex = getSemiRandomItemIndex();
            group.budget += itemBudgets[itemIndex];
            group.items.add(getItem(itemIndex));
        }
        boolean success = false;
        while (!success) {
            // Adds item to increase budget. Does this until budget can spawn a zombie
            int itemIndex = new Random().nextInt(itemBudgets.length);
            group.budget += itemBudgets[itemIndex];
            group.items.add(getItem(itemIndex));

            for (int i = 0; i < group.n.length; i++) {

                // float allocatedBudget = group.n[i]*group.budget;

                int amount = (int) Math.floor(group.n[i] * group.budget * group.q[i]);

                for (int j = 0; j < amount; j++) {
                    group.zombies.add(zombieFromPreset(i, group.n[i] * group.budget * group.q[i], group));
                    success = true;
                }
            }
        }
        updateBest();
    }

    private static final float diffMult = 1f;

    private static Zombie zombieFromPreset(int preset, float allocatedBudget, Group group) {

        // HER TAGES DER EN DEEP COPY!
        float[] presetGenes = new float[presets[preset].length];
        for (int i = 0; i < presets[preset].length; i++) {
            presetGenes[i] = presets[preset][i];
        }

        float budgetFocusSum = 0;
        for (int i = 0; i < presetBudgetFocus.length; i++) {
            budgetFocusSum += presetBudgetFocus[preset][i];
        }
        // presetGenes.lenth bliver miusset med 1, da presetID er ligegyldigt ikke skal
        // regnes på
        for (int i = 0; i < presetGenes.length - 1; i++) {
            presetGenes[i] = presetGenes[i]
                    + diffMult * (presetBudgetFocus[preset][i] * allocatedBudget) / budgetFocusSum;
        }

        for (int i = 0; i < geneMult.length; i++) {
            presetGenes[i] *= geneMult[i];
        }

        Zombie zombie = new Zombie(-1, -1, presetGenes, group);
        return zombie;
    }

      private static final float[] geneMult = {1.2f,  2f,      1,       1,     40,      1,  2000,    50f };
    //private static final float[] geneMult = {HEAR, SEE, RANGED, SCREECH, DAMAGE, SPRINT, RANGE, Health };

    private static final float[][] presetBudgetFocus = {
            // Hvad budget kommer til at fokusere på som mængde
            // Bemærk at det her ganges med budget, og plusses på presettet.
            // Derfor er 0 ingen effekt på preset
            { // SCREECHER
                    1f, 1f, 0f, 0f, 0.2f, 0f, 0f, 0.5f//
            }, { // SPRINTER
                    0.3f, 0.3f, 0, 0, 1.5f, 0, 0, 0.7f//
            }, { // SNIPER
                    1f, 1f, 0, 0, 2f, 0, 1, 0.3f//
            }, { // Light ranged
                    0.5f, 0.5f, 0, 0, 1f, 0, 0.3f, 0.5f//
            }, { // Bruiser
                    0.5f, 0.5f, 0, 0, 1f, 0, 0, 0.7f//
            } //
    };
    private static float qVal = 0f;
    // BEMÆRK AT DETTE SKAL VÆRE TOM FOR TING SOM ZombieBUdegettet ændrer på. Det er
    // kun tin som ALTID er det samme, som skal have værdier.
    private static final float[][] presets = { // presets for the zombies
            { // SCREECHER
                    qVal, qVal, 0, 1f, qVal, 0, 0, qVal, 0 //
            }, { // SPRINTER
                    qVal, qVal, 0, 0, qVal, 1, 0, qVal, 1 //
            }, { // Sniper
                    qVal, qVal, 1, 0, qVal, 0, qVal, qVal, 2 //
            }, { // Light ranged
                    qVal, qVal, 1, 0, qVal, 0, qVal, qVal, 3 //
            }, { // Bruiser
                    qVal, qVal, 0, 0, qVal, 0, 0, qVal, 4 //
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
                scoreBoard += "ID: " + group.getID() + " Score: " + group.getScore()
                        + ((group.getParentID() == -1) ? " No Parent" : " Parent: " + group.getParentID()) + "\n";
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

    private static final float[] itemBudgets = { // (Starter is not included and cant be spawned)
            0.5f, // AmmoBox 9mm
            0.5f, // AmmoBox 45ACP
            0.8f, // AmmoBox Shells
            0.6f, // Bandage
            1.0f, // Healthpack
            1.5f, // Machete
            0.4f, // Pistol
            1.5f, // Shotgun
    };

    private static int getSemiRandomItemIndex() {
        int[] probabilities = { //
                7, //
                7, //
                5, //
                6, //
                2, //
                1, //
                2, //
                1 //
        };

        { // CHANGE PROBALITIES BASED ON PLAYER
          // If low health
            if (Main.player.health < Main.player.maxHealth / 2f) {
                probabilities[3] *= 2;
                probabilities[4] *= 2;
            }

            // if Uses shotgun
            if (Main.player.cWeapon0.wpnType.equals("Shotgun") || Main.player.cWeapon1.wpnType.equals("Shotgun")) {
                probabilities[2] *= 2;
            }

            // if Uses pistol
            if (Main.player.cWeapon0.wpnType.equals("Pistol") || Main.player.cWeapon1.wpnType.equals("Pistol")) {
                probabilities[2] *= 2;
            }
        }

        // Normalize
        int totalSum = 0;
        for (int i = 0; i < probabilities.length; i++) {
            totalSum += probabilities[i];
        }

        int index = new Random().nextInt(totalSum);
        int i = 0;
        int sum = 0;
        while (sum < index) {
            sum = sum + probabilities[i++];
        }

        return Math.max(0,i-1);
    }

    private static Item getItem(int i) {
        float x = -1, y = -1;
        switch (i) {
            case 0:
                return new AmmoBox9mm(x, y);
            case 1:
                return new AmmoBox45ACP(x, y);
            case 2:
                return new AmmoBoxShells(x, y);
            case 3:
                return new Bandage(x, y);
            case 4:
                return new HealthPack(x, y);
            case 5:
                return new Machete(x, y);
            case 6:
                return new Pistol(x, y);
            case 7:
                return new Shotgun(x, y);
            default:
                System.out.println("Tried to spawn item of index: " + i + " which does not exist. null retuned");
                return null;
        }
    }

    public static final float budgetPerAreaConstant = 1f / 50000;
}
