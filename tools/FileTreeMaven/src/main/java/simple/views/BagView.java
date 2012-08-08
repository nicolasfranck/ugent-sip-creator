/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package simple.views;

import helper.Context;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileFilter;
import javax.xml.validation.Schema;
import org.springframework.richclient.application.support.AbstractView;

/**
 *
 * @author nicolas
 */
public class BagView extends AbstractView{
    private JPanel rootPanel;
    private JPanel payloadPanel;
    private JPanel tagfilePanel;
    private JTextArea console;
    private JFileChooser fileSelector;
    private HashMap xsdMap;
    private HashSet<String> xmlFiles = new HashSet<String>();
    private HashMap<String,Schema>xsd_cache = new HashMap<String,Schema>();


    public HashMap getXsdMap() {
        return xsdMap;
    }
    public void setXsdMap(HashMap xsdMap) {
        this.xsdMap = xsdMap;
    }

    public JPanel getPayloadPanel() {
        if(payloadPanel == null)
            payloadPanel = new JPanel();
        return payloadPanel;
    }
    public void setPayloadPanel(JPanel payloadPanel) {
        this.payloadPanel = payloadPanel;
    }
    public JPanel getRootPanel(){
        if(rootPanel == null)
            rootPanel = new JPanel(new GridLayout(0,1));
        return rootPanel;
    }
    public void setRootPanel(JPanel rootPanel) {
        this.rootPanel = rootPanel;
    }
    public JPanel getTagfilePanel() {
        if(tagfilePanel == null)
            tagfilePanel = new JPanel(new GridBagLayout());
        return tagfilePanel;
    }
    public void setTagfilePanel(JPanel tagfilePanel) {
        this.tagfilePanel = tagfilePanel;
    }
    public JFileChooser getFileSelector(){
        if(fileSelector == null){
            fileSelector = new JFileChooser();
            fileSelector.setDialogTitle(Context.getMessage("choose a xml file"));
            fileSelector.setFileFilter(new FileFilter(){
                @Override
                public boolean accept(File file) {
                    return (file.isDirectory()) || (file.isFile() && file.getName().endsWith(".xml"));
                }
                @Override
                public String getDescription() {
                    return Context.getMessage("BagView.fileSelector.tagfile.filter.description");
                }
            });
            fileSelector.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileSelector.setMultiSelectionEnabled(true);
        }
        return fileSelector;
    }
    public void setFileChooser(JFileChooser fileSelector) {
        this.fileSelector = fileSelector;
    }
    public File [] selectFiles(){
        JFileChooser fchooser = getFileSelector();
        int freturn = fchooser.showOpenDialog(null);
        File [] files = {};
        if(freturn == JFileChooser.APPROVE_OPTION)files = fchooser.getSelectedFiles();
        return files;
    }

    @Override
    protected JComponent createControl() {       
        getRootPanel().add(getPayloadPanel());
        getRootPanel().add(getTagfilePanel());
        return getRootPanel();
    }
}
