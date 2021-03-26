public class AmmoBox extends Item {
    String type;
    int amount;

    AmmoBox(float x, float y) {
        super(x, y);
    }

    @Override
    public void drawInInventory(int x, int y) {

    }
}

class Ammobox45APC extends AmmoBox {
    Ammobox45APC(float x, float y) {
        super(x, y);
    }
}