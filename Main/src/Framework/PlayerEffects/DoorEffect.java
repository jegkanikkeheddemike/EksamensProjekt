package Framework.PlayerEffects;

import GameObjects.Door;
import Setup.Main;

public class DoorEffect extends PlayerEffect{
    Door door;
    public DoorEffect(Door door) {
        super(Door.openTime, "Dooring");
        this.door = door;
    }

    @Override
    void effect() {
        Main.player.canRun = false;
        Main.player.occupied = true;
        
    }

    @Override
    void endEffect() {
        if (door.open)
            door.open = false;
        else
            door.open = true;
        
    }

    @Override
    protected void drawEffectSymbol(int x, int y) {
        // TODO Auto-generated method stub
        
    }
    
}
