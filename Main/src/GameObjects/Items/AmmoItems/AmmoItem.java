package GameObjects.Items.AmmoItems;

import GameObjects.Items.Item;
import Setup.Main;

public class AmmoItem extends Item {
    public int amount;
    protected int maxAmount;

    AmmoItem(float x, float y) {
        super(x, y);
        maxAmount = 50;
    }

    @Override
    public void drawInInventory(int x, int y) {
        Main.main.fill(255);
        Main.main.rect(x, y, 80, 80);
        Main.main.fill(0);
        Main.main.text(this.itemType,x+40,y+40);
        Main.main.text(this.amount,x+60,y+60);
    }

    @Override
    public void reactPickedUp(){

        Item[] ammoList = Main.player.getItemListOfTypeFromInventory(itemType);
        
        for (Item item:ammoList){
            AmmoItem ammoItem = (AmmoItem) item;
            if (ammoItem.amount == ammoItem.maxAmount)
                continue;
            if(ammoItem.amount + amount > ammoItem.maxAmount){
                int diff = ammoItem.maxAmount - ammoItem.amount;
                ammoItem.amount = ammoItem.maxAmount;
                amount -= diff;
            }else{
                ammoItem.amount += amount;
                amount = 0;
                delete();
                break;
            }
        }
        if (amount > 0){
            super.reactPickedUp();
        }
    }
}



