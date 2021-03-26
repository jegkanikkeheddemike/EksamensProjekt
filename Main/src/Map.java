import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Map {
    //                                        north,   south, east, west
    private static final int[][] directions = {{0,1}, {0,-1}, {1,0}, {-1,0}};
    public static final int NORTH = 0;
    public static final int SOUTH = 1;
    public static final int EAST  = 2;
    public static final int WEST  = 3;
    Node initialNode;
    ArrayList<Node> allNodes;
    ArrayList<Node> endNodes; // or maybe is should be called outer nodes?
    private Random rand = new Random();
    private int maxLevel;
    private final int maxRoadLength = 300;
    private final int minRoadLength = 100;
    private final float minPointDist = 40f;
    private final float minRoadDist = 30f;

    Map(int maxLevel){
        initialNode = new Node(Main.main.width/2, Main.main.height/2);
        allNodes = new ArrayList<Node>();
        endNodes = new ArrayList<Node>();
        this.maxLevel = maxLevel;
    }

    public void generateMap(){
        Node north = new Node(initialNode.x, initialNode.y+randomRoadLength(), initialNode);
        Node south = new Node(initialNode.x, initialNode.y-randomRoadLength(), initialNode);
        Node east  = new Node(initialNode.x+randomRoadLength(), initialNode.y, initialNode);
        Node west  = new Node(initialNode.x-randomRoadLength(), initialNode.y, initialNode);
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
        //North
        if(deltaCoorParents[1] > 0){
            directionIndexes = new int[]{1,2,3};
        //South
        }else if(deltaCoorParents[1] < 0){
            directionIndexes = new int[]{0,2,3};
        //East
        }else if(deltaCoorParents[0] < 0){
            directionIndexes = new int[]{0,1,2};
        //West
        }else{ //if (deltaCoorParents[0] > 0){
            directionIndexes = new int[]{0,1,3};
        }
        int numberOfNewNodes = 1+rand.nextInt(3);
        //System.out.println("NUMBER OF NEW NODES: "+numberOfNewNodes+"");
        Node[] nodesIntersectionCheck = {null, null, null};
        for(int x = 0; x < numberOfNewNodes; x++){
            int randomDirectionIndex = rand.nextInt(directionIndexes.length);
            //CREATING THE NEW NODE WITH THE GIVEN DIRECTION
            int roadLength = randomRoadLength();
            int[] direction = directions[directionIndexes[randomDirectionIndex]];
            Node generatedNode = new Node(n.x+(roadLength*direction[0]), n.y+(roadLength*direction[1]), n);
            
            //Check whether the node is too close to any of the others, if it is it shouldn't be included
            Boolean createNode = true;
            for(Node n0 : allNodes){
                float pointDist = GameMath.pointDistance(n0.x, n0.y, generatedNode.x, generatedNode.y);
                //Check first for the distance between each node
                if(pointDist <= minPointDist){
                    System.out.println("POINT DISTANCE --- NODE NOT CREATED");
                    createNode = false;
                    break; // The node shouldn't be created
                //Secondly check the distance between each node.
                }else{
                    //Is the road n0 makes with its parent
                    //parallel?
                    Boolean bothNorth = n0.parent == n0.connected[NORTH] && generatedNode.parent == generatedNode.connected[NORTH];
                    Boolean bothSouth = n0.parent == n0.connected[SOUTH] && generatedNode.parent == generatedNode.connected[SOUTH];
                    Boolean northSouth = (n0.parent == n0.connected[NORTH] && generatedNode.parent == generatedNode.connected[SOUTH]) || (n0.parent == n0.connected[SOUTH] && generatedNode.parent == generatedNode.connected[NORTH]);
                    Boolean vertical = (bothNorth || bothSouth || northSouth );
            
                    Boolean bothEast = n0.parent == n0.connected[EAST] && generatedNode.parent == generatedNode.connected[EAST];
                    Boolean bothWest = n0.parent == n0.connected[WEST] && generatedNode.parent == generatedNode.connected[WEST];
                    Boolean eastWest = (n0.parent == n0.connected[EAST] && generatedNode.parent == generatedNode.connected[WEST]) || (n0.parent == n0.connected[WEST] && generatedNode.parent == generatedNode.connected[EAST]);
                    Boolean horizontal = (bothEast || bothWest || eastWest);
            
                    if(vertical){
                        //Within
                        //Boolean yWithout = (n0.y < generatedNode.y && n0.y < generatedNode.parent.y && n0.parent.y < generatedNode.y && n0.parent.y < generatedNode.parent.y) || (n0.y > generatedNode.y && n0.y > generatedNode.parent.y && n0.parent.y > generatedNode.y && n0.parent.y > generatedNode.parent.y);
                        //Boolean yWithin = (generatedNode.parent.y < n0.y < generatedNode.y ||generatedNode.parent.y > n0.y > generatedNode.y || generatedNode.parent.y < n0.parent.y < generatedNode.y || generatedNode.parent.y > n0.parent.y > generatedNode.y);
                        Boolean yWithin = (generatedNode.parent.y < n0.y && n0.y < generatedNode.y ||generatedNode.parent.y > n0.y & n0.y > generatedNode.y || generatedNode.parent.y < n0.parent.y && n0.parent.y < generatedNode.y || generatedNode.parent.y > n0.parent.y && n0.parent.y > generatedNode.y);
                        int dx = Math.abs(n0.x - generatedNode.x);
                        if(yWithin && dx < minRoadDist){
                            System.out.println("ROAD DISTANCE VERTICAL --- NODE NOT CREATED");
                            createNode = false;
                            break;
                        }
                    }else if(horizontal){
                        //Boolean xWithin = n0.x || n0.parent.x should be within generatedNode.x and generatedNode.parent.x
                        Boolean xWithin = (generatedNode.parent.x < n0.x && n0.x < generatedNode.x ||generatedNode.parent.x > n0.x & n0.x > generatedNode.x || generatedNode.parent.x < n0.parent.x && n0.parent.x < generatedNode.x || generatedNode.parent.x > n0.parent.x && n0.parent.x > generatedNode.x);
                        //Boolean xWithout = (n0.x < generatedNode.x && n0.x < generatedNode.parent.x && n0.parent.x < generatedNode.x && n0.parent.x < generatedNode.parent.x) || (n0.x > generatedNode.x && n0.x > generatedNode.parent.x && n0.parent.x > generatedNode.x && n0.parent.x > generatedNode.parent.x);
                        int dy = Math.abs(n0.y - generatedNode.y);
                        if(xWithin && dy < minRoadDist){
                            System.out.println("ROAD DISTANCE HORIZONTAL --- NODE NOT CREATED");
                            createNode = false;
                            break;
                        }
                    }
                }
            }

            if(createNode){          
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
            int dx = n1.x - n1.parent.x;
            int dy = n1.y - n1.parent.y;
            for(Node n2 : allNodes){
                if(n2 != n1){
                    int dx2 = n2.x-n2.parent.x;
                    int dy2 = n2.y-n2.parent.y;
                    Boolean xWithin = ((n1.x > n2.x && n1.x < n2.parent.x) || (n1.x < n2.x && n1.x > n2.parent.x));
                    Boolean yOut = ((n2.y > n1.y && n2.y < n1.parent.y) || (n2.y < n1.y && n2.y > n1.parent.y));
                   
                    Boolean yWithin = ((n1.y > n2.y && n1.y < n2.parent.y) || (n1.y < n2.y && n1.y > n2.parent.y));
                    Boolean xOut = ((n2.x > n1.x && n2.x < n1.parent.x) || (n2.x < n1.x && n2.x > n1.parent.x));
                   
                    if(dx == 0 && dx2 != 0 && xWithin && yOut){
                        //Handle this intersection
                        //the new node should n1.x and n2.y
                        Node intersectionNode = new Node(n1.x, n2.y, n1.parent);
                        intersectionNode.addOtherParent(n2.parent);
                        intersectionNode.addNode(n1); //The parent is connected in the constructor.
                        intersectionNode.addNode(n2);

                        n1.parent.connectNode(intersectionNode);
                        n1.parent = intersectionNode;
                        n1.connectNode(intersectionNode);

                        n2.parent.connectNode(intersectionNode);
                        n2.parent = intersectionNode;
                        n2.connectNode(intersectionNode);

                        /*intersectionNode.addNode(n1); //The parent is connected in the constructor.
                        intersectionNode.addNode(n2);
                        intersectionNode.addNode(n2.parent);
                        //intersectionNode should n1's parent?
                        n1.parent.connectNode(intersectionNode);
                        n1.parent = intersectionNode;
                        n1.connectNode(intersectionNode);

                        n2.parent.connectNode(intersectionNode);
                        n2.parent = intersectionNode;
                        n2.connectNode(intersectionNode);*/

                        nodesToBeAdded.add(intersectionNode);

                    }else if(dy == 0 && dy2 != 0 && yWithin && xOut){
                        //Handle this intersection
                        //the new node should n1.y and n2.x
                        Node intersectionNode = new Node(n2.x, n1.y, n1.parent);
                        intersectionNode.addOtherParent(n2.parent);
                        intersectionNode.addNode(n1); //The parent is connected in the constructor.
                        intersectionNode.addNode(n2);
                        
                        n1.parent.connectNode(intersectionNode);
                        n1.parent = intersectionNode;
                        n1.connectNode(intersectionNode);

                        n2.parent.connectNode(intersectionNode);
                        n2.parent = intersectionNode;
                        n2.connectNode(intersectionNode);

                        nodesToBeAdded.add(intersectionNode);//MAYBE WE CAN NOT ACTUALLY WAIT?

                    }
                }
            }
            }
        }
        allNodes.addAll(nodesToBeAdded);
    }
    private int randomRoadLength(){
        return minRoadLength + rand.nextInt(maxRoadLength - minRoadLength);
    }
    private void updateEndNodes(){
        endNodes = new ArrayList<Node>();
        for(Node node : allNodes){
            if(node.isEndPoint){
                endNodes.add(node);
            }
        }
    }
    public void draw(){
        for(Node n : allNodes){
            n.draw();
        }
    }
}