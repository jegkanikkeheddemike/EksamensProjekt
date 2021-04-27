package GameObjects.Items.AmmoItems;

import Framework.Images;

public class AmmoBoxShells extends AmmoItem {
    public AmmoBoxShells(float x, float y) {
        super(x, y);
        maxAmount = 12;
        itemType = "Shells";
        amount = 6;
    }



    @Override
    protected void loadSprite() {
        sprite_ref = Images.SPRITE_AMMOBOXSHELLS_REF;
    }
    
}
