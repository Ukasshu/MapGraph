import java.util.ArrayList;
import java.util.HashMap;

import MapElements.*;
/**
 * Created by lukasz on 16.10.16.
 */
public class Main {
    public static void main(String[] args){
        MapReader mapReader = new MapReader();
        try{
            mapReader.openFile("/home/lukasz/Pulpit/lessmap");
            mapReader.run();
            HashMap<String, Node> nodes = mapReader.getNodes();
            ArrayList<Way> ways = mapReader.getWays();
            for(String key : nodes.keySet()){
                System.out.println(nodes.get(key).getId() + " " + nodes.get(key).getLatitude() + " " + nodes.get(key).getLongitude());
            }
            for(Way w: ways){
                for(Node n: w.getNodes())
                    System.out.println("W"+ n.getId() + " " + n.getLatitude() + " " + n.getLongitude() + (w.getName()!=null? " " + w.getName():" -") + (w.getType()!=null? " " + w.getType():" -"));
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}
