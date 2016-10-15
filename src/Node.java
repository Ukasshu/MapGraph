/**
 * Created by lukasz on 15.10.16.
 */
public class Node {
    private final String id;
    private final double latitude;
    private final double longitude;

    public Node(String id, double latitude, double longitude){
        this.id = new String(id);
        this.latitude = latitude;
        this.longitude = longitude;
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
}
