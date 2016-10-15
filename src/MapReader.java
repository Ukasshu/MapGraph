import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by lukasz on 15.10.16.
 */
public class MapReader {
    private File file;
    private Scanner input;
    private boolean areNodesAlreadyRead;

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

    HashMap<String, Node> readNodes() {
        HashMap<String, Node> nodes = new HashMap<String, Node>();

        return nodes;
    }
}
