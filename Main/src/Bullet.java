public class Bullet extends Movables{
    float dir;
    
    Bullet(float rotation, float x, float y){
        super();
        dir = rotation;
        this.x = Main.player.middleX()-(float)Math.sin(dir-Math.PI/2)*Main.player.w;
        Main.println((float)Math.sin(dir-Math.PI/2),100,100);
        this.y = Main.player.middleY()+(float)Math.cos(dir-Math.PI/2)*Main.player.h;
        xSpeed = -(float)Math.sin(dir-Math.PI/2)*10;
        ySpeed = (float)Math.cos(dir-Math.PI/2)*10;
        classID = "Bullet";
    }

    @Override
    public void draw() {
        Main.main.circle(x,y,2);
    }

    @Override
    public void step() {
        x += xSpeed;
        y += ySpeed;
    }
    
}
