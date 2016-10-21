import MapElements.*;
import DataConverterExceptions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by lukasz on 18.10.16.
 */
public class DataConverter {
    private HashMap<String, Node> nodes;
    private ArrayList<Way> ways;
    private boolean areDataConververted = false;

    public DataConverter(HashMap<String, Node> nodes, ArrayList<Way> ways){
        this.nodes = nodes;
        this.ways = ways;
    }

    private void removeUnnecessaryNodes() {
        ArrayList<Node> unnecessaryNodes = new ArrayList<>();
        for(String key: nodes.keySet()){
            if(!nodes.get(key).isNeeded()){
                unnecessaryNodes.add(nodes.get(key));
            }
        }
        for(Node node : unnecessaryNodes){
            nodes.remove(node.getId());
        }
    }

    private void markUnnecessaryNodes(){
        Node currentNode;
        for(Way way: ways){
            if(!way.isRoundabout()){
                way.getNodes().get(0).setNeeded();
                way.getNodes().get(way.getNodes().size()-1).setNeeded();
            }
            Iterator<Node> iterator = way.getNodes().listIterator(0);

            while(iterator.hasNext()){
                currentNode = iterator.next();
                if(currentNode.getWaysCounter() > 1){
                    currentNode.setNeeded();
                }
            }
        }
    }

    private void convertWaysIntoEdges(){
        Node previousNode;
        //int nodesCounter = 1;
        for(Way way:ways){
            previousNode = null;
            for(Node node: way.getNodes()){
                if(node.isNeeded()) {
                    if (previousNode != null && Long.parseLong(node.getId()) < Long.parseLong(previousNode.getId())) {
                        node.addEdge(previousNode);
                        previousNode.addEdge(node);
                    }
                    //if (!node.hasNewId()) {
                    //    node.setNewId(Integer.toString(nodesCounter++));
                    previousNode = node;
                }
            }
        }
    }

    public void runConverter(){
        this.markUnnecessaryNodes();
        this.removeUnnecessaryNodes();
        this.convertWaysIntoEdges();
        this.areDataConververted = true;
    }

    public HashMap<String, Node> returnConvertedNodes() throws DataNotConvertedYetException {
        if(!areDataConververted){
            throw new DataNotConvertedYetException("There was an attempt to return data without converting them!");
        }
        return nodes;
    }
}
