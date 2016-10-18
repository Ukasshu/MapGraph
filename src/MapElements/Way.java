package MapElements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * Created by lukasz on 16.10.16.
 */
public class Way {
    private ArrayList<Node> nodes;
    private String type;
    private String name;
    private static final List<String> allowedTypes = Arrays.asList("motorway", "trunk", "primary", "secondary", "tertiary", "unclassified", "residential", "service", "living_street", "pedestrian", "track", "bus_guideway", "escape", "raceway", "road");

    public Way(){
        nodes = new ArrayList<>();
        type = null;
        name = null;
    }

    public void addNode(Node node){
        this.nodes.add(node);
    }

    public boolean isCorrect(){
        return (!this.nodes.get(0).getId().equals(this.nodes.get(this.nodes.size() -1).getId())) && allowedTypes.contains(this.type);
    }

    public void setType(String type){
        this.type =type;
    }

    public String getType(){
        return this.type;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public ArrayList<Node> getNodes(){
        return nodes;
    }
}
