package GameObjects.Items;

import Framework.GameMath;
import Framework.GameObject;
import Framework.Images;
import Framework.PlayerEffects.*;
import Setup.Main;

public abstract class Item extends GameObject {
    public String sprite_ref;

    public Boolean held = false;
    public String itemType = null;

    protected Item(float x, float y) {
        super(x, y, 50, 50);
        this.classID = "Item";
        loadSprite();
    }

    protected abstract void loadSprite();

    public void use() {
    }

    public void deleteFromInventory() {
        int index = Main.player.getItemIndexFromInventory(this);
        Main.player.inventory[index] = null;
    }

    static int pickUpMaxCooldown = 30;
    static int pickUpCooldown = pickUpMaxCooldown;
    boolean pickingUp = false;

    @Override
    public void step() {
        if (!held) {
            if (GameMath.pointDistance(this.middleX(), this.middleY(), Main.player.middleX(),
                    Main.player.middleY()) < this.h / 2f + Main.player.w / 2f && held == false && Main.keyTapped('e')
                    && !Main.player.occupied) {
                Main.player.addPlayerEffect(new PickupEffect(this));
            }
        } else {
            x = Main.player.middleX() - w / 2;
            y = Main.player.middleY() - h / 2;

        }

    }

    public void draw() {
        if (!held) {
            if (sprite_ref != null) {
                Main.main.image(Images.getSprite(sprite_ref), x, y, w, h);
                Main.main.fill(255);
                Main.main.textSize(12);
                Main.main.text(ID, x + 50, y);
            } else {
                super.draw();
            }
            if (GameMath.pointDistance(this.middleX(), this.middleY(), Main.player.middleX(),
                    Main.player.middleY()) < this.h / 2f + Main.player.w / 2f && held == false) {
                Main.main.textSize(22);
                Main.main.fill(255);
                Main.main.text("E", middleX(), middleY() - 50);
                Main.main.textSize(12);
                Main.main.fill(155);
                ;
                Main.main.text("to pick up", middleX() - 20, middleY() - 40);
            }

        }

    }

    public void drawInInventory(int x, int y) {
        if (sprite_ref != null) {
            Main.main.image(Images.getSprite(sprite_ref), x, y, 80, 80);
        } else {
            super.draw();
        }
    }

    public void reactPickedUp() {

        int index = Main.player.getEmptyInventorySpace();
        if (index != -1) {
            Main.player.inventory[index] = this;
            held = true;

        }
    }

}