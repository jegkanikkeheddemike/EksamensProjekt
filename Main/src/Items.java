

import processing.core.*;

public class Items extends GameObject {
    PImage sprite;
    Boolean held = false;
    Items(){
        super();
        this.classID = "GroundItems";
    }
    @Override
    public void step() {
        if(GameMath.pointDistance(this.x,this.y,Main.player.x,Main.player.y)<this.h){
            reactPickedUp();
            held = true;
        }
        if (held){
            x = Main.player.x;
            y = Main.player.y;
        }
        
    }
    public void draw(){
        
        if (!held)
        Main.main.image(sprite,x,y,w,h);

    }

    public void reactPickedUp(){
    }



}


class Weapon extends Items {
    String wpnType;
    float damage;
    float fireRate;
    float range;
    float spread;

    Weapon(float x, float y,String wpnType){
        super();
        this.wpnType = wpnType;
        classID = "Weapon";
        this.x=x;
        this.y=y;
        this.sprite = Main.main.loadImage("Data/Images/"+this.wpnType+".png");
        this.w=(float)sprite.width/3;
        this.h=(float)sprite.height/3;
    }
    public void reactPickedUp(){
        if(!Main.player.cWNumber){
            Main.player.cWeapon0 = this;
        }else{
            Main.player.cWeapon1 = this;
        }

        Main.toBeDelted.add(this);
    }
    public void reactShoot(){
    }
}

class Starter extends Weapon{
    public Starter(float x, float y) {
        super(x, y, "Starter");
        this.wpnType = "Starter";
        damage = 5;
        fireRate = 1;
        range = 300;
        spread = 0.05f;
        held = true;
    }
    public void reactShoot(){
        new Bullet(Main.player.rotation);
    }
}

class Pistol extends Weapon{
    public Pistol(float x, float y) {
        super(x, y, "Pistol");
        this.wpnType = "Pistol";
        damage = 5;
        fireRate = 1;
        range = 700;
        spread = 0.02f;
    }
    public void reactShoot(){
        new Bullet(Main.player.rotation);
    }
}




