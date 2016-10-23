import MapElements.Node;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * Class providing GUI
 *
 * @author Łukasz Mielczarek
 * @version 22.10.2016
 */
public class MainView extends JDialog {
    /**
     * Main panel
     */
    private JPanel contentPane;
    /**
     * "Open file" button
     */
    private JButton openFileButton;
    /**
     * "Process" button
     */
    private JButton processButton;
    /**
     * "Save as text" button
     */
    private JButton saveAsTextButton;
    /**
     * "Save as image" button
     */
    private JButton saveAsImageButton;
    /**
     * Label where graph will have been drawn
     */
    private JLabel image;
    /**
     * String holding path to the file that will be opened
     */
    private String path;
    /**
     * Holds MapReader that will be reading file
     */
    private MapReader mapReader;
    /**
     * Holds DataConverter that will be converting data
     */
    private DataConverter dataConverter;
    /**
     * Container which will be holding converted data
     */
    private HashMap<String, Node> nodes;
    /**
     * Used to create graphical version of graph
     */
    private BufferedImage graph =null;
    /**
     * Holds map's boundaries provided by MapReader
     */
    private double bounds[];
    /**
     * Height of image
     */
    private int height;
    /**
     * Width of image
     */
    private int width;

    /**
     * Default constructor
     */
    public MainView() {
        setContentPane(contentPane);
        setModal(true);
        setTitle("MapGraph");
        setResizable(false);
        setFocusableWindowState(true);
        openFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openFile();
                mapReader = new MapReader();
                dataConverter = null;
                nodes = null;
            }
        });

        processButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    mapReader.openFile(path);
                    System.out.println(10);
                    mapReader.runReader();
                    bounds = mapReader.getBounds();
                    System.out.println(40);
                    dataConverter = new DataConverter(mapReader.getNodes(), mapReader.getWays());
                    dataConverter.runConverter();
                    System.out.println(80);
                    nodes = dataConverter.returnConvertedNodes();
                    height = 400;
                    width = (int)Math.round(400 * (bounds[3]-bounds[1])/(bounds[2]-bounds[0]));
                    drawMapGraph();
                    System.out.println(100);
                }catch(FileNotFoundException fileException){
                    JOptionPane.showMessageDialog(contentPane, "Error: file may not exist yet");
                }catch(Exception anotherException){
                    JOptionPane.showMessageDialog(contentPane, "Error: occurred unexpected problem with data converting");
                    anotherException.printStackTrace();
                }
            }
        });

        saveAsTextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveFile();
            }
        });
        saveAsImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveImage();
            }
        });
    }

    /**
     * Main
     * @param args
     */
    public static void main(String[] args) {
        MainView dialog = new MainView();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    /**
     * Opens file using dialog
     */
    private void openFile(){
        final JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Opening file...");
        final int returnedValue = fc.showOpenDialog(contentPane);
        if(returnedValue == JFileChooser.APPROVE_OPTION){
            final File file= fc.getSelectedFile();
            path = file.getPath();
        }
        else{

        }
    }

    /**
     * Saves graph as text
     */
    private void saveFile(){
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Saving as text...");
        final int returnedValue = fc.showSaveDialog(contentPane);
        if(returnedValue == JFileChooser.APPROVE_OPTION){
            final File file = fc.getSelectedFile();
            final String path = file.getPath();
            try {
                PrintWriter output = new PrintWriter(path);
                Node n;
                int i=0;
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
            }catch(FileNotFoundException e){
                JOptionPane.showMessageDialog(contentPane, "Error: cannot save the file");
            }catch(NullPointerException e){
                JOptionPane.showMessageDialog(contentPane, "Error: you didn't process any file");
            }
        }
    }

    /**
     * Saves graph as image
     */
    private void saveImage(){
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Saving as image...");
        final int returnedValue = fc.showSaveDialog(contentPane);
        if(returnedValue == JFileChooser.APPROVE_OPTION){
            try {
                File file = fc.getSelectedFile();
                ImageIO.write(graph, "png", file);
            }catch(IOException e){
                JOptionPane.showMessageDialog(contentPane, "Error: cannot save file");
            }catch(IllegalArgumentException e){
                JOptionPane.showMessageDialog(contentPane, "Error: you didn't process any file");
            }
        }
        else{

        }
    }

    /**
     * Draws graph on BufferedImage and makes it visible
     */
    private void drawMapGraph(){
        image.setSize(width, height);
        graph = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D grph = graph.createGraphics();
        grph.setColor(Color.WHITE);
        grph.fillRect(0,0,width, height);
        grph.setColor(Color.BLACK);
        int x1=0, x2=0, y1=0, y2=0;
        Node n2;
        for(Node n: nodes.values()){
            x1 = (int)Math.round(width * (n.getLongitude()-bounds[1])/(bounds[3]-bounds[1]));
            y1 = (int)Math.round(height -height * (n.getLatitude()-bounds[0])/(bounds[2]-bounds[0]));
            for(int i =0; i<n.getEdges().size(); i++){
                    n2 = n.getEdges().get(i);
                    x2 = (int) Math.round(width * (n2.getLongitude() - bounds[1]) / (bounds[3] - bounds[1]));
                    y2 = (int) Math.round(height - height * (n2.getLatitude() - bounds[0]) / (bounds[2] - bounds[0]));
                    grph.drawLine(x1, y1, x2, y2);
            }
            grph.setColor(Color.BLUE);
            grph.fillRect(x1-1,y1-1, 2, 2);
            grph.setColor(Color.BLACK);
        }
        image.setIcon(new ImageIcon(graph));
    }
}
