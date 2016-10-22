package MapElements;

import java.util.ArrayList;
import java.math.*;

/**
 * Created by lukasz on 15.10.16.
 */
public class Node {
    private String id;
    private final double latitude;
    private final double longitude;
    private int waysCounter;
    private ArrayList<Node> edges;
    private ArrayList<Double> distances;
    private boolean needed;

    public Node(String id, double latitude, double longitude){
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        waysCounter = 0;
        needed =false;
        //this.newId = false;
        edges = new ArrayList<>();
        distances = new ArrayList<>();
    }

    public double getLatitude(){
        return this.latitude;
    }

    public double getLongitude(){
        return this.longitude;
    }

    public String getId(){
        return this.id;
    }

    public int getWaysCounter(){
        return waysCounter;
    }

    public Node increaseWaysCounter(){
        waysCounter++;
        return this;
    }

    public void addEdge(Node node){
        this.edges.add(node);
        this.distances.add(this.distance(node));
    }

    public void setNeeded(){
        this.needed = true;
    }

    public boolean isNeeded(){
        return needed;
    }

    public ArrayList<Node> getEdges(){
        return this.edges;
    }

    public double distance(Node node){
        double R = 6371e3;
        double phi1 = Math.toRadians(this.latitude);
        double phi2 = Math.toRadians(node.getLatitude());
        double deltaPhi = Math.toRadians(node.getLatitude() - this.latitude);
        double deltaLambda = Math.toRadians(node.getLongitude() - this.longitude);
        double a = Math.sin(deltaPhi/2)*Math.sin(deltaPhi/2)+Math.cos(phi1)*Math.cos(phi2)*Math.sin(deltaLambda/2)*Math.sin(deltaLambda/2);
        double c = 2*Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return R*c;
    }

    public ArrayList<Double> getDistances(){
        return distances;
    }
}
