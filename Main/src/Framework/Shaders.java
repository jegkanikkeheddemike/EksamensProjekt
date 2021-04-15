package Framework;

import java.util.ArrayList;

import GameObjects.*;
import Setup.Main;
import Threads.ShaderPreRenderWorkThread;
import processing.opengl.PShader;

public class Shaders {
    static PShader zombieFOVConeShader;
    public static ArrayList<Zombie> zombies = new ArrayList<Zombie>();
    public static ArrayList<Wall> walls = new ArrayList<Wall>();

    public static void loadShaders() {
        zombieFOVConeShader = Main.main.loadShader("Shaders\\ZombieViewCone.frag");
    }

    public static void drawZombieFOVCone() {

        // MAX AMOUNT OF ZOMBIES ON SCREEN IS 30 FOR NOW
        float[] zombieX = new float[zombieShaderAmount];
        float[] zombieY = new float[zombieShaderAmount];
        float[] zombieRotation = new float[zombieShaderAmount];
        float[] wallX = new float[wallShaderAmount];
        float[] wallY = new float[wallShaderAmount];
        float[] wallWidth = new float[wallShaderAmount];
        float[] wallHeight = new float[wallShaderAmount];

        for (int i = 0; i < zombies.size(); i++) {
            Zombie z = zombies.get(i);
            zombieX[i] = z.middleX();
            zombieY[i] = z.middleY();
            zombieRotation[i] = z.rotation;
        }

        for (int i = 0; i < walls.size(); i++) {
            Wall w = walls.get(i);
            wallX[i] = w.x;
            wallY[i] = w.y;
            wallWidth[i] = w.w;
            wallHeight[i] = w.h;
        }
        ShaderPreRenderWorkThread.beginWork();

        zombieFOVConeShader.set("zombies", zombies.size());
        zombieFOVConeShader.set("walls", walls.size());
        zombieFOVConeShader.set("camX", Main.player.middleX());
        zombieFOVConeShader.set("camY", Main.player.middleY());
        zombieFOVConeShader.set("zombieX", zombieX);
        zombieFOVConeShader.set("zombieY", zombieY);
        zombieFOVConeShader.set("zombieRotation", zombieRotation);
        zombieFOVConeShader.set("wallX", wallX);
        zombieFOVConeShader.set("wallY", wallY);
        zombieFOVConeShader.set("wallWidth", wallWidth);
        zombieFOVConeShader.set("wallHeight", wallHeight);

        // DET ER MEGET VIGTIGT AT DER IKKE ER NOFILL(), FOR SÅ VIRKER SHADEREN IKKE!!!
        // TILFØJ NOSTROKE ellers Kommer der stroke på hjørnerne af shaderen

        Main.main.fill(255);
        Main.main.noStroke();

        Main.main.shader(zombieFOVConeShader);

        // SHADER RECT DÆKKER HELE SKÆRMEN
        Main.main.rect(Main.player.middleX() - 1920 / 2, Main.player.middleY() - 1080 / 2, 1920, 1080);

        // HUST AT RESET SHADER BAGEFTER, ELLER FÅR ANDRE TEXTURES FORKERT SHADER :( !!
        Main.main.resetShader();
    }

    public static final int zombieShaderAmount = 50;
    public static final int wallShaderAmount = 200;
}
