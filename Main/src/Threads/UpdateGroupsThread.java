package Threads;

import Framework.GeneticAlgorithm.ZombieGenerator;
import Setup.Main;

public class UpdateGroupsThread extends Thread {
    private static UpdateGroupsThread instance;

    private UpdateGroupsThread() {
        setName("UpdateGroupsThread");
        System.out.println(getName() + " ThreadID: " + getId());
    }

    private boolean update;

    @Override
    public void run() {
        while (Main.isRunning) {
            if (update) {
                ZombieGenerator.updateBest();
                update = false;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void update() {
        instance.update = true;
    }

    public static void startThread() {
        if (instance == null) {
            instance = new UpdateGroupsThread();
            instance.start();
        } else {
            System.out.println("Group Thread was already running. Therefore startThread was cancelled");
        }
    }
}
