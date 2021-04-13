package GameObjects.Items;
import Framework.GameMath;
import Framework.GameObject;
import GameObjects.Items.AmmoItems.AmmoItem;
import Setup.Main;
import processing.core.*;

public class Item extends GameObject {
    public PImage sprite;
    
    public Boolean held = false;
    public String itemType = null;

    protected Item(float x, float y) {
        super(x, y, 50, 50);
        this.classID = "Item";
    }

    public void use() {
    }

    public void deleteFromInventory() {
        int index = Main.player.getItemIndexFromInventory(this);
        Main.player.inventory[index] = null;
    }

    @Override
    public void step() {
        if (!held) {

        if (GameMath.pointDistance(this.middleX(), this.middleY(), Main.player.middleX(), Main.player.middleY()) < this.h/2f+Main.player.w/2f && held == false) {
            reactPickedUp();
        }
        
            
        }else{
            x = Main.player.x;
            y = Main.player.y;

        }

    }

    public void draw() {
        if (!held) {
            if (sprite != null){
                Main.main.image(sprite, x, y, w, h);
                Main.main.text(ID,x+50,y);
            }
            else
                super.draw();
        }

    }

    public void drawInInventory(int x, int y) {
        Main.main.fill(255, 0, 0);
        Main.main.rect(x, y, 80, 80);
    }

    
    public void reactPickedUp() {
        
        int index = Main.player.getEmptyInventorySpace();
        if (index != -1) {
            Main.player.inventory[index] = this;
            held = true;
            
            
        }
        
    }

}

