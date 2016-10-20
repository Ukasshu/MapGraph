import MapElements.Node;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;

public class MainView extends JDialog {
    private JPanel contentPane;
    private JButton openFileButton;
    private JButton processButton;
    private JButton saveButton;
    private String path;
    MapReader mapReader;
    DataConverter dataConverter;
    HashMap<String, Node> nodes;


    public MainView() {
        setContentPane(contentPane);
        setModal(true);
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
                    dataConverter = new DataConverter(mapReader.getNodes(), mapReader.getWays());
                    dataConverter.runConverter();
                    nodes = dataConverter.returnConvertedNodes();
                }catch(FileNotFoundException fileException){
                    JOptionPane.showMessageDialog(contentPane, "Error: file may not exist yet");
                }catch(Exception anotherException){
                    JOptionPane.showMessageDialog(contentPane, "Error: occurred unexpected problem with data converting");
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
            }
        }
    }
}
