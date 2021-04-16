package MapGeneration;

import GameObjects.Wall;
import Setup.Main;

public class Node {
    public int x, y;
    public Node parent;
    public Node parent2 = null;
    //   North/Up, South/Down, East/Right, West/Left
    public Node[] connected  = {null, null, null, null};
    public Boolean isEndPoint;
    public Boolean hasHouse = false;
    public static final int roadWidth = 300;//BASED ON THE PLAYER WIDTH AND HEIGHT IS TO BE REPLACED ANYWAYS
    public static final int wallWidth = 50;
    public static final int houseDepth = 500;
    public Boolean isINTERSECTIONPOINT = false;
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
            nodeIndex = Map.SOUTH;
        }else if(y-n.y > 0){
            nodeIndex = Map.NORTH;
        }else if(x-n.x < 0){
            nodeIndex = Map.EAST;
        }else{//else if(x-n.x < 0)
            nodeIndex = Map.WEST;
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
        }else if(isINTERSECTIONPOINT){
            Main.main.fill(255, 255, 255);
        }
        Main.main.circle(x, y, 10);
        Main.main.popMatrix();
    }
    private static int boolToInt(Boolean b) { //WHY DOES THIS NOT EXIST IN THE STD LIB????
        return b ? 1 : 0;
    }
    //Houses on vertical roads will give way to horizontal ones :), very cool
    public void housesAlongParentEdge(){
        hasHouse = true; // IDK IF IT SAFE TO PUT IT HERE BUT I THINK IT IS
        //Parent is to the EAST
        if (parent == connected[Map.EAST]){
            //THEN A HOUSE SHOULD BE TO THE NORTH OF THE ROAD
            int x1North = x + roadWidth/2 * boolToInt(connected[Map.NORTH] != null);
            int x2North = parent.x - roadWidth/2 * boolToInt(parent.connected[Map.NORTH] != null);
            new Building(x1North, parent.y-roadWidth/2-houseDepth, x2North, parent.y-roadWidth/2-houseDepth, x1North, parent.y-roadWidth/2, x2North, parent.y-roadWidth/2, Map.SOUTH);
            
            //AND ONE TO THE SOUTH OF THE ROAD
            int x1South = x + roadWidth/2 * boolToInt(connected[Map.SOUTH] != null);
            int x2South = parent.x - roadWidth/2 * boolToInt(parent.connected[Map.SOUTH] != null);
            new Building(x1South, parent.y+roadWidth/2, x2South, parent.y+roadWidth/2, x1South, parent.y+roadWidth/2+houseDepth, x2South, parent.y+roadWidth/2+houseDepth, Map.NORTH);
        }else if (parent == connected[Map.WEST]){
            //THEN A HOUSE SHOULD BE TO THE NORTH OF THE ROAD
            int x1North = parent.x + roadWidth/2 * boolToInt(parent.connected[Map.NORTH] != null);
            int x2North = x - roadWidth/2 * boolToInt(connected[Map.NORTH] != null);
            new Building(x1North, parent.y-roadWidth/2-houseDepth, x2North, parent.y-roadWidth/2-houseDepth, x1North, parent.y-roadWidth/2, x2North, parent.y-roadWidth/2, Map.SOUTH);
            //AND ONE TO THE SOUTH OF THE ROAD
            int x1South = parent.x + roadWidth/2 * boolToInt(parent.connected[Map.SOUTH] != null);
            int x2South = x - roadWidth/2 * boolToInt(connected[Map.SOUTH] != null);
            new Building(x1South, parent.y+roadWidth/2, x2South, parent.y+roadWidth/2, x1South, parent.y+roadWidth/2+houseDepth, x2South, parent.y+roadWidth/2+houseDepth, Map.NORTH);
        }else if(parent == connected[Map.SOUTH]){
            //FOR THE VERTICAL MOTHERFUCKERS WE WILL AUTOMATICALLY REMOVE 1 HOUSE DEPTH
            int y1West = y + (roadWidth/2 + houseDepth) * boolToInt(connected[Map.WEST] != null);
            int y2West = parent.y - (roadWidth/2 + houseDepth) * boolToInt(parent.connected[Map.WEST] != null);
            new Building(parent.x-roadWidth/2-houseDepth, y1West, parent.x-roadWidth/2, y1West, parent.x-roadWidth/2-houseDepth, y2West, parent.x-roadWidth/2, y2West, Map.EAST);

            int y1East = y + (roadWidth/2 + houseDepth) * boolToInt(connected[Map.EAST] != null);
            int y2East = parent.y - (roadWidth/2 + houseDepth) * boolToInt(parent.connected[Map.EAST] != null);
            new Building(parent.x+roadWidth/2, y1East, parent.x+roadWidth/2+houseDepth, y1East, parent.x+roadWidth/2, y2East, parent.x+roadWidth/2+houseDepth, y2East, Map.WEST);
        }else if(parent == connected[Map.NORTH]){
            int y1West = parent.y + (roadWidth/2 + houseDepth) * boolToInt(parent.connected[Map.WEST] != null);
            int y2West = y - (roadWidth/2 + houseDepth) * boolToInt(connected[Map.WEST] != null);
            new Building(parent.x-roadWidth/2-houseDepth, y1West, parent.x-roadWidth/2, y1West, parent.x-roadWidth/2-houseDepth, y2West, parent.x-roadWidth/2, y2West, Map.EAST);

            int y1East = parent.y + (roadWidth/2 + houseDepth) * boolToInt(parent.connected[Map.EAST] != null);
            int y2East = y - (roadWidth/2 + houseDepth) * boolToInt(connected[Map.EAST] != null);
            new Building(parent.x+roadWidth/2, y1East, parent.x+roadWidth/2+houseDepth, y1East, parent.x+roadWidth/2, y2East, parent.x+roadWidth/2+houseDepth, y2East, Map.WEST);
        }
    }

    public void wallsAlongParentEdge(){ // THE WALLS ARE AUTOMATICALLY ADDED
        //Parent is to the EAST
        if (parent == connected[Map.EAST]){
            //System.out.println("OEST");
            //virker ikke helt endnu
            //housesAlongParentEdge();
            
            int x1North = x + roadWidth/2 * boolToInt(connected[Map.NORTH] != null);
            int x2North = parent.x - roadWidth/2 * boolToInt(parent.connected[Map.NORTH] != null);
            new Wall(x1North, y-roadWidth/2, x2North, parent.y-roadWidth/2, wallWidth);
        
            int x1South = x + roadWidth/2 * boolToInt(connected[Map.SOUTH] != null);
            int x2South = parent.x - roadWidth/2 * boolToInt(parent.connected[Map.SOUTH] != null);
            new Wall(x1South, y+roadWidth/2, x2South, parent.y+roadWidth/2, wallWidth);
        }else if (parent == connected[Map.WEST]){
            //System.out.println("VEST");
            int x1North = parent.x + roadWidth/2 * boolToInt(parent.connected[Map.NORTH] != null);
            int x2North = x - roadWidth/2 * boolToInt(connected[Map.NORTH] != null);
            new Wall(x1North, y-roadWidth/2, x2North, parent.y-roadWidth/2, wallWidth);
        
            int x1South = parent.x + roadWidth/2 * boolToInt(parent.connected[Map.SOUTH] != null);
            int x2South = x - roadWidth/2 * boolToInt(connected[Map.SOUTH] != null);
            new Wall(x1South, y+roadWidth/2, x2South, parent.y+roadWidth/2, wallWidth);
        }
        //Parent is to the SOUTH
        else if (parent == connected[Map.SOUTH]){
            //System.out.println("SYD");
            //virker ikke helt endnu
            int y1West = y + roadWidth/2 * boolToInt(connected[Map.WEST] != null);
            int y2West = parent.y - roadWidth/2 * boolToInt(parent.connected[Map.WEST] != null);
            new Wall(x-roadWidth/2, y1West, parent.x-roadWidth/2, y2West, wallWidth);

            int y1East = y + roadWidth/2 * boolToInt(connected[Map.EAST] != null);
            int y2East = parent.y - roadWidth/2 * boolToInt(parent.connected[Map.EAST] != null);
            new Wall(x+roadWidth/2, y1East, parent.x+roadWidth/2, y2East, wallWidth);
        }else if (parent == connected[Map.NORTH]){
           // System.out.println("NORD");
            //virker ikke helt endnu
            int y1West = parent.y + roadWidth/2 * boolToInt(parent.connected[Map.WEST] != null);
            int y2West = y - roadWidth/2 * boolToInt(connected[Map.WEST] != null);
            new Wall(x-roadWidth/2, y1West, parent.x-roadWidth/2, y2West, wallWidth);

            int y1East = parent.y + roadWidth/2 * boolToInt(parent.connected[Map.EAST] != null);
            int y2East = y - roadWidth/2 * boolToInt(connected[Map.EAST] != null);
            new Wall(x+roadWidth/2, y1East, parent.x+roadWidth/2, y2East, wallWidth);
        }
        //Try to generate for parent22 if it exists
        if(parent2 != null){
                //parent2 is to the EAST
            if (parent2 == connected[Map.EAST]){
                //System.out.println("OEST");
                //virker ikke helt endnu
                int x1North = x + roadWidth/2 * boolToInt(connected[Map.NORTH] != null);
                int x2North = parent2.x - roadWidth/2 * boolToInt(parent2.connected[Map.NORTH] != null);
                new Wall(x1North, y-roadWidth/2, x2North, parent2.y-roadWidth/2, wallWidth);

                int x1South = x + roadWidth/2 * boolToInt(connected[Map.SOUTH] != null);
                int x2South = parent2.x - roadWidth/2 * boolToInt(parent2.connected[Map.SOUTH] != null);
                new Wall(x1South, y+roadWidth/2, x2South, parent2.y+roadWidth/2, wallWidth);

            }else if (parent2 == connected[Map.WEST]){
                //System.out.println("VEST");
                int x1North = parent2.x + roadWidth/2 * boolToInt(parent2.connected[Map.NORTH] != null);
                int x2North = x - roadWidth/2 * boolToInt(connected[Map.NORTH] != null);
                new Wall(x1North, y-roadWidth/2, x2North, parent2.y-roadWidth/2, wallWidth);

                int x1South = parent2.x + roadWidth/2 * boolToInt(parent2.connected[Map.SOUTH] != null);
                int x2South = x - roadWidth/2 * boolToInt(connected[Map.SOUTH] != null);
                new Wall(x1South, y+roadWidth/2, x2South, parent2.y+roadWidth/2, wallWidth);
            }
            //parent2 is to the SOUTH
            else if (parent2 == connected[Map.SOUTH]){
                //System.out.println("SYD");
                //virker ikke helt endnu
                int y1West = y + roadWidth/2 * boolToInt(connected[Map.WEST] != null);
                int y2West = parent2.y - roadWidth/2 * boolToInt(parent2.connected[Map.WEST] != null);
                new Wall(x-roadWidth/2, y1West, parent2.x-roadWidth/2, y2West, wallWidth);

                int y1East = y + roadWidth/2 * boolToInt(connected[Map.EAST] != null);
                int y2East = parent2.y - roadWidth/2 * boolToInt(parent2.connected[Map.EAST] != null);
                new Wall(x+roadWidth/2, y1East, parent2.x+roadWidth/2, y2East, wallWidth);
            }else if (parent2 == connected[Map.NORTH]){
                //System.out.println("NORD");
                ////virker ikke helt endnu
                int y1West = parent2.y + roadWidth/2 * boolToInt(parent2.connected[Map.WEST] != null);
                int y2West = y - roadWidth/2 * boolToInt(connected[Map.WEST] != null);
                new Wall(x-roadWidth/2, y1West, parent2.x-roadWidth/2, y2West, wallWidth);

                int y1East = parent2.y + roadWidth/2 * boolToInt(parent2.connected[Map.EAST] != null);
                int y2East = y - roadWidth/2 * boolToInt(connected[Map.EAST] != null);
                new Wall(x+roadWidth/2, y1East, parent2.x+roadWidth/2, y2East, wallWidth);
            }
        }
    }
}