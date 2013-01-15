package ugent.bagger.panels;

import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import ugent.bagger.helper.Context;
import ugent.bagger.params.Failure;
import ugent.bagger.params.FixityFailure;
import ugent.bagger.tables.ClassTable;

/**
 *
 * @author njfranck
 */
public class BagErrorPanel extends JPanel{
    ArrayList<Failure>missingFiles;
    ArrayList<FixityFailure>fixityFailure;
    ArrayList<Failure>newFiles;
    ClassTable<FixityFailure> tableFixityFailure;
    ClassTable<Failure> tableMissingFiles;
    ClassTable<Failure> tableNewFiles;
    
    public BagErrorPanel(ArrayList<Failure>missingFiles,ArrayList<FixityFailure>fixityFailure,ArrayList<Failure>newFiles){
        setMissingFiles(missingFiles);
        setFixityFailure(fixityFailure);
        setNewFiles(newFiles);
        init();
    }
    protected void init(){
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(400,500));
        setComponents();
    }
    protected void setComponents(){
        setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        
        JLabel labelReportTitle = new JLabel(
            Context.getMessage("BagErrorPanel.reportTitle.label"),
            SwingConstants.CENTER
        );
        labelReportTitle.setAlignmentX(LEFT_ALIGNMENT);        
        add(labelReportTitle);
        
        JLabel labelMissingFiles = new JLabel(
            Context.getMessage(
                "BagErrorPanel.missingFiles.label",
                new Object [] {getMissingFiles().size()}
            )
        );
        labelMissingFiles.setAlignmentX(LEFT_ALIGNMENT);
        add(labelMissingFiles);
        
        JComponent cMissingFiles = getTableMissingFiles().getControl();
        JScrollPane sMissingFiles = new JScrollPane(cMissingFiles);
        sMissingFiles.setAlignmentX(LEFT_ALIGNMENT);
        add(sMissingFiles);
        
        JLabel labelFixityFailure = new JLabel(
            Context.getMessage(
                "BagErrorPanel.fixityFailure.label",
                new Object []{getFixityFailure().size()}
            )
        );
        labelFixityFailure.setAlignmentX(LEFT_ALIGNMENT);
        add(labelFixityFailure);
        
        JComponent cFixityFailure = getTableFixityFailure().getControl();        
        JScrollPane sFixityFailure = new JScrollPane(cFixityFailure);
        sFixityFailure.setAlignmentX(LEFT_ALIGNMENT);
        add(sFixityFailure);
        
        JLabel labelNewFiles = new JLabel(
            Context.getMessage(
                "BagErrorPanel.newFiles.label",
                new Object []{getNewFiles().size()}
            )
        );
        labelNewFiles.setAlignmentX(LEFT_ALIGNMENT);
        add(labelNewFiles);
        
        JComponent cNewFiles = getTableNewFiles().getControl();
        JScrollPane sNewFiles = new JScrollPane(cNewFiles);
        sNewFiles.setAlignmentX(LEFT_ALIGNMENT);
        add(sNewFiles);
    }    
    public ClassTable<FixityFailure> getTableFixityFailure() {
        if(tableFixityFailure == null){
            tableFixityFailure = new ClassTable<FixityFailure>(
                getFixityFailure(), 
                new String [] {"path","fixity"}, 
                "tableFixityFailure"
            );
        }
        return tableFixityFailure;
    }
    public ClassTable<Failure> getTableMissingFiles() {
        if(tableMissingFiles == null){
            tableMissingFiles = new ClassTable<Failure>(
                getMissingFiles(),
                new String [] {"path"},
                "tableMissingFiles"
            );            
        }
        return tableMissingFiles;
    }
    public ClassTable<Failure> getTableNewFiles() {
        if(tableNewFiles == null){
            tableNewFiles = new ClassTable<Failure>(
                getNewFiles(),
                new String [] {"path"},
                "tableNewFiles"
            );            
        }
        return tableNewFiles;
    }   
    public ArrayList<Failure> getMissingFiles() {
        if(missingFiles == null){
            missingFiles = new ArrayList<Failure>();
        }
        return missingFiles;
    }
    public void setMissingFiles(ArrayList<Failure> missingFiles) {
        this.missingFiles = missingFiles;
    }
    public ArrayList<FixityFailure> getFixityFailure() {
        if(fixityFailure == null){
            fixityFailure = new ArrayList<FixityFailure>();
        }
        return fixityFailure;
    }
    public void setFixityFailure(ArrayList<FixityFailure> fixityFailure) {
        this.fixityFailure = fixityFailure;
    }
    public ArrayList<Failure> getNewFiles() {
        if(newFiles == null){
            newFiles = new ArrayList<Failure>();
        }
        return newFiles;
    }
    public void setNewFiles(ArrayList<Failure> newFiles) {
        this.newFiles = newFiles;
    }    
}
