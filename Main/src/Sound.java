import java.util.ArrayList;
import java.util.Random;

import processing.sound.*;

public class Sound extends GameObject {
    float volume;
    int timeAlive;

    public Sound(float x, float y, float volume, ArrayList<SoundFile> sounds) {
        super(x, y, 1, 1);
        this.volume = volume;
        int r = new Random().nextInt(sounds.size());
        if(Main.onWindows){
            sounds.get(r).amp(volume / 100);
            sounds.get(r).play();
        }
        classID = "Sound";
    }

    public Sound(float x, float y, float volume, SoundFile soundFile) {
        super(x, y, 1, 1);
        this.volume = volume;
        if(Main.onWindows){
            soundFile.amp(volume / 100);
            soundFile.play();
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
        Main.main.circle(x, y, 10 * timeAlive);
    }

    public static ArrayList<SoundFile> footsteps = new ArrayList<SoundFile>();
    public static SoundFile screech;

    public static void setupSound() {
        for (int i = 1; i < 11; i++) {
            footsteps.add(new SoundFile(Main.main, "\\Sound\\FootSteps\\" + i + ".wav"));
        }
        screech = new SoundFile(Main.main, "Sound\\Zombies\\Screech.wav");
    }

}
