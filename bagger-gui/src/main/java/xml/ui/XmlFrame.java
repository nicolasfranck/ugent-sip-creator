/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xml.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

/**
 *
 * @author nicolas
 */
public class XmlFrame extends JFrame{
    public static void main(String [] args) throws FileNotFoundException, IOException{
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        XmlTextPane textPane = new XmlTextPane();        
        textPane.setEditable(false);
        
        
        textPane.read(
            new InputStreamReader(
                new FileInputStream(new File("/home/nicolas/xml/mets_pp.xml")),
                "UTF8"
            ),
            null
        );        
        
        frame.setLayout(new BorderLayout());
        frame.getContentPane().add(new JScrollPane(textPane));        
        frame.setPreferredSize(new Dimension(500,600));
        frame.pack();        
        frame.setVisible(true);            
        
    }
}
