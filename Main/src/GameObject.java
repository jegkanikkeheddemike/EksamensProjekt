public abstract class GameObject {
    private static int idCounter = 0;
    public float x, y, w, h;
    public int ID;
    public String classID = "NO CLASS ID PLZ FIX";

    protected GameObject() {
        this.ID = idCounter;
        idCounter++;
        Main.toBeCreated.add(this);
    }

    public void draw(){
        drawBorder();
    }
    public abstract void step();

    public void drawBorder(){
        Main.main.noFill();
        Main.main.stroke(255,0,0);
        Main.main.strokeWeight(3);
        Main.main.rect(x,y,w,h);
    }

    public float middleX() {
        return x + w / 2;
    }

    public float middleY() {
        return y + h / 2;
    }

    public void delete() {
        Main.toBeDelted.add(this);
    }


}
