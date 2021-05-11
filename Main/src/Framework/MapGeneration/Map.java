package Framework.MapGeneration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import Setup.Main;
import Framework.*;

public class Map implements java.io.Serializable {
    //                                        north,   south, east, west
    private static final int[][] directions = {{0,-1}, {0,1}, {1,0}, {-1,0}};
    public static final int NORTH = 0;
    public static final int SOUTH = 1;
    public static final int EAST  = 2;
    public static final int WEST  = 3;
    public Node initialNode;
    public ArrayList<Node> allNodes;
    public ArrayList<Node> endNodes;
    private Random rand = new Random();
    private int maxLevel;
    private final int maxRoadLength = Node.houseDepth*5+Node.roadWidth;
    private final int minRoadLength = Node.houseDepth*4+Node.roadWidth+1; //BECAUSE AT LEAST ONE HOUSE SHOULD BE ON EITHER SIDE OF THE VERTICAL ROADS
    private final float minPointDist = Node.houseDepth*3+Node.roadWidth;
    private final float minRoadDist = Node.houseDepth*3+Node.roadWidth;

    public Map(int maxLevel){
        initialNode = new Node(Main.main.width/2, Main.main.height/2);
        allNodes = new ArrayList<Node>();
        endNodes = new ArrayList<Node>();
        this.maxLevel = maxLevel;
    }

    public void generateMap(){
        Node north = new Node(initialNode.x+directions[NORTH][0]*randomRoadLength(), initialNode.y+directions[NORTH][1]*randomRoadLength(), initialNode);
        Node south = new Node(initialNode.x+directions[SOUTH][0]*randomRoadLength(), initialNode.y+directions[SOUTH][1]*randomRoadLength(), initialNode);
        Node east  = new Node(initialNode.x+directions[EAST][0]*randomRoadLength(), initialNode.y+directions[EAST][1]*randomRoadLength(), initialNode);
        Node west  = new Node(initialNode.x+directions[WEST][0]*randomRoadLength(), initialNode.y+directions[WEST][1]*randomRoadLength(), initialNode);
        ArrayList<Node> startNodes = new ArrayList<Node>(Arrays.asList(north, south, east, west));
        for (Node n : startNodes) {
            allNodes.add(n);
            initialNode.addNode(n);
        }
        updateEndNodes();
        for(int curLevel = 1; curLevel < maxLevel; curLevel++){        
            for (Node endNode : endNodes) {
                // Generate either 1, 2 or 3 new nodes at each end node
                generateNodesAtNode(endNode);
            }
            updateEndNodes();
        }
    }
    public void generateNodesAtNode(Node n){
        //Generate one or three adjacent nodes, check for intersection, how should they be handled? possibly just make new node at the intersection and replace the two nodes that made the intersection happen.
        //Generating 1-3 adjacent nodes.
        int[] deltaCoorParents = {(n.parent.x-n.x), (n.parent.y-n.y)};
        int[] directionIndexes;// Arrays.copyOf(directions, directions.length);
        //They can not be in the opposite direction of the respective two previous parents
        //PARENT IS TO THE NORTH
        if(deltaCoorParents[1] < 0){
            directionIndexes = new int[]{SOUTH, EAST, WEST};
        //South
        }else if(deltaCoorParents[1] > 0){
            directionIndexes = new int[]{NORTH, EAST, WEST};
        //PARENT IS TO THE WEST
        }else if(deltaCoorParents[0] < 0){
            directionIndexes = new int[]{NORTH, SOUTH, EAST};
        //PARENT IS TO THE EAST
        }else{ //if (deltaCoorParents[0] > 0){
            directionIndexes = new int[]{NORTH, SOUTH, WEST};
        }
        int numberOfNewNodes = 1;//1+rand.nextInt(3);
        //System.out.println("NUMBER OF NEW NODES: "+numberOfNewNodes+"");
        Node[] nodesIntersectionCheck = {null, null, null};
        for(int x = 0; x < numberOfNewNodes; x++){
            int randomDirectionIndex = rand.nextInt(directionIndexes.length);
            //CREATING THE NEW NODE WITH THE GIVEN DIRECTION
            int roadLength = randomRoadLength();
            int[] direction = directions[directionIndexes[randomDirectionIndex]];
            Node generatedNode = new Node(n.x+(roadLength*direction[0]), n.y+(roadLength*direction[1]), n);
            
            //Check whether the node is too close to any of the others, if it is it shouldn't be included
            if(createNode(generatedNode)){          
                //Adding the new node to the things.
                allNodes.add(generatedNode);
                n.addNode(generatedNode);
                nodesIntersectionCheck[x] = generatedNode;
            }

            //REMOVING THE DIRECTION INDEX, BY JUST MAKING A NEW ARRAY NOT CONTAINING THE ELEMENT
            if(x < 2){
                //CHANGE SO IT USES AN ARRAY INSTEAD OF ARRAYLIST
                ArrayList<Integer> intermediateIndexes = new ArrayList<Integer>();
                for(int i = 0; i < directionIndexes.length; i++){
                    if(directionIndexes[i] != directionIndexes[randomDirectionIndex]){
                        intermediateIndexes.add(directionIndexes[i]);
                    }
                }
                //this just takes the arraylist of Integers and turns it into an int array
                directionIndexes = intermediateIndexes.stream().mapToInt(i->i).toArray();;
            }
        }

        ArrayList<Node> nodesToBeAdded = new ArrayList<Node>();
        //Check for intersection
        for(Node n1 : nodesIntersectionCheck){
            if(n1 != null){
            int dx = n1.parent.x - n1.x;
            int dy = n1.parent.y - n1.y;
            for(Node n2 : allNodes){
                if(n2 != n1){
                    int dx2 = n2.parent.x - n2.x;
                    int dy2 = n2.parent.y - n2.y;
                    Boolean xWithin = ((n1.x > n2.x && n1.x < n2.parent.x) || (n1.x < n2.x && n1.x > n2.parent.x));
                    Boolean yOut = ((n2.y > n1.y && n2.y < n1.parent.y) || (n2.y < n1.y && n2.y > n1.parent.y)); // or actually rather that n2.y is within the span of the n1.y and its parent's y-coordinate as well
                   
                    Boolean yWithin = ((n1.y > n2.y && n1.y < n2.parent.y) || (n1.y < n2.y && n1.y > n2.parent.y));
                    Boolean xOut = ((n2.x > n1.x && n2.x < n1.parent.x) || (n2.x < n1.x && n2.x > n1.parent.x));
                   
                    if(dx == 0 && dx2 != 0 && xWithin && yOut){
                        //System.out.println("INTERSECTIONNODE YYYYYYY");
                        //Handle this intersection
                        //the new node should n1.x and n2.y
                        Node intersectionNode = new Node(n1.x, n2.y, n1.parent);
                        intersectionNode.isINTERSECTIONPOINT = true;
                        intersectionNode.addOtherParent(n2.parent);
                        //if(createNode(intersectionNode)){
                            intersectionNode.addNode(n1); //The parent is connected in the constructor.
                            intersectionNode.addNode(n2);

                            n1.parent.connectNode(intersectionNode);
                            n1.parent = intersectionNode;
                            n1.connectNode(intersectionNode);

                            n2.parent.connectNode(intersectionNode);
                            n2.parent = intersectionNode;
                            n2.connectNode(intersectionNode);
                            
                            nodesToBeAdded.add(intersectionNode);
                        //}

                    }else if(dy == 0 && dy2 != 0 && yWithin && xOut){
                        //System.out.println("INTERSECTIONNODE XXXXXXXX");
                        //Handle this intersection
                        //the new node should n1.y and n2.x
                        Node intersectionNode = new Node(n2.x, n1.y, n1.parent);
                        intersectionNode.isINTERSECTIONPOINT = true;
                        intersectionNode.addOtherParent(n2.parent);
                        //if(createNode(intersectionNode)){
                            intersectionNode.addNode(n1); //The parent is connected in the constructor.
                            intersectionNode.addNode(n2);
                            
                            n1.parent.connectNode(intersectionNode);
                            n1.parent = intersectionNode;
                            n1.connectNode(intersectionNode);

                            n2.parent.connectNode(intersectionNode);
                            n2.parent = intersectionNode;
                            n2.connectNode(intersectionNode);

                            nodesToBeAdded.add(intersectionNode);//MAYBE WE CAN NOT ACTUALLY WAIT?
                        //}
                    }
                }
            }
            }
        }
        allNodes.addAll(nodesToBeAdded);
    }
    //THIS MAY BE WRONG?
    //If this is wrong one would see overlapping roads.
    private Boolean createNode(Node generatedNode){
        Boolean createNode = true;
        for(Node n0 : allNodes){
            float pointDist = GameMath.pointDistance(n0.x, n0.y, generatedNode.x, generatedNode.y);
            //Check first for the distance between each node
            if(pointDist <= minPointDist){
                //System.out.println("POINT DISTANCE --- NODE NOT CREATED");
                createNode = false;
                break; // The node shouldn't be created
            //Secondly check the distance between each node.
            }else{
                //Is the road n0 makes with its parent
                //parallel?
                Boolean n0North    = n0.parent == n0.connected[NORTH];
                Boolean gNorth     = generatedNode.parent == generatedNode.connected[NORTH];
                Boolean bothNorth  = n0North && gNorth;
                Boolean n0South    = n0.parent == n0.connected[SOUTH];
                Boolean gSouth     = generatedNode.parent == generatedNode.connected[SOUTH];
                Boolean bothSouth  = n0South && gSouth;
                Boolean northSouth = (n0North && gSouth) || (n0South && gNorth);
                Boolean vertical   = (bothNorth || bothSouth || northSouth );
                
                Boolean n0East     = n0.parent == n0.connected[EAST];
                Boolean gEast      = generatedNode.parent == generatedNode.connected[EAST];
                Boolean bothEast   = n0East && gEast;
                Boolean n0West     = n0.parent == n0.connected[WEST];
                Boolean gWest      = generatedNode.parent == generatedNode.connected[WEST];
                Boolean bothWest   = n0West && gWest;
                Boolean eastWest   = (n0East && gWest) || (n0West && gEast);
                Boolean horizontal = (bothEast || bothWest || eastWest);
        
                if(vertical){
                    //Within
                    //Boolean yWithout = (n0.y < generatedNode.y && n0.y < generatedNode.parent.y && n0.parent.y < generatedNode.y && n0.parent.y < generatedNode.parent.y) || (n0.y > generatedNode.y && n0.y > generatedNode.parent.y && n0.parent.y > generatedNode.y && n0.parent.y > generatedNode.parent.y);
                    //Boolean yWithin = (generatedNode.parent.y < n0.y < generatedNode.y ||generatedNode.parent.y > n0.y > generatedNode.y || generatedNode.parent.y < n0.parent.y < generatedNode.y || generatedNode.parent.y > n0.parent.y > generatedNode.y);
                    Boolean yWithin = (generatedNode.parent.y < n0.y && n0.y < generatedNode.y ||generatedNode.parent.y > n0.y & n0.y > generatedNode.y || generatedNode.parent.y < n0.parent.y && n0.parent.y < generatedNode.y || generatedNode.parent.y > n0.parent.y && n0.parent.y > generatedNode.y);
                    int dx = Math.abs(n0.x - generatedNode.x);
                    if(yWithin && dx < minRoadDist){
                        //System.out.println("ROAD DISTANCE VERTICAL --- NODE NOT CREATED");
                        createNode = false;
                        break;
                    }
                }else if(horizontal){
                    //Boolean xWithin = n0.x || n0.parent.x should be within generatedNode.x and generatedNode.parent.x
                    Boolean xWithin = (generatedNode.parent.x < n0.x && n0.x < generatedNode.x ||generatedNode.parent.x > n0.x & n0.x > generatedNode.x || generatedNode.parent.x < n0.parent.x && n0.parent.x < generatedNode.x || generatedNode.parent.x > n0.parent.x && n0.parent.x > generatedNode.x);
                    //Boolean xWithout = (n0.x < generatedNode.x && n0.x < generatedNode.parent.x && n0.parent.x < generatedNode.x && n0.parent.x < generatedNode.parent.x) || (n0.x > generatedNode.x && n0.x > generatedNode.parent.x && n0.parent.x > generatedNode.x && n0.parent.x > generatedNode.parent.x);
                    int dy = Math.abs(n0.y - generatedNode.y);
                    if(xWithin && dy < minRoadDist){
                        //System.out.println("ROAD DISTANCE HORIZONTAL --- NODE NOT CREATED");
                        createNode = false;
                        break;
                    }
                }else{
                    //If they are not perpendicular check the distance from one of the points to the other line.
                    //check dist form generatedNode to n0's line with parent
                    //Is generatedNode vertical
                    if(gSouth || gNorth){
                        //We know that n0 and its parent will be horizontal so
                        //Is the generatedNode's x within n0 and parent?
                        if((n0.x < generatedNode.x &&  generatedNode.x < n0.parent.x) || (n0.x > generatedNode.x &&  generatedNode.x > n0.parent.x)){
                            int dy = Math.min(Math.abs(n0.y-generatedNode.y), Math.abs(n0.parent.y-generatedNode.y));
                            if(dy < minRoadDist){
                                //System.out.println("POINT TO ROAD DISTANCE TOO SMALL --- NODE NOT CREATED");
                                createNode = false;
                                break;
                            }
                        }
                    }
                    //Is generatedNode horizontal
                    else if(gEast || gWest){
                        if((n0.y < generatedNode.y &&  generatedNode.y < n0.parent.y) || (n0.y > generatedNode.y &&  generatedNode.y > n0.parent.y)){
                            int dx = Math.min(Math.abs(n0.x-generatedNode.x), Math.abs(n0.parent.x-generatedNode.x));
                            if(dx < minRoadDist){
                                //System.out.println("POINT TO ROAD DISTANCE TOO SMALL --- NODE NOT CREATED");

                                createNode = false;
                                break;
                            }
                        }
                    }
                    //check dist form n0 to generatedNode's line with parent
                    //Is n0 vertical?
                    if(n0South || n0North){
                        //We know that n0 and its parent will be horizontal so
                        //Is the generatedNode's x within n0 and parent?
                        if((generatedNode.x < n0.x &&  n0.x < generatedNode.parent.x) || (generatedNode.x > n0.x &&  n0.x > generatedNode.parent.x)){
                            //System.out.println("POINT TO ROAD DISTANCE TOO SMALL --- NODE NOT CREATED");

                            int dy = Math.min(Math.abs(n0.y-generatedNode.y), Math.abs(n0.y-generatedNode.parent.y));
                            if(dy < minRoadDist){
                                createNode = false;
                                break;
                            }
                        }
                    }
                    //Is generatedNode horizontal
                    else if(n0East || n0West){
                        if((generatedNode.y < n0.y &&  n0.y < generatedNode.parent.y) || (generatedNode.y > n0.y &&  n0.y > generatedNode.parent.y)){
                            //System.out.println("POINT TO ROAD DISTANCE TOO SMALL --- NODE NOT CREATED");

                            int dx = Math.min(Math.abs(n0.x-generatedNode.x), Math.abs(n0.x-generatedNode.parent.x));
                            if(dx < minRoadDist){
                                createNode = false;
                                break;
                            }
                        }
                    }
                }
            }
        }
        return createNode;
    }

    private int randomRoadLength(){
        return minRoadLength + rand.nextInt(maxRoadLength - minRoadLength);
    }
    public void updateEndNodes(){
        endNodes = new ArrayList<Node>();
        for(Node node : allNodes){
            if(node.isEndPoint){
                endNodes.add(node);
            }
        }
    }
    public void removeUselessNodes(){ // MAKE A FUNCTION FOR JUST REMOVING ONE NODE?
        ArrayList<Node> remNodes = new ArrayList<Node>();
        for(Node n : allNodes){
            if(!n.isEndPoint){
                //HORIZONTAL
                if(n.connected[NORTH] == null && n.connected[SOUTH] == null){
                    //If parent is to the east
                    if(n.parent == n.connected[EAST]){
                        n.parent.connectNode(n.connected[WEST]);
                        n.connected[WEST].parent = n.parent;
                        n.connected[WEST].connectNode(n.parent);
                        remNodes.add(n);
                    //If parent is to the west
                    }else if(n.parent == n.connected[WEST]){
                        n.parent.connectNode(n.connected[EAST]);
                        n.connected[EAST].parent = n.parent;
                        n.connected[EAST].connectNode(n.parent);
                        remNodes.add(n);
                    }
                }
                //VERTICAL
                if(n.connected[EAST] == null && n.connected[WEST] == null){
                    //If parent is to the North
                    if(n.parent == n.connected[NORTH]){
                        n.parent.connectNode(n.connected[SOUTH]);
                        n.connected[SOUTH].parent = n.parent;
                        n.connected[SOUTH].connectNode(n.parent);
                        remNodes.add(n);
                    //If parent is to the south
                    }else if(n.parent == n.connected[SOUTH]){
                        n.parent.connectNode(n.connected[NORTH]);
                        n.connected[NORTH].parent = n.parent;
                        n.connected[NORTH].connectNode(n.parent);
                        remNodes.add(n);
                    }
                }
            }
        
        }
        allNodes.removeAll(remNodes);
    }
    public void draw(){
        for(Node n : allNodes){
            n.draw();
        }
        //DRAWING A YELLOW SQUARE AT THE PLAYERS CURRENT NODE
        Main.main.fill(255, 255, 0);
        Main.main.rect(Main.player.currentNode.x-40, Main.player.currentNode.y-40, 80, 80);
        
    }
}