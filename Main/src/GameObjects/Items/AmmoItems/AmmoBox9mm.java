package GameObjects.Items.AmmoItems;

import Framework.Images;

public class AmmoBox9mm extends AmmoItem {
    public AmmoBox9mm(float x, float y) {
        super(x, y);
        itemType = "9mm";
        amount = 20;
    }

    @Override
    protected void loadSprite() {
        sprite_ref = Images.SPRITE_AMMOBOX9MM_REF;
    }
}