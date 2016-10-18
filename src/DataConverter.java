import MapElements.*;
import DataConverterExceptions.*;

import java.util.ArrayList;
import java.util.HashMap;

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
        for(String key: nodes.keySet()){
            if(nodes.get(key).getWaysCounter() == 0){
                nodes.remove(key);
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
