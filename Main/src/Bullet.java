public class Bullet extends Movables{
    float dir;

    Bullet(float rotation, float x, float y){
        super();
        dir = rotation;
        this.x = x;
        this.y = y;
        xSpeed = (float)Math.sin(dir);
        ySpeed = (float)Math.cos(dir);
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
