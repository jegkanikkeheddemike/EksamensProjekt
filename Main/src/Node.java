

public class Node {
    public int x, y;
    public Node parent;
    public Node parent2 = null;
    //   North/Up, South/Down, East/Right, West/Left
    public Node[] connected  = {null, null, null, null};
    public Boolean isEndPoint;
    //INITIAL
    Node(int x, int y){
        this.parent = this;
        this.x = x;
        this.y = y;
        this.isEndPoint = false;
    }
    //with parent
    Node(int x, int y, Node parent){
        this.x = x;
        this.y = y;
        this.parent = parent;
        this.connectNode(parent);
        isEndPoint = true;
    }
    public void addOtherParent(Node p){
        parent2 = p;
        connectNode(p);
    }
    public void addNode(Node n){
        connectNode(n);
        isEndPoint = false;
    }
    public void connectNode(Node n){
        //Check whether the other node is n/s/e or w with the this node at set it at the appropriate place in the list.
        int nodeIndex;

        if(y-n.y < 0){
            nodeIndex = Map.NORTH;
        }else if(y-n.y > 0){
            nodeIndex = Map.SOUTH;
        }else if(x-n.x > 0){
            nodeIndex = Map.WEST;
        }else{//else if(x-n.x < 0)
            nodeIndex = Map.EAST;
        }
        connected[nodeIndex] = n;
    }
    public void draw(){
        Main.main.pushMatrix();
        Main.main.strokeWeight(5);
        Main.main.stroke(255, 255, 255, 50);
        /* DRAW ALL CONNECTIONS TO SEE IF ANY EDGE IS NOT A PARENT CONNECTION
        for(Node n : connected){
            if(n != null ){
                if(x != n.x || y != n.y){
                    Main.main.line(x, y, n.x, n.y);
                }
            }
        }*/
        Main.main.stroke(0, 0, 255);
        Main.main.line(x, y, parent.x, parent.y);
        if(parent2 != null){
            Main.main.line(x, y, parent2.x, parent2.y);    
        }
        Main.main.fill(0, 0, 255);
        if(isEndPoint){
            Main.main.fill(255, 0, 0);
        }
        Main.main.circle(x, y, 10);
        Main.main.popMatrix();
    }
}