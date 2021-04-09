public class Player extends Movables {
    float xAcc = 2;
    float yAcc = 2;
    float friction = 0.85f;
    float sprintSpeed = 5;
    float walkSpeed = 2;
    float sneakSpeed = 1;
    Weapon cWeapon0;
    Weapon cWeapon1;
    boolean cWNumber;

    Item[] inventory = new Item[10];

    int getEmptyInventorySpace() {
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] == null)
                return i;
        }
        return -1;
    }

    boolean containsSameItemType(String itemType){
        for(int i = 0; i < inventory.length; i++){
            if (inventory[i]== null)
                break;
            if (inventory[i].itemType.equals(itemType))
                return true;
        }
        return false;
    }

    Item getItemTypeFromInventory(String itemType) {
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i].itemType.equals(itemType))
                return inventory[i];
        }
        return null;
    }

    int getItemIndexFromInventory(Item item) {
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] == item)
                return i;
        }
        return -1;
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
        cWeapon1 = new Starter(x, y);
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
        updateAngle();
        updateMove();
        updateWeapons();
        updateUseItems();
    }

    void updateAngle() {
        rotation = GameMath.pointAngle(middleX(), middleY(), Main.getMouseX(), Main.getMouseY());
    }

    void updateMove() {
        float cMaxSpeed;
        if (Main.keyDown(-1/* SHIFT */)) {
            cMaxSpeed = sprintSpeed;
        } else if (Main.keyDown('c'/* CONTROL */)) {
            cMaxSpeed = sneakSpeed;
        } else {
            cMaxSpeed = walkSpeed;
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

    public Weapon getWeapon() {
        if (!cWNumber) {
            return cWeapon0;
        } else {
            return cWeapon1;
        }

    }

    void updateWeapons() {
        if (Main.keyTapped('1') && cWeapon0 != null) {
            cWNumber = false;
        } else if (Main.keyTapped('2') && cWeapon1 != null) {
            cWNumber = true;
        }
        if (Main.mousePressed && getWeapon().cooldown > getWeapon().shotCooldown && getWeapon().cClip > 0) {
            getWeapon().use();
            getWeapon().cClip -= 1;
            getWeapon().cooldown = 0;
        } else {
            getWeapon().cooldown += 1;
        }
        if (Main.keyTapped('r') && getWeapon().cClip != getWeapon().clipSize) {
            if(Main.player.containsSameItemType(getWeapon().ammoType)){
                Item oldItem = Main.player.getItemTypeFromInventory(getWeapon().ammoType);

                if (oldItem.amount > (getWeapon().clipSize-getWeapon().cClip)){
                    oldItem.amount -= (getWeapon().clipSize-getWeapon().cClip);
                    getWeapon().cClip = getWeapon().clipSize;
                }else if (oldItem.amount != 0){
                    getWeapon().cClip += oldItem.amount;
                    oldItem.amount = 0;
                }
                
                
            }
            

        }

    }

    int timeSinceLastWalkSound = 0;
    int timePerWalkSound = 10;

    void makeSound() {
        timeSinceLastWalkSound++;
        if (Math.floor(speed()) == 0)
            return;
        if (Main.keyDown(-1) && timeSinceLastWalkSound > timePerWalkSound) {
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
