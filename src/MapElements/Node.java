package MapElements;

/**
 * Created by lukasz on 15.10.16.
 */
public class Node {
    private final String id;
    private final double latitude;
    private final double longitude;
    private int waysCounter;

    public Node(String id, double latitude, double longitude){
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        waysCounter = 0;
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

    /*@Override
    public boolean equals(Object obj){
        if(obj == null){
            return false;
        }
        if(!Node.class.isAssignableFrom(obj.getClass())){
            return false;
        }
        final Node other = (Node) obj;
        if((this.id == null)? (other.id != null): !this.id.equals(other.id)){
            return false;
        }
        return true;
    }*/

    public Node increaseWaysCounter(){
        waysCounter++;
        return this;
    }
}
