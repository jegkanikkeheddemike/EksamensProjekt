import processing.core.*;

public class Items extends GameObject {
    PImage sprite = rect(10,10,10,10);

    Items(){
        super();

    }

    @Override
    public void step() {
        // TODO Auto-generated method stub
        
    }

    public void draw(){
    }
    
}


class Weapons extends Items {
    int damage;
    float firerate;
    String name;

    Weapons(){
        super();
        switch(name){
        case "Pistol":
            damage = 1;
            firerate = 1;
            sprite = Main.main.loadImage();

        }
    }



    @Override
    public void step() {
        // TODO Auto-generated method stub
        
    }


    
}





