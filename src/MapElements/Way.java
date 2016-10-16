package MapElements;

import java.util.ArrayList;

/**
 * Created by lukasz on 16.10.16.
 */
public class Way {
    private ArrayList<Node> nodes;
    private String type;
    private String name;

    public Way(){
        nodes = new ArrayList<>();
        type = null;
        name = null;
    }

    public void addNode(Node node){
        this.nodes.add(node);
    }

    public boolean isCorrect(){
        return !this.nodes.get(0).getId().equals(this.nodes.get(this.nodes.size() -1).getId());
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
