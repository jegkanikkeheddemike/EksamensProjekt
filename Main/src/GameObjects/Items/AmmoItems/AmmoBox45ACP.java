package GameObjects.Items.AmmoItems;

import Framework.Images;

public class AmmoBox45ACP extends AmmoItem {
    public AmmoBox45ACP(float x, float y) {
        super(x, y);
        itemType = ".45 ACP";
        amount = 20;
    }

    @Override
    protected void loadSprite() {
        sprite = Images.SPRITE_AMMOBOX45ACP;
    }
}