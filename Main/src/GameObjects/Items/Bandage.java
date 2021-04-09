package GameObjects.Items;

import Setup.Main;

public class Bandage extends Item {
    int amount = 1;
    int maxAmount = 3;
    public Bandage(float x, float y) {
        super(x, y);
        itemType = "Bandage";
    }

    

    @Override
    public void use() {
        Main.player.heal(30);
        deleteFromInventory();
    }
}
