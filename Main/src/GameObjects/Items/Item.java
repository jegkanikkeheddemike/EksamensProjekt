package GameObjects.Items;
import Framework.GameMath;
import Framework.GameObject;
import Setup.Main;
import processing.core.*;

public class Item extends GameObject {
    public PImage sprite;
    public int amount;
    protected int maxAmount;
    protected Boolean held = false;
    public String itemType = null;

    protected Item(float x, float y) {
        super(x, y, 50, 50);
        this.classID = "Item";
    }

    public void use() {
    }

    protected void deleteFromInventory() {
        int index = Main.player.getItemIndexFromInventory(this);
        Main.player.inventory[index] = null;
    }

    @Override
    public void step() {
        if (GameMath.pointDistance(this.x, this.y, Main.player.x, Main.player.y) < this.h && held == false) {
            reactPickedUp();
        }
        if (held) {
            x = Main.player.x;
            y = Main.player.y;
        }

    }

    public void draw() {
        if (!held) {
            if (sprite != null)
                Main.main.image(sprite, x, y, w, h);
            else
                super.draw();
        }

    }

    public void drawInInventory(int x, int y) {
        Main.main.fill(255, 0, 0);
        Main.main.rect(x, y, 80, 80);
    }

    
    public void reactPickedUp() {
        if (Main.player.containsSameItemType(this.itemType)){
            Item oldItem = Main.player.getItemTypeFromInventory(this.itemType);
            oldItem.amount += this.amount;
            
            if(oldItem.amount > oldItem.maxAmount)
                oldItem.amount = oldItem.maxAmount;
            held = true;
            
        }else{
            int index = Main.player.getEmptyInventorySpace();
            if (index != -1) {
                Main.player.inventory[index] = this;
                held = true;
                
            }
        }
    }

}

