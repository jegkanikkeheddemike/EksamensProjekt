package GameObjects;

import java.util.ArrayList;
import Framework.*;
import Framework.PlayerEffects.*;
import GameObjects.Items.Item;
import GameObjects.Items.AmmoItems.AmmoItem;
import GameObjects.Items.Weapons.*;
import Setup.Main;
import MapGeneration.Building;
import MapGeneration.Map;
import MapGeneration.Node;

public class Player extends Movables {
    float xAcc = 2;
    float yAcc = 2;
    float friction = 0.85f;
    float sprintSpeed = 5;
    float walkSpeed = 2;
    float sneakSpeed = 1;
    public Weapon cWeapon0;
    public Weapon cWeapon1;
    public boolean cWNumber;

    public Item[] inventory = new Item[10];
    public ArrayList<PlayerEffect> playerEffects = new ArrayList<PlayerEffect>();

    public int getEmptyInventorySpace() {
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] == null)
                return i;
        }
        return -1;
    }

    public boolean containsSameItemType(String itemType) {
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] == null)
                continue;
            if (inventory[i].itemType.equals(itemType))
                return true;
        }
        return false;
    }

    public Item[] getItemListOfTypeFromInventory(String itemType) {
        ArrayList<Item> itemList = new ArrayList<Item>();
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] == null)
                continue;
            if (inventory[i].itemType.equals(itemType))
                itemList.add(inventory[i]);
        }
        Item[] array = new Item[itemList.size()];

        return itemList.toArray(array);

    }

    public Item getItemTypeFromInventory(String itemType) {
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i].itemType.equals(itemType))
                return inventory[i];
        }
        return null;
    }

    public int getItemIndexFromInventory(Item item) {
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] == item)
                return i;
        }
        return -1;
    }

    public void deleteFromInventory(Item item) {
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] == item) {
                item.delete(); // BARE FOR AT VÆRE SIKKER PÅ AT DEN IKKE EKSISTERE LÆNGERE
                inventory[i] = null;
            }
        }
    }

    public Player() {
        super(1920 / 2, 1080 / 2, 50, 50);
        ySpeed = 0;
        xSpeed = 0;
        classID = "Player";
        hasHealth = true;
        maxHealth = 100;
        health = maxHealth;
        cWeapon0 = new Pistol(x, y);
        cWeapon0.held = true;
        cWeapon1 = new Starter(x, y);
        cWeapon1.held = true;
        cWNumber = true;
    }

    @Override
    public void draw() {

        Main.main.noStroke();
        Main.main.fill(255);
        Main.main.pushMatrix();
        Main.main.translate(middleX(), middleY());
        Main.main.rotate(rotation);
        Main.main.rect(0 - w / 2, -h / 2, w, h);
        Main.main.stroke(255, 0, 0);
        Main.main.strokeWeight(5);
        Main.main.line(0, 0, 35, 0);
        Main.main.popMatrix();
    }

    @Override
    public void step() {
        updatePlayerEffects();
        updateAngle();
        updateMove();
        updateWeapons();
        updateUseItems();
    }

    public boolean canWalk = true;
    public boolean canRun = true;
    public boolean occupied = false;

    private void nomalizeEffects() {
        canWalk = true;
        canRun = true;
        occupied = false;
    }

    public void addPlayerEffect(PlayerEffect playerEffect) {
        playerEffects.add(playerEffect);
    }

    public void removePlayerEffect(PlayerEffect playerEffect) {
        playerEffects.remove(playerEffect);
    }

    void updatePlayerEffects() {
        nomalizeEffects();

        for (int i = 0; i < playerEffects.size(); i++) {
            playerEffects.get(i).apply();
        }
    }

    void updateAngle() {
        rotation = GameMath.pointAngle(middleX(), middleY(), Main.getMouseX(), Main.getMouseY());
    }

    void updateMove() {
        float cMaxSpeed;
        if (Main.keyDown(-1/* SHIFT */) && canRun && canWalk) {
            cMaxSpeed = sprintSpeed;
        } else if (Main.keyDown('c'/* CONTROL */) && canWalk) {
            cMaxSpeed = sneakSpeed;
        } else if (canWalk) {
            cMaxSpeed = walkSpeed;
        } else {
            cMaxSpeed = 0;
        }

        boolean acceleratingX = false, acceleratingY = false;
        if (Main.keyDown('s') || Main.keyDown('S')) {
            ySpeed += yAcc;
            acceleratingY = true;
        }
        if (Main.keyDown('w') || Main.keyDown('W')) {
            ySpeed -= yAcc;
            acceleratingY = true;
        }
        if (Main.keyDown('a') || Main.keyDown('A')) {
            xSpeed -= xAcc;
            acceleratingX = true;
        }
        if (Main.keyDown('d') || Main.keyDown('D')) {
            xSpeed += xAcc;
            acceleratingX = true;
        }

        if (Math.abs(xSpeed) >= cMaxSpeed)
            xSpeed = Math.signum(xSpeed) * cMaxSpeed;
        if (Math.abs(ySpeed) >= cMaxSpeed)
            ySpeed = Math.signum(ySpeed) * cMaxSpeed;

        if (!acceleratingX)
            xSpeed *= friction;
        if (!acceleratingY)
            ySpeed *= friction;

        runStandardCollisions();
        makeSound();

        x += xSpeed;
        y += ySpeed;
    }

    void updateCurrentNode(){
        Node newCurrentNode = currentNode;
        float newNodeToPlayer = GameMath.pointDistance(x, y, currentNode.x, currentNode.y);
        for(Node n : currentNode.connected){
            if(n != null){
                float nToPlayer = GameMath.pointDistance(x, y, n.x, n.y);
                if(newNodeToPlayer > nToPlayer){
                    newCurrentNode = n;
                    newNodeToPlayer = nToPlayer;
                }
            }
        }

        if(currentNode != newCurrentNode){
            currentNode = newCurrentNode;
            if(!currentNode.hasHouse)
                currentNode.housesAlongParentEdge();
            
            for(Node n : currentNode.connected){
                if(n != currentNode.parent && n != null){
                    if(n.isEndPoint){
                        Main.m.generateNodesAtNode(n);
                        //Main.m.removeUselessNodes();
                    }
                    if(!n.hasHouse){
                        n.housesAlongParentEdge();
                    }
                }
            }

            for(Node n : Main.m.allNodes){
                if(!n.endOrCornerDone){
                    //END
                    if(n.connected.length == 1){
                        //MAKE A WALL HERE
                        if(n.connected[Map.NORTH] == n.parent || n.connected[Map.SOUTH] == n.parent){
                            new Wall(n.x-Node.roadWidth/2, n.y, n.x+Node.roadWidth/2, n.y);
                        }else if(n.connected[Map.EAST] == n.parent || n.connected[Map.WEST] == n.parent){
                            new Wall(n.x, n.y-Node.roadWidth/2, n.x, n.y+Node.roadWidth/2);
                        }
                        n.endOrCornerDone = true;
                    }
                    //CORNER
                    else if(n.connected.length == 2){
                        //MAKE A WALL HERE
                    }
                }
            }
        }
    }

    public Weapon getWeapon() {
        return cWNumber ? cWeapon1 : cWeapon0;
    }

    void updateWeapons() {
        if (Main.keyTapped('1') && cWeapon0 != null && !occupied) {
            cWNumber = false;
        } else if (Main.keyTapped('2') && cWeapon1 != null && !occupied) {
            cWNumber = true;
        }
        if (Main.mousePressed && getWeapon().cooldown > getWeapon().shotCooldown && getWeapon().cClip > 0
                && !occupied) {
            getWeapon().use();
            getWeapon().cClip -= 1;
            getWeapon().cooldown = 0;
        } else {
            getWeapon().cooldown += 1;
        }
        if (Main.keyTapped('r') && getWeapon().cClip != getWeapon().clipSize && !occupied) {
            addPlayerEffect(new ReloadEffect(getWeapon()));
        }
    }

    public void reload() {
        Item[] ammoList = Main.player.getItemListOfTypeFromInventory(getWeapon().ammoType);
        for (Item item : ammoList) {
            AmmoItem ammoItem = (AmmoItem) item;
            if (ammoItem.amount > (getWeapon().clipSize - getWeapon().cClip)) {
                ammoItem.amount -= (getWeapon().clipSize - getWeapon().cClip);
                getWeapon().cClip = getWeapon().clipSize;
            } else if (ammoItem.amount != 0) {
                getWeapon().cClip += ammoItem.amount;
                ammoItem.amount = 0;
            }
            if (ammoItem.amount == 0) {
                ammoItem.deleteFromInventory();
            }
        }
    }

    int timeSinceLastWalkSound = 0;
    int timePerWalkSound = 10;

    void makeSound() {
        timeSinceLastWalkSound++;
        if (Math.floor(speed()) == 0)
            return;
        if (Main.keyDown(-1) && timeSinceLastWalkSound > timePerWalkSound && canRun) {
            timeSinceLastWalkSound = 0;
            new Sound(middleX(), middleY(), 20, Sound.footsteps);
        } else if (Main.keyDown('c') && timeSinceLastWalkSound > timePerWalkSound) {
            timeSinceLastWalkSound = 0;
            new Sound(middleX(), middleY(), 2, Sound.footsteps);
        } else if (timeSinceLastWalkSound > timePerWalkSound) {
            timeSinceLastWalkSound = 0;
            new Sound(middleX(), middleY(), 7, Sound.footsteps);
        }
    }

    public void reactGetHit(float dmg, String vpnType) {
        health -= dmg;
    }

    void updateUseItems() {
        if (!Main.mouseReleased)
            return;

        if (Main.main.mouseX > Main.main.width - 120 && Main.main.mouseX < Main.main.width - 20) {
            if (Main.main.mouseY > 20 && Main.main.mouseY < 1020) {
                int itemIndex = (int) Math.floor((Main.main.mouseY - 20) / 100);
                if (inventory[itemIndex] != null)
                    inventory[itemIndex].use();
            }
        }
    }
}
