package ugent.bagger.dialogs;

import com.anearalone.mets.MdSec;
import gov.loc.repository.bagger.ui.BagView;
import gov.loc.repository.bagger.ui.util.ApplicationContextUtil;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;
import org.springframework.richclient.progress.BusyIndicator;
import ugent.bagger.helper.SwingUtils;
import ugent.bagger.importers.ImportType;
import ugent.bagger.tables.EditMdSecPropertiesTable;
import ugent.bagger.workers.TaskAddMdSecFromImport2;

/**
 *
 * @author nicolas
 */
public final class ImportDialog extends JDialog{
    private File [] files;        
    private JTextField fileField;
    private ImportType importType;        
    
    public ImportDialog(JFrame frame,boolean isModal){
        super(frame,isModal);
        getContentPane().add(createContentPane());
        setTitle("Import");
    }
    public JComponent createContentPane(){
        
        JPanel mainPanel = new JPanel(); 
        mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));
        final BagView bagView = BagView.getInstance();
        final EditMdSecPropertiesTable mdSecPropertiesTable = bagView.getInfoFormsPane().getInfoInputPane().getMetsPanel().getDmdSecPropertiesPanel().getEditDmdSecPropertiesTable();
        
        //buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        final JButton okButton = new JButton("ok");
        okButton.setEnabled(false);
        JButton cancelButton = new JButton("cancel");        
        buttonPanel.add(okButton);        
        buttonPanel.add(cancelButton);
        cancelButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                ImportDialog.this.dispose();
            }            
        });
        okButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                if(files == null || importType == null){
                    return;
                }
                BusyIndicator.showAt(SwingUtils.getFrame());                
                
                SwingUtils.monitor(
                    new TaskAddMdSecFromImport2(files,importType),                    
                    "importing..",
                    "procent",
                    getPropertyListeners()
                );
                
                BusyIndicator.clearAt(SwingUtils.getFrame());
                ImportDialog.this.dispose();                
            }            
        });
        
        //input type
        JPanel importTypePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel importTypeLabel = new JLabel("type:");
        final JComboBox importTypeComboBox = new JComboBox(ImportType.values());        
        importTypeComboBox.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent ie) {
                if(ie.getStateChange() == ItemEvent.SELECTED){
                    importType = (ImportType) ie.getItem();                    
                }
            }
        });
        importTypeComboBox.setEnabled(false);
        importTypePanel.add(importTypeLabel);
        importTypePanel.add(importTypeComboBox);        
        
        //file input
        JPanel filePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel fileLabel = new JLabel("File:");
        JButton fileButton = new JButton("browse");
        fileButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                importTypeComboBox.setEnabled(false);
                okButton.setEnabled(false);                
                
                File [] selectedFiles = SwingUtils.chooseFiles(
                    "Select file",
                    null,
                    JFileChooser.FILES_ONLY,
                    true,
                    SwingUtils.getFrame(),
                    JFileChooser.OPEN_DIALOG
                );                
                if(selectedFiles.length > 0){
                    setFiles(selectedFiles);
                    fileField.setText(""+files.length+" selected");
                    importTypeComboBox.setEnabled(true);
                    okButton.setEnabled(true);
                }                
            }            
        });
        filePanel.add(fileLabel);
        filePanel.add(getFileField());
        filePanel.add(fileButton);
        
        //voeg alles samen
        mainPanel.add(filePanel);
        mainPanel.add(importTypePanel);
        mainPanel.add(buttonPanel);
        
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        
        return mainPanel;
    }
    public File [] getFiles() {
        return files;
    }
    public void setFiles(File [] files) {
        this.files = files;
    }    
    public JTextField getFileField() {
        if(fileField == null){
            fileField = new JTextField();
            fileField.setColumns(20);
            fileField.setEditable(false);
            fileField.setEnabled(false);
        }
        return fileField;
    }
    public void setFileField(JTextField fileField) {
        this.fileField = fileField;
    }       
    private List<PropertyChangeListener> getPropertyListeners(){        
        PropertyChangeListener listener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent pce) {                
                final BagView bagView = BagView.getInstance();
                final EditMdSecPropertiesTable dmdSecPropertiesTable = bagView.getInfoFormsPane().getInfoInputPane().getMetsPanel().getDmdSecPropertiesPanel().getEditDmdSecPropertiesTable();
                if(pce.getPropertyName().equals("state") && pce.getNewValue() == SwingWorker.StateValue.STARTED){                    
                    ApplicationContextUtil.addConsoleMessage("adding new mdSec batch");
                }else if(pce.getPropertyName().equals("log")){
                    ApplicationContextUtil.addConsoleMessage(pce.getNewValue().toString());                    
                }else if(pce.getPropertyName().equals("send")){
                    dmdSecPropertiesTable.addMdSec((MdSec)pce.getNewValue());
                }else if(
                    pce.getPropertyName().equals("report") && 
                    pce.getNewValue().toString().compareTo("success") == 0
                ){
                    dmdSecPropertiesTable.refresh();
                }
            }
        };
        return Arrays.asList(new PropertyChangeListener [] {listener});        
    }    
}