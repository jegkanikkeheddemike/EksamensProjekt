package GameObjects.Items.AmmoItems;

import GameObjects.Items.Item;
import Setup.Main;

public abstract class AmmoItem extends Item {
    public int amount;
    protected int maxAmount;

    AmmoItem(float x, float y) {
        super(x, y);
        maxAmount = 50;
    }

    @Override
    public void drawInInventory(int x, int y, int boxSize) {
        super.drawInInventory(x, y,boxSize);
        Main.main.textAlign(Main.CENTER);
        Main.main.noStroke();
        Main.main.textSize((float)Math.ceil(boxSize/80*13));
        Main.main.fill(255);
        Main.main.text(itemType + ": "+ amount, x+boxSize/2, y+boxSize/1.15f);
        
    }

    @Override
    public void reactPickedUp() {

        Item[] ammoList = Main.player.getItemListOfTypeFromInventory(itemType);

        for (Item item : ammoList) {
            AmmoItem ammoItem = (AmmoItem) item;
            if (ammoItem.amount == ammoItem.maxAmount)
                continue;
            if (ammoItem.amount + amount > ammoItem.maxAmount) {
                int diff = ammoItem.maxAmount - ammoItem.amount;
                ammoItem.amount = ammoItem.maxAmount;
                amount -= diff;
            } else {
                ammoItem.amount += amount;
                amount = 0;
                delete();
                break;
            }
        }
        if (amount > 0) {
            super.reactPickedUp();
        }
    }
}
