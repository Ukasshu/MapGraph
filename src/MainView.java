import MapElements.Node;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;

public class MainView extends JDialog {
    private JPanel contentPane;
    private JButton openFileButton;
    private JButton processButton;
    private JButton saveButton;
    private JLabel image;
    private String path;
    MapReader mapReader;
    DataConverterRemastered dataConverter;
    HashMap<String, Node> nodes;
    BufferedImage graph;
    double bounds[];
    int height;
    int width;

    public MainView() {
        setContentPane(contentPane);
        setModal(true);
        setTitle("MapGraph");
        //setResizable(false);
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
                    mapReader.runReader();
                    bounds = mapReader.getBounds();
                    dataConverter = new DataConverterRemastered(mapReader.getNodes(), mapReader.getWays());
                    dataConverter.runConverter();
                    nodes = dataConverter.returnConvertedNodes();
                    height = 400;
                    width = (int)Math.round(400 * (bounds[3]-bounds[1])/(bounds[2]-bounds[0]));
                    drawMapGraph();
                }catch(FileNotFoundException fileException){
                    JOptionPane.showMessageDialog(contentPane, "Error: file may not exist yet");
                }catch(Exception anotherException){
                    JOptionPane.showMessageDialog(contentPane, "Error: occurred unexpected problem with data converting");
                    anotherException.printStackTrace();
                }
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveFile();
            }
        });
    }

    public static void main(String[] args) {
        MainView dialog = new MainView();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    private void openFile(){
        final JFileChooser fc = new JFileChooser();
        final int returnedValue = fc.showOpenDialog(contentPane);
        if(returnedValue == JFileChooser.APPROVE_OPTION){
            final File file= fc.getSelectedFile();
            path = file.getPath();
        }
        else{

        }
    }

    private void saveFile(){
        JFileChooser fc = new JFileChooser();
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

    private void drawMapGraph(){
        image.setSize(width, height);
        graph = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D grph = graph.createGraphics();
        grph.setColor(Color.WHITE);
        grph.fillRect(0,0,width, height);
        grph.setColor(Color.BLACK);
        int x1=0, x2=0, y1=0, y2=0;
        Node n2;
        double dst;
        for(Node n: nodes.values()){
            x1 = (int)Math.round(width * (n.getLongitude()-bounds[1])/(bounds[3]-bounds[1]));
            y1 = (int)Math.round(height -height * (n.getLatitude()-bounds[0])/(bounds[2]-bounds[0]));
            for(int i =0; i<n.getEdges().size(); i++){
                //if(Long.parseLong(n.getId())<Long.parseLong(n2.getId())) {
                    n2 = n.getEdges().get(i);
                    dst = n.getDistances().get(i);
                    x2 = (int) Math.round(width * (n2.getLongitude() - bounds[1]) / (bounds[3] - bounds[1]));
                    y2 = (int) Math.round(height - height * (n2.getLatitude() - bounds[0]) / (bounds[2] - bounds[0]));
                    grph.drawLine(x1, y1, x2, y2);
                    //grph.drawString(Double.toString(dst), (x1+x2)/2, (y1+y2)/2 );
                //}
            }
            grph.setColor(Color.BLUE);
            grph.fillRect(x1-1,y1-1, 2,2);
            grph.setColor(Color.BLACK);
        }
        image.setIcon(new ImageIcon(graph));
    }
}
