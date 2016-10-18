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

    private void markNonCrossroadsNodes(){
        Node currentNode;
        for(Way way: ways){
            way.getNodes().get(0).setNeeded();
            Iterator<Node> iterator = way.getNodes().listIterator(0);
            while(iterator.hasNext()){
                currentNode = iterator.next();
                if(currentNode.getWaysCounter() != 1 || !iterator.hasNext()){
                    currentNode.setNeeded();
                }
            }
        }
    }

    private void convertWaysIntoEdges(){
        Node previousNode = null;
        int nodesCounter = 1;
        for(Way way:ways){
            for(Node node: way.getNodes()){
                if(previousNode != null){
                    node.addEdge(previousNode);
                }
                if(!node.hasNewId()){
                    node.setNewId(Integer.toString(nodesCounter++));
                }
            }
        }
    }

    public void runConverter(){
        this.markNonCrossroadsNodes();
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
