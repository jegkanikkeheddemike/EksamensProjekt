import java.util.Random;

public class TESTMAP {
    public static void createRandomMap() {
        Random r = new Random();
        for (int i = 0; i < 10; i++) {
            new Wall(r.nextInt(1920), r.nextInt(1080));
        }
        for (int x = 0; x < Main.main.width; x += 100) {
            new Wall(x, 0);
            new Wall(x, Main.main.height - 100);
        }
        for (int y = 100; y < Main.main.height - 100; y += 100) {
            new Wall(0, y);
            new Wall(Main.main.width - 100, y);
        }

        new Zombie(1920f / 3f, 1080f / 2f);
        new Zombie(1920f / 2f, 1080f / 2f);

    }
}
