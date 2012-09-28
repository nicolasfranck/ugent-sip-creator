package gov.loc.repository.bagger.ui;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class FileChooser extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private static final Log log = LogFactory.getLog(FileChooser.class);
    private JMenuItem openItem;
    private JMenuItem exitItem;

    public FileChooser() {
        setTitle("ZipTest");
        setSize(300, 400);

        JMenuBar mbar = new JMenuBar();
        JMenu m = new JMenu("File");
        
        //Nicolas Franck
        /*
        openItem = new JMenuItem("Open");
        openItem.addActionListener(this);        
        m.add(openItem);*/
        m.add(getOpenItem());
        
        //Nicolas Franck
        /*
        exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(this);
        m.add(exitItem);
        */ 
        m.add(getExitItem());
        
        mbar.add(m);

        getContentPane().add(mbar, "North");
    }

    public JMenuItem getOpenItem() {
        if(openItem == null){
            openItem = new JMenuItem("Open");
            openItem.addActionListener(this);
        }
        return openItem;
    }
    public void setOpenItem(JMenuItem openItem) {
        this.openItem = openItem;
    }
    public JMenuItem getExitItem() {
        if(exitItem == null){
            exitItem = new JMenuItem("Exit");
            exitItem.addActionListener(this);
        }
        return exitItem;
    }
    public void setExitItem(JMenuItem exitItem) {
        this.exitItem = exitItem;
    }   

    @Override
    public void actionPerformed(ActionEvent evt) {
        Object source = evt.getSource();
        if(source == openItem){
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new File("."));
            chooser.setFileFilter(new FileFilter(){
                @Override
                public boolean accept(File f) {
                    return f.getName().toLowerCase().endsWith(".zip") || f.isDirectory();
                }
                @Override
                public String getDescription() {
                    return "ZIP Files";
                }
            });
            int r = chooser.showOpenDialog(this);
            if (r == JFileChooser.APPROVE_OPTION) {
                String zipname = chooser.getSelectedFile().getPath();
                log.debug(zipname);
            }
        }
        else if (source == exitItem){
            //this.hide();
            dispatchEvent(evt);
            //System.exit(0);
        }
    }
}