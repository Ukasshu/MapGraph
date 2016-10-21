import DataConverterExceptions.DataNotConvertedYetException;
import MapElements.Node;
import MapElements.Way;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;

/**
 * Created by lukasz on 21.10.16.
 */
public class DataConverterRemastered {
    private HashMap<String, Node> nodes;
    private ArrayList<Way> ways;
    private boolean areDataConverted = false;
    private ArrayList<Node> unnecessaryNodes;

    public DataConverterRemastered(HashMap<String, Node> nodes, ArrayList<Way> ways){
        this.nodes = nodes;
        this.ways = ways;
        unnecessaryNodes = new ArrayList<>();
    }

    private void markUnnecessaryNodes(){
        Node currentNode;
        ListIterator<Node> iterator;
        for(Way way: ways){
            iterator = way.getNodes().listIterator(0);
            if(!way.isRoundabout()){
                currentNode = iterator.next();
                currentNode.setNeeded();
            }
            currentNode = iterator.next();
            while(iterator.hasNext()){
                if(currentNode.getWaysCounter() <=1){
                    iterator.remove();
                    unnecessaryNodes.add(currentNode);
                }
                else{
                    currentNode.setNeeded();
                }
                currentNode = iterator.next();
            }
            if(!way.isRoundabout()){
                currentNode.setNeeded();
            }
            else if(currentNode.getWaysCounter()<=1){
                iterator.remove();
                //unnecessaryNodes.add(currentNode);
            }
        }
    }

    private void removeUnnecessaryNodes(){
        for(Node node: nodes.values()){
            if(!node.isNeeded()){
                unnecessaryNodes.add(node);
            }
        }
        for(Node node: unnecessaryNodes){
            nodes.remove(node.getId());
        }
    }

    private void convertWaysIntoEdges(){
        ListIterator<Node> iterator;
        Node previousNode, currentNode;
        for(Way way: ways){
            iterator = way.getNodes().listIterator(0);
            if(iterator.hasNext()) {
                currentNode = iterator.next();
                while (iterator.hasNext()) {
                    previousNode = currentNode;
                    currentNode = iterator.next();
                    previousNode.addEdge(currentNode);
                    currentNode.addEdge(previousNode);
                }
            }
        }
    }

    public void runConverter(){
        this.markUnnecessaryNodes();
        this.removeUnnecessaryNodes();
        this.convertWaysIntoEdges();
        this.areDataConverted = true;
    }

    public HashMap<String, Node> returnConvertedNodes() throws DataNotConvertedYetException{
        if(!areDataConverted){
            throw new DataNotConvertedYetException("There was an attempt to return data without converting them!");
        }
        return nodes;
    }
}