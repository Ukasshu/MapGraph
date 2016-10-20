import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import MapElements.*;
/**
 * Created by lukasz on 16.10.16.
 */
public class Main2 {
    public static void main(String[] args){
        MapReader mapReader = new MapReader();
        DataConverter dataConverter;
        try{
            mapReader.openFile("/home/lukasz/Pulpit/map");
            mapReader.runReader();
            HashMap<String, Node> nodes = mapReader.getNodes();
            ArrayList<Way> ways = mapReader.getWays();
            System.out.println(nodes.size());
            dataConverter = new DataConverter(mapReader.getNodes(), mapReader.getWays());
            dataConverter.runConverter();
            /*for(String key : nodes.keySet()){
                System.out.println(nodes.get(key).getId() + " " + nodes.get(key).getLatitude() + " " + nodes.get(key).getLongitude());
            }
            for(Way w: ways){
                //System.out.println(w.getName() + " " + w.getType());
                if(w.isRoundabout()){
                    for(Node n: w.getNodes()){
                        System.out.println(w.getName()+" "+n.getId() +" "+ n.getLongitude() +" "+ n.getLatitude()+" "+n.isNeeded()+" "+n.getWaysCounter());
                    }
                }
            }*/
            PrintWriter output = new PrintWriter("/home/lukasz/Pulpit/output.txt");
            System.out.println(nodes.size());
            Node n;
            int i = 0;
            for(String key: nodes.keySet()){
                i++;
                n = nodes.get(key);
                output.println("{" + i);
                output.println("\tId: "+n.getId());
                output.println("\tLatitude: "+n.getLatitude());
                output.println("\tLongitude: "+n.getLongitude());
                output.println("\tGraph edges: ");
                for(Node node: n.getEdges()){
                    output.println("\t\t"+node.getId() + " " + n.distance(node));
                    System.out.println(i);
                }
                output.println("}");
                output.println("");
            }
            output.close();
            System.out.println(nodes.size());
            System.out.println(ways.size());
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}
