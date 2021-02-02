import java.util.ArrayList;
import java.awt.*;

public class Node {
    private static final int r = 10;
    private Color c;
    public int x, y;
    public int level;
    public Node parent;
    public ArrayList<Node> connected = new ArrayList<Node>();
    public boolean endPoint = true;
    Node(int x, int y, Node parent, Color c){
        this.c = c; 
        this.x = x;
        this.y = y;
        this.level = parent.level + 1;
        this.parent = parent;
        connected.add(parent);
    }
    Node(int x, int y, Node parent){
        this.c = parent.c; 
        this.x = x;
        this.y = y;
        this.level = parent.level + 1;
        this.parent = parent;
        connected.add(parent);
    }
    //For the first node
    Node(int x, int y){
        this.c = Color.blue;
        this.x = x;
        this.y = y;
        this.level = 0;
    }
    //Should we return the node as well just to make it easier to draw and such
    public void addNode(Node child){
        endPoint = false;
        connected.add(child);
    }
    public void draw(Graphics g){
        g.setColor(c);
        g.fillOval(x-r, y-r,r*2,r*2);
        g.setColor(Color.BLACK);
        g.drawString(level+"", x, y);
        g.setColor(c);
        for (Node node : connected) {
            if(node != parent){
                g.drawLine(x, y, node.x, node.y);
            }
        }
        //This maybe shouldn't be there and all should be drawn from the other one but what the hell
        /*for (Node node : connected) {
            if(node != parent){
                node.draw(g);
            }
        }*/
    }
    
}
