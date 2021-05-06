package Framework;

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
    public static final int wallShaderAmount = 100;

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

        if (NearThread.zombies.size() > zombieShaderAmount)
            System.out.println("For mange zombier");
        if (NearThread.walls.size() > wallShaderAmount)
            System.out.println("For mange walls");

        if (NearThread.isReady) { // Hvis klar, skal den opdatere dataen i shadersne, hvis ikke bruger den fra
                                  // tidligere
            NearThread.pauseThread();
            for (int i = 0; i < zombieX.length && i < NearThread.zombies.size(); i++) {
                Zombie z = NearThread.zombies.get(i);
                zombieX[i] = z.middleX();
                zombieY[i] = z.middleY();
                zombieRotation[i] = z.rotation;
            }

            for (int i = 0; i < wallX.length && i < NearThread.walls.size(); i++) {
                Wall w = NearThread.walls.get(i);
                wallX[i] = w.x;
                wallY[i] = w.y;
                wallWidth[i] = w.w;
                wallHeight[i] = w.h;
            }
            NearThread.resumeThread();
        }
        zombieFOVConeShader.set("zombies", Math.min(NearThread.zombies.size(), zombieShaderAmount));
        zombieFOVConeShader.set("walls", Math.min(NearThread.walls.size(), wallShaderAmount));
        zombieFOVConeShader.set("camX", Main.player.middleX());
        zombieFOVConeShader.set("camY", Main.player.middleY());
        zombieFOVConeShader.set("zombieX", zombieX);
        zombieFOVConeShader.set("zombieY", zombieY);
        zombieFOVConeShader.set("zombieRotation", zombieRotation);
        zombieFOVConeShader.set("wallX", wallX);
        zombieFOVConeShader.set("wallY", wallY);
        zombieFOVConeShader.set("wallWidth", wallWidth);
        zombieFOVConeShader.set("wallHeight", wallHeight);
        zombieFOVConeShader.set("resX", (float) Main.main.width);
        zombieFOVConeShader.set("resY", (float) Main.main.height);

        // DET ER MEGET VIGTIGT AT DER IKKE ER NOFILL(), FOR SÅ VIRKER SHADEREN IKKE!!!
        // TILFØJ NOSTROKE ellers Kommer der stroke på hjørnerne af shaderen

        Main.main.fill(255);
        Main.main.noStroke();

        Main.main.shader(zombieFOVConeShader);

        // SHADER RECT DÆKKER HELE SKÆRMEN
        Main.main.rect(Main.player.middleX() - Main.main.width / 2, Main.player.middleY() - Main.main.height / 2,
                Main.main.width, Main.main.height);

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
        return drawShaders || Main.forceShaders;
    }

    public static final int minFrameRateForZombieShader = 50;
}
