package Framework;
import java.util.ArrayList;
import java.util.Random;

import Setup.Main;
import Threads.SoundThread;
import processing.sound.*;

public class Sound extends GameObject {
    public float volume;
    int timeAlive;

    public Sound(float x, float y, float volume, ArrayList<SoundFile> sounds) {
        super(x, y, 1, 1);
        this.volume = volume;
        int r;
        if(Main.onWindows){ 
            r = new Random().nextInt(sounds.size());
            sounds.get(r).amp(volume / 100);
            SoundThread.playSound(sounds.get(r));
        }
        classID = "Sound";
    }

    public Sound(float x, float y, float volume, SoundFile soundFile) {
        super(x, y, 1, 1);
        this.volume = volume;
        if(Main.onWindows){
            soundFile.amp(volume / 100);
            SoundThread.playSound(soundFile);
        }
        classID = "Sound";
    }

    @Override
    public void step() {
        timeAlive++;
        if (timeAlive > volume)
            delete();

    }

    @Override
    public void draw() {
        Main.main.noFill();
        Main.main.stroke(255);
        Main.main.strokeWeight(2);
        Main.main.beginShape();
        for (float i = 0; i <= 2*Math.PI; i+=2*Math.PI/30) {
            Main.main.vertex(x+(float)Math.sin(i)*timeAlive*5,y+(float)Math.cos(i)*timeAlive*5);
        }
        Main.main.endShape(Main.LINES);
    }

    public static ArrayList<SoundFile> footsteps = new ArrayList<SoundFile>();
    public static ArrayList<SoundFile> shotgunshots = new ArrayList<SoundFile>();
    public static ArrayList<SoundFile> pistolshots = new ArrayList<SoundFile>();
    public static SoundFile screech;

    public static void setupSound() {
        for (int i = 1; i < 11; i++) {
            footsteps.add(new SoundFile(Main.main, "/Sound/FootSteps/" + i + ".wav"));
        }
        for(int i = 1; i <= 3; i ++){
            shotgunshots.add(new SoundFile(Main.main,"/Sound/ShotgunShots/shot" + i + ".wav"));
        }
        for(int i = 1; i <= 4; i ++){
            pistolshots.add(new SoundFile(Main.main,"/Sound/PistolShots/pistolshot" + i + ".wav"));
        }
        screech = new SoundFile(Main.main, "Sound/Zombies/Screech.wav");
    }

}