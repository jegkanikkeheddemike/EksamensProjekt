package GameObjects.Items.AmmoItems;
 
public class AmmoBox45APC extends AmmoItem {
    AmmoBox45APC(float x, float y) {
        super(x, y);
        itemType = ".45 ACP";
        amount = 20;
    }

    @Override
    protected void loadSprite() {
        // TODO Auto-generated method stub
        
    }
}