import java.util.Random;

public class TESTMAP {
    public static void createRandomMap() {
        Random r = new Random();
        for (int i = 0; i < 10; i++) {
            new Wall(r.nextInt(1920), r.nextInt(1080));
        }

        new Wall(0, 0, 1920, 100);
        new Wall(0, 1080 - 100, 1920, 100);
        new Wall(0, 100, 100, 1080 - 200);
        new Wall(1920 - 100, 100, 100, 1080 - 200);

        new Zombie(1920f / 3f, 1080f / 2f);
        new Zombie(1920f / 2f, 1080f / 2f);

    }
}
