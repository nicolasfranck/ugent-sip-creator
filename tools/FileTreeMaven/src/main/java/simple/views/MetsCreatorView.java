/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package simple.views;

import eu.medsea.mimeutil.MimeType;
import eu.medsea.mimeutil.MimeUtil;
import helper.FileConstraint;
import helper.FileConstraintForm;
import helper.FileSource;
import helper.FileTreeCellRenderer;
import helper.FileUtils;
import helper.XML;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.xml.validation.Schema;
import org.springframework.richclient.application.support.AbstractView;
import org.w3c.dom.Document;

;
/**
 *
 * @author nicolas
 */
public class MetsCreatorView extends AbstractView{
    private JTable table;
    private JPanel panel;
    private JFileChooser fileChooser;
    private HashMap xsdMap;
    private HashSet<String> xmlFiles = new HashSet<String>();
    private HashMap<String,Schema>xsd_cache = new HashMap<String,Schema>();

    public HashMap getXsdMap() {
        return xsdMap;
    }

    public void setXsdMap(HashMap xsdMap) {
        this.xsdMap = xsdMap;
    }
    
    @Override
    protected JComponent createControl() {
        panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JTable t = getTable();
        JScrollPane scrollTable = new JScrollPane(t);
        scrollTable.setPreferredSize(new Dimension(400,500));

        panel.add(scrollTable,BorderLayout.CENTER);


        JButton chooseButton = new JButton("choose file..");
        chooseButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                final File [] files = chooseFiles();
                if(files == null || files.length == 0)return;                
                final DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
                
                getStatusBar().getProgressMonitor().taskStarted("validating files",files.length);

                SwingUtilities.invokeLater(new Runnable(){
                    @Override
                    public void run() {
                       
                        int i = 0;
                        for(File file:files){

                            if(getStatusBar().getProgressMonitor().isCanceled()){
                                logger.info("cancelling");
                                return;
                            }

                            if(xmlFiles.contains(file.getAbsolutePath()))continue;
                            xmlFiles.add(file.getAbsolutePath());

                            boolean validates = false;
                            String ns = null;
                            boolean validated = false;
                            try{
                                Document doc = XML.XMLToDocument(file);
                                ns = doc.getDocumentElement().getNamespaceURI();
                                String xsd_url = (String) xsdMap.get(ns);
                                if(xsd_url != null){
                                    validated = true;
                                    logger.info("searching for "+xsd_url);
                                    logger.info("validating..");
                                    Schema xsd_schema = xsd_cache.get(xsd_url);
                                    if(xsd_schema == null){
                                        xsd_schema = XML.getSchemaFactory().newSchema(new URL(xsd_url));
                                        xsd_cache.put(xsd_url,xsd_schema);
                                    }
                                    XML.validate(doc,xsd_schema);
                                    logger.info("validating done");
                                }
                                validates = true;
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                            tableModel.addRow(new String [] {
                               file.getName(),
                               ns,
                               validated ? "yes":"no",
                               validates ? "yes":"no"
                            });
                            getStatusBar().getProgressMonitor().worked(++i);
                            tableModel.fireTableDataChanged();
                        }
                        getStatusBar().getProgressMonitor().done();
                            
                       
                    }
                });

                table.repaint();
            }
        });
        panel.add(chooseButton,BorderLayout.SOUTH);
        

        //panel.add(new FileConstraintForm(new FileConstraint()).getControl(),BorderLayout.SOUTH);

        return panel;
    }
    public JTable getTable(){
        if(table == null){
            DefaultTableModel tableModel = newTableModel();
            table = new JTable(tableModel);
            tableModel.addColumn("file");
            tableModel.addColumn("namespace");
            tableModel.addColumn("validated against schema");
            tableModel.addColumn("validation result");
        }
        return table;
    }
    public DefaultTableModel newTableModel(){
        return new DefaultTableModel();
    }
    public JFileChooser getFileChooser(){
        if(fileChooser == null){
            fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("choose a xml file");
            fileChooser.setFileFilter(new FileFilter(){
                @Override
                public boolean accept(File file) {
                    return (file.isDirectory()) || (file.isFile() && file.getName().endsWith("xml"));
                }
                @Override
                public String getDescription() {
                    return "xml only";
                }
            });
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setMultiSelectionEnabled(true);
        }
        return fileChooser;
    }
    public void setFileChooser(JFileChooser fileChooser) {
        this.fileChooser = fileChooser;
    }
    public File [] chooseFiles(){
        JFileChooser fchooser = getFileChooser();
        int freturn = fchooser.showOpenDialog(null);
        File [] files = {};
        if(freturn == JFileChooser.APPROVE_OPTION)files = fchooser.getSelectedFiles();
        return files;
    }
}