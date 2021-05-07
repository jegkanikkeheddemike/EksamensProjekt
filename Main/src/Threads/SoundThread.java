package Threads;

import java.util.LinkedList;
import java.util.Queue;

import Setup.Main;
import processing.sound.SoundFile;

public class SoundThread extends Thread {
    private static SoundThread instance;
    private static volatile Queue<SoundFile> queue = new LinkedList<SoundFile>();

    SoundThread(){
        setName("SoundThread");
        //System.out.println(getName() + " ThreadID: " + getId());
    }

    public static synchronized void playSound(SoundFile soundFile){
        queue.add(soundFile);
    }

    public static void StartThread(){
        if (instance == null){
            instance = new SoundThread();
            instance.start();
            
        } else {
            System.out.println("SoundThread already runs");
        }
    }
    public void run(){
        while(Main.isRunning){
            if (!queue.isEmpty()){
                queue.peek().play();
                queue.remove();
            }
        }
    }
}
