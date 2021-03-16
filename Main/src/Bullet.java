

public class Bullet extends Movables{
    float dir;
    float xDir;
    float yDir;
    float damage = 10;
    float range = 400;
    float xEnd;
    float yEnd;
    
    Bullet(float rotation, float x, float y){
        super();
        dir = rotation;
        this.x = Main.player.middleX()-(float)Math.sin(dir-Math.PI/2)*Main.player.w;
        this.y = Main.player.middleY()+(float)Math.cos(dir-Math.PI/2)*Main.player.h;
        xDir = -(float)Math.sin(dir-Math.PI/2);
        yDir = (float)Math.cos(dir-Math.PI/2);
        classID = "Bullet";
        checkCollide();
    }

    @Override
    public void draw() {
        Main.main.line(x,y,xEnd,yEnd);
    }

    @Override
    public void step() {

        
    }

    void checkCollide(){
        LineCollisionData data = new LineCollisionData(false, 0, 0);
        for (int i = 0; i < Main.nearObjects.size();i++){
            GameObject object = Main.nearObjects.get(i);
            
            if(object.classID == "Wall"||object.classID=="Zombie"){
                data = GameMath.lineRect(x,y,x+xDir*range,y+yDir*range,object.middleX(),object.middleY(),object.w,object.h);
                if(GameMath.objectDistance(Main.player, object)<GameMath.pointDistance(Main.player.middleX(), Main.player.middleY(), data.xCol, data.yCol))
                data = GameMath.lineRect(x,y,x+xDir*range,y+yDir*range,object.middleX(),object.middleY(),object.w,object.h);
                System.out.println(GameMath.objectDistance(Main.player, object) + " < " + GameMath.pointDistance(Main.player.middleX(), Main.player.middleY(), data.xCol, data.yCol));
                
            }
            
        }
        if (data.hasCollided){
            xEnd = data.xCol;
            yEnd = data.yCol;
        }
    }

    
}
