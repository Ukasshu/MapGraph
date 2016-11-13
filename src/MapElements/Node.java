package MapElements;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class used to hold data about point on the map
 *
 * @author Łukasz Mielczarek
 * @version 21.10.2016
 */
public class Node implements Serializable{
    /**
     * Id of point usen in OpenStreetMap
     */
    private String id;
    /**
     * Latitude of point
     */
    private final double latitude;
    /**
     * Longitude of point
     */
    private final double longitude;
    /**
     * Holds information how many times the point is used
     */
    private int waysCounter;
    /**
     * Holds refereces to the connected points
     */
    private ArrayList<Node> edges;
    /**
     * Holds information whether the point will appear on graph
     */
    private boolean needed;

    /**
     * Node's constructor
     * @param id the id of the point
     * @param latitude the latitude of the point
     * @param longitude the logitude of the point
     */
    public Node(String id, double latitude, double longitude){
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        waysCounter = 0;
        needed =false;
        edges = new ArrayList<>();
    }

    /**
     * Returns the latitude of the point
     * @return latitude of the point
     */
    public double getLatitude(){
        return this.latitude;
    }

    /**
     * Returns the longitude of the point
     * @return longitude of the point
     */
    public double getLongitude(){
        return this.longitude;
    }

    /**
     * Returns the id of the point
     * @return id of the point
     */
    public String getId(){
        return this.id;
    }

    /**
     * Returns the number of Node's uses on the map
     * @return value of waysCounter
     */
    public int getWaysCounter(){
        return waysCounter;
    }

    /**
     * Increases waysCounter value by 1
     */
    public void increaseWaysCounter(){
        waysCounter++;
    }

    /**
     * Adds reference to the point the Node makes an edge with
     * @param node Node that makes an edge
     */
    public void addEdge(Node node){
        this.edges.add(node);
    }

    /**
     * Sets that Node will be used on the graph
     */
    public void setNeeded(){
        this.needed = true;
    }

    /**
     * Returns whether the Node will be used on the graph
     * @return needed value
     */
    public boolean isNeeded(){
        return needed;
    }

    /**
     * Returns collection of points that make edges with the Node
     * @return edges collection
     */
    public ArrayList<Node> getEdges(){
        return this.edges;
    }

    /**
     * Returns distance to the another Node
     * @param node another Node
     * @return distance to the another node in meters
     */
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
}
