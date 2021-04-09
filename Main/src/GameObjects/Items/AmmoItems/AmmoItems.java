package GameObjects.Items.AmmoItems;

import GameObjects.Items.Item;
import Setup.Main;

public class AmmoItems extends Item {
    

    AmmoItems(float x, float y) {
        super(x, y);
        maxAmount = 99;
    }

    @Override
    public void drawInInventory(int x, int y) {
        Main.main.fill(255);
        Main.main.rect(x, y, 80, 80);
        Main.main.fill(0);
        Main.main.text(this.itemType,x+40,y+40);
        Main.main.text(this.amount,x+60,y+60);
    }
    

}



