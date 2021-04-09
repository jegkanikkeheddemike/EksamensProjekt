package Items;

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

class AmmoBox45APC extends AmmoItems {
    AmmoBox45APC(float x, float y) {
        super(x, y);
        itemType = ".45 ACP";
        amount = 20;
    }
}

class AmmoBox9mm extends AmmoItems {
    AmmoBox9mm(float x, float y) {
        super(x, y);
        itemType = "9mm";
        amount = 20;
    }
}