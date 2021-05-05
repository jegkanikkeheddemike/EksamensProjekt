package Framework;

import java.util.ArrayList;

import GameObjects.*;
import Setup.Main;
import Threads.NearThread;
import processing.opengl.PShader;

public class Shaders {
    static PShader zombieFOVConeShader;
    

    static long renderTime = 0;

    public static void loadShaders() {
        zombieFOVConeShader = Main.main.loadShader("Shaders\\ZombieViewCone.frag");
    }

    public static final int zombieShaderAmount = 20;
    public static final int wallShaderAmount = 70;

    // MAX AMOUNT OF ZOMBIES ON SCREEN IS 30 FOR NOW
    private static float[] zombieX = new float[zombieShaderAmount];
    private static float[] zombieY = new float[zombieShaderAmount];
    private static float[] zombieRotation = new float[zombieShaderAmount];
    private static float[] wallX = new float[wallShaderAmount];
    private static float[] wallY = new float[wallShaderAmount];
    private static float[] wallWidth = new float[wallShaderAmount];
    private static float[] wallHeight = new float[wallShaderAmount];

    public static void drawZombieFOVCone() {
        long preTime = System.currentTimeMillis();

        if (NearThread.isReady) {
            NearThread.pauseThread();
            for (int i = 0; i < zombieX.length&&i<NearThread.zombies.size(); i++) {
                Zombie z = NearThread.zombies.get(i);
                zombieX[i] = z.middleX();
                zombieY[i] = z.middleY();
                zombieRotation[i] = z.rotation;
            }

            for (int i = 0; i < wallX.length&&i<NearThread.walls.size(); i++) {
                Wall w = NearThread.walls.get(i);
                wallX[i] = w.x;
                wallY[i] = w.y;
                wallWidth[i] = w.w;
                wallHeight[i] = w.h;
            }
            NearThread.resumeThread();
        }

        zombieFOVConeShader.set("zombies", NearThread.zombies.size());
        zombieFOVConeShader.set("walls", NearThread.walls.size());
        zombieFOVConeShader.set("camX", Main.player.middleX());
        zombieFOVConeShader.set("camY", Main.player.middleY());
        zombieFOVConeShader.set("zombieX", zombieX);
        zombieFOVConeShader.set("zombieY", zombieY);
        zombieFOVConeShader.set("zombieRotation", zombieRotation);
        zombieFOVConeShader.set("wallX", wallX);
        zombieFOVConeShader.set("wallY", wallY);
        zombieFOVConeShader.set("wallWidth", wallWidth);
        zombieFOVConeShader.set("wallHeight", wallHeight);
        zombieFOVConeShader.set("resX",(float) Main.main.width);
        zombieFOVConeShader.set("resY",(float) Main.main.height);

        // DET ER MEGET VIGTIGT AT DER IKKE ER NOFILL(), FOR SÅ VIRKER SHADEREN IKKE!!!
        // TILFØJ NOSTROKE ellers Kommer der stroke på hjørnerne af shaderen

        Main.main.fill(255);
        Main.main.noStroke();

        Main.main.shader(zombieFOVConeShader);

        // SHADER RECT DÆKKER HELE SKÆRMEN
        Main.main.rect(Main.player.middleX() - 1920 / 2, Main.player.middleY() - 1080 / 2, 1920, 1080);

        // HUST AT RESET SHADER BAGEFTER, ELLER FÅR ANDRE TEXTURES FORKERT SHADER :( !!
        Main.main.resetShader();

        renderTime = System.currentTimeMillis() - preTime;
    }

    static int shaderMaxTime = 3600;
    static int shaderTimer = 0;
    static boolean drawShaders = true;

    public static boolean shouldDrawShaders() {
        if (shaderTimer <= 0 || Main.main.frameRate <= minFrameRateForZombieShader) {
            shaderTimer = shaderMaxTime;
            if (Main.main.frameRate >= minFrameRateForZombieShader) {
                drawShaders = true;
            } else {
                drawShaders = false;
            }
        }
        shaderTimer--;
        return drawShaders;
    }

    public static final int minFrameRateForZombieShader = 50;
}
