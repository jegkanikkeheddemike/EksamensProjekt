import java.util.Random;

public class TESTMAP {
    public static void createRandomMap() {
        Random r = new Random();
        for (int i = 0; i < 15; i++) {
            new Wall(r.nextInt(1920), r.nextInt(1080));
        }
        new Zombie(1920f / 2f, 1080f / 2f);
    }
}
