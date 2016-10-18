import java.util.ArrayList;
import java.util.HashMap;

import MapElements.*;
/**
 * Created by lukasz on 16.10.16.
 */
public class Main {
    public static void main(String[] args){
        MapReader mapReader = new MapReader();
        DataConverter dataConverter;
        try{
            mapReader.openFile("/home/lukasz/Pulpit/map");
            mapReader.runReader();
            HashMap<String, Node> nodes = mapReader.getNodes();
            ArrayList<Way> ways = mapReader.getWays();
            dataConverter = new DataConverter(mapReader.getNodes(), mapReader.getWays());
            for(String key : nodes.keySet()){
                System.out.println(nodes.get(key).getId() + " " + nodes.get(key).getLatitude() + " " + nodes.get(key).getLongitude());
            }
            for(Way w: ways){
                System.out.println(w.getName() + " " + w.getType());
                for(Node n: w.getNodes())
                    System.out.println("W"+ n.getId() + " " + n.getLatitude() + " " + n.getLongitude() + " " + n.getWaysCounter() + (w.getName()!=null? " " + w.getName():" -") + (w.getType()!=null? " " + w.getType():" -"));
            }
            System.out.println(ways.size());
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}
