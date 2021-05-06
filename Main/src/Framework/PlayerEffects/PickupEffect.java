package Framework.PlayerEffects;

import GameObjects.Items.Item;
import Setup.Main;

public class PickupEffect extends PlayerEffect{
    Item item;
    static int pickupTime = 10;
    public PickupEffect(Item item) {
        super(pickupTime, "Picking Up: " + item.classID);
        this.item = item;
        Main.player.occupied = true;
    }

    @Override
    void effect() {
        Main.player.occupied = true;
        Main.player.canRun = false;
        
    }

    @Override
    void endEffect() {
        item.reactPickedUp();
    }

    @Override
    protected void drawEffectSymbol(int x, int y) {
        // TODO Auto-generated method stub
        
    }
    
}
