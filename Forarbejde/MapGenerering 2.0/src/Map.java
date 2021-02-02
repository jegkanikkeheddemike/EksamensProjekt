import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.awt.*;

public class Map {
    //                                        north,   south, east, west
    private static final int[][] directions = {{0,1}, {0,-1}, {1,0}, {-1,0}};
    //private static final String[] directionsName = {"North", "South", "East", "West"};

    Node initialNode;
    ArrayList<Node> allNodes;
    ArrayList<Node> endNodes;
    int maxLevel;

    private Random rand = new Random();

    Map(int maxLevel){
        initialNode = new Node(Main.width / 2, Main.height / 2);
        allNodes = new ArrayList<Node>();
        endNodes = new ArrayList<Node>();
        this.maxLevel = maxLevel;
    }

    public void draw(Graphics g){
        for (Node node : allNodes) {
            node.draw(g);
        }
    }

    public void generateMap(){
        Node north = new Node(initialNode.x, initialNode.y+randomRoadLength(), initialNode, Color.green);
        Node south = new Node(initialNode.x, initialNode.y-randomRoadLength(), initialNode, Color.red);
        Node east  = new Node(initialNode.x+randomRoadLength(), initialNode.y, initialNode, Color.yellow);
        Node west  = new Node(initialNode.x-randomRoadLength(), initialNode.y, initialNode, Color.pink);
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
    public void updateEndNodes(){
        endNodes = new ArrayList<Node>();
        for(Node node : allNodes){
            if(node.endPoint){
                endNodes.add(node);
            }
        }
    }

    private void generateNodesAtNode(Node n){
        //System.out.println("=================GENERATING NODES=================");
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
        /*//PRINTING SOME STUFF OUT
        System.out.println("DELTA COOR:");
        for(int i = 0; i < deltaCoorParents.length; i++){
            System.out.println(deltaCoorParents[i]+"");
        }
        System.out.println("DIRECTION INDEX:");
        for(int i = 0; i < directionIndexes.length; i++){
            System.out.println("DIR"+i+" "+directionsName[i]+" :  "+directions[directionIndexes[i]][0]+""+directions[directionIndexes[i]][1]+"");
        }*/

        int numberOfNewNodes = 1+rand.nextInt(3);
        //System.out.println("NUMBER OF NEW NODES: "+numberOfNewNodes+"");
        for(int x = 0; x < numberOfNewNodes; x++){
            //System.out.println("=================NEW NODE "+x+"=================");
            int randomDirectionIndex = rand.nextInt(directionIndexes.length);
            //CREATING THE NEW NODE WITH THE GIVEN DIRECTION
            int roadLength = randomRoadLength();
            int[] direction = directions[directionIndexes[randomDirectionIndex]];
            //System.out.println("DIRECTION: "+direction[0]+""+direction[1]+"");
            //System.out.println("COORDINATES: "+(n.x+(roadLength*direction[0]))+" "+ (n.y+(roadLength*direction[1]))+"");
            Node generatedNode = new Node(n.x+(roadLength*direction[0]), n.y+(roadLength*direction[1]), n);
            //Adding the new node to the things.
            allNodes.add(generatedNode);
            n.addNode(generatedNode);
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
    }


    private int randomRoadLength(){
        return Settings.minRoadLength + rand.nextInt(Settings.maxRoadLength - Settings.minRoadLength);
    }





}