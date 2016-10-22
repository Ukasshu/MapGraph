import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import MapElements.*;
import MapReaderExceptions.*;

/**
 * Created by lukasz on 15.10.16.
 */
public class MapReader {
    private File file;
    private Scanner input;
    private boolean areNodesAlreadyRead;
    private boolean areWaysAlreadyRead;
    private String currentLine;
    private HashMap<String, Node> nodes;
    private ArrayList<Way> ways;
    private double bounds[];

    MapReader(){
        this.areNodesAlreadyRead = false;
        this.areWaysAlreadyRead = false;
    }

    public void openFile(String filename) throws FileNotFoundException{
        this.file = new File(filename);
        areNodesAlreadyRead = false;
        areWaysAlreadyRead = false;
    }

    public void openStream() throws FileNotFoundException {
        this.input = new Scanner(file);
    }

    private void readBounds() throws NodesAlreadyReadException{
        bounds = new double[4];
        if (areNodesAlreadyRead){
            throw new NodesAlreadyReadException("Too late to read bounds!");
        }
        this.currentLine = input.nextLine();
        while(!this.currentLine.matches("(.*)<bounds minlat(.*)")){
            this.currentLine = input.nextLine();
        }
        bounds[0] = Double.parseDouble(this.currentLine.substring(this.currentLine.indexOf("minlat")+8, this.currentLine.indexOf('\"', this.currentLine.indexOf("minlat")+8)));
        bounds[1] = Double.parseDouble(this.currentLine.substring(this.currentLine.indexOf("minlon")+8, this.currentLine.indexOf('\"', this.currentLine.indexOf("minlon")+8)));
        bounds[2] = Double.parseDouble(this.currentLine.substring(this.currentLine.indexOf("maxlat")+8, this.currentLine.indexOf('\"', this.currentLine.indexOf("maxlat")+8)));
        bounds[3] = Double.parseDouble(this.currentLine.substring(this.currentLine.indexOf("maxlon")+8, this.currentLine.indexOf('\"', this.currentLine.indexOf("maxlon")+8)));

    }

    private void readNodes() throws NodesAlreadyReadException {
        if(areNodesAlreadyRead){
            throw new NodesAlreadyReadException("Nodes have been already read from this file! Reopen file to do this again.");
        }
        nodes = new HashMap<>();
        String tempId;
        double tempLat;
        double tempLon;
        this.currentLine = input.nextLine();
        while(!currentLine.matches("(.*)<way (.*)") && input.hasNextLine()){
            if(currentLine.matches("(.*)<node (.*)")){
                tempId = this.currentLine.substring(this.currentLine.indexOf("id")+4, this.currentLine.indexOf('\"',this.currentLine.indexOf("id")+4));
                tempLat = Double.parseDouble(this.currentLine.substring(this.currentLine.indexOf("lat")+5, this.currentLine.indexOf('\"',this.currentLine.indexOf("lat")+5)));
                tempLon = Double.parseDouble(this.currentLine.substring(this.currentLine.indexOf("lon")+5, this.currentLine.indexOf("\"", this.currentLine.indexOf("lon")+5)));
                nodes.put(tempId, new Node(tempId, tempLat, tempLon));
            }
            this.currentLine =input.nextLine();
        }
        areNodesAlreadyRead = true;
    }

    private void readWays() throws NodesNotReadYetException, WaysAlreadyReadException{
        if(!areNodesAlreadyRead){
            throw new NodesNotReadYetException("There was an attempt of reading ways without nodes!");
        }
        if(areWaysAlreadyRead){
            throw new WaysAlreadyReadException("Ways have been already read from this file! Reopen file to do this again.");
        }
        ways = new ArrayList<>();
        while(input.hasNextLine() ) {
            if (currentLine.matches("(.*)<way(.*)")) {
                Way newWay = new Way();
                String nodeId;
                currentLine = input.nextLine();
                while (currentLine.matches("(.*)<nd ref(.*)")) {
                    nodeId = currentLine.substring(currentLine.indexOf("ref") + 5, currentLine.indexOf("\"", currentLine.indexOf("ref") + 5));
                    newWay.addNode(nodes.get(nodeId));
                    currentLine = input.nextLine();
                }
                while(!currentLine.matches("(.*)</way>(.*)")){
                    if(currentLine.matches("(.*)tag k=\"highway\"(.*)")){
                        newWay.setType(currentLine.substring(currentLine.indexOf("v=\"")+3, currentLine.indexOf('\"', currentLine.indexOf("v=\"")+3)));
                    }
                    else if(currentLine.matches("(.*)tag k=\"name\"(.*)")){
                        newWay.setName(currentLine.substring(currentLine.indexOf("v=\"")+3, currentLine.indexOf('\"', currentLine.indexOf("v=\"")+3)));
                    }
                    else if(currentLine.matches("(.*)tag k=\"junction\" v=\"roundabout\"(.*)")){
                        newWay.setRoundabout();
                        newWay.getNodes().remove(newWay.getNodes().size()-1);
                    }
                    currentLine = input.nextLine();
                }
                if(newWay.isCorrect()){
                    for(Node n: newWay.getNodes()){
                        n.increaseWaysCounter();
                    }
                    ways.add(newWay);
                }
            }
            currentLine = input.nextLine();
        }
        areWaysAlreadyRead=true;
    }

    public void runReader() throws NodesAlreadyReadException, NodesNotReadYetException, WaysAlreadyReadException, FileNotFoundException{
        this.openStream();
        readBounds();
        this.readNodes();
        this.readWays();
    }

    public HashMap<String, Node> getNodes() throws NodesNotReadYetException{
        if(!areNodesAlreadyRead){
            throw new NodesNotReadYetException("Try to get nodes after reading them!");
        }
        return nodes;
    }

    public double[] getBounds(){
        return bounds;
    }

    public ArrayList<Way> getWays() throws WaysNotReadYetException{
        return ways;
    }
}
