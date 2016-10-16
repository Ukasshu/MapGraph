import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

import MapElements.Node;
import MapReaderExceptions.*;

/**
 * Created by lukasz on 15.10.16.
 */
public class MapReader {
    private File file;
    private Scanner input;
    private boolean areNodesAlreadyRead;
    private String currentLine;

    MapReader(){
        this.areNodesAlreadyRead = false;
    }

    void openFile(String filename) throws FileNotFoundException{
        this.file = new File(filename);
        areNodesAlreadyRead = false;
    }

    void openStream() throws FileNotFoundException {
        this.input = new Scanner(file);
    }

    HashMap<String, Node> readNodes() throws NodesAlreadyReadException {
        if(areNodesAlreadyRead){
            throw new NodesAlreadyReadException("Nodes have been already read from this file! Reopen file to do this again.");
        }
        HashMap<String, Node> nodes = new HashMap<>();
        String tempId;
        double tempLat;
        double tempLon;
        this.currentLine = input.nextLine();
        while(!currentLine.matches("^<way id=") || input.hasNextLine()){
            if(currentLine.matches("^<node id=")){
                tempId = this.currentLine.substring(this.currentLine.indexOf("id")+4, this.currentLine.indexOf("\"",this.currentLine.indexOf("id")+4));
                tempLat = Double.parseDouble(this.currentLine.substring(this.currentLine.indexOf("lat")+5, this.currentLine.indexOf("\"", this.currentLine.indexOf(this.currentLine.indexOf("lat")+5))));
                tempLon = Double.parseDouble(this.currentLine.substring(this.currentLine.indexOf("lon")+5, this.currentLine.indexOf("\"", this.currentLine.indexOf(this.currentLine.indexOf("lon")+5))));
                nodes.put(tempId, new Node(tempId, tempLat, tempLon));
            }
            this.currentLine =input.nextLine();
        }
        areNodesAlreadyRead = true;
        return nodes;
    }


}
