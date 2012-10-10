package ugent.bagger.dialogs;

import gov.loc.repository.bagger.ui.BagView;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;
import javax.swing.*;
import ugent.bagger.forms.RenameParamsForm;
import ugent.bagger.helper.SwingUtils;
import ugent.bagger.params.RenameParams;
import ugent.bagger.workers.DefaultWorker;
import ugent.rename.ErrorAction;
import ugent.rename.RenameError;
import ugent.rename.RenameFilePair;
import ugent.rename.RenameListenerAdapter;
import ugent.rename.Renamer;

/**
 *
 * @author nicolas
 */
public final class RenameDialog extends JDialog{
    private File [] files; 
    private JTextField selectedField;
    private RenameParams renameParams;
    private RenameParamsForm renameParamsForm;      
   
    public RenameDialog(JFrame frame){
        super(frame);
        setLayout(new BorderLayout());
        getContentPane().add(createContentPane());
        setTitle("Rename");
    }

    public JTextField getSelectedField() {
        if(selectedField == null){
            selectedField = new JTextField(getFiles().length);
            selectedField.setEditable(false);
        }
        return selectedField;
    }
    public void setSelectedField(JTextField selectedField) {
        this.selectedField = selectedField;
    }    
    public JComponent createContentPane(){
        
        //JPanel mainPanel = new JPanel(new GridLayout(0,1)); 
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));
        
        final BagView bagView = BagView.getInstance();        
        
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
                RenameDialog.this.dispose();              
            }            
        });
        okButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                        
            }            
        });       
        
        //panel selected
        JPanel selectedPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel selectedLabel = new JLabel("Num selected: ");        
        selectedPanel.add(selectedLabel);
        selectedPanel.add(getSelectedField());        
        
        //voeg alles samen        
        mainPanel.add(selectedPanel);        
        mainPanel.add(getRenameParamsForm().getControl());        
        mainPanel.add(buttonPanel);                
        
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        
        return mainPanel;
    }
    public File [] getFiles() {
        if(files == null){
            files = new File [] {};
        }
        return files;
    }
    public void setFiles(File [] files) {
        this.files = files;
        getSelectedField().setText(""+files.length);
    }  

    public RenameParams getRenameParams() {
        if(renameParams == null){
            renameParams = new RenameParams();
        }
        return renameParams;
    }

    public void setRenameParams(RenameParams renameParams) {
        this.renameParams = renameParams;
    }

    public RenameParamsForm getRenameParamsForm() {
        if(renameParamsForm == null){
            renameParamsForm = new RenameParamsForm(getRenameParams());
        }
        return renameParamsForm;
    }

    public void setRenameParamsForm(RenameParamsForm renameParamsForm) {
        this.renameParamsForm = renameParamsForm;
    }
    
    private class TaskRename extends DefaultWorker {
        private RenameParams renameParams;      
        private int numRenamedSuccess = 0;
        private int numRenamedError = 0;
        private int numToRename = 0;
        public TaskRename(RenameParams renameParams){
            this.renameParams = renameParams;           
        }
        @Override
        protected Void doInBackground(){            
            
            try{

                final Renamer renamer = new Renamer();                            
                renamer.setInputFiles((ArrayList)Arrays.asList(getFiles()));
                renamer.setSource(renameParams.getSource());
                renamer.setDestination(renameParams.getDestination());                
                renamer.setCopy(renameParams.isCopy());               
                renamer.setSimulateOnly(renameParams.isSimulateOnly());                                
                renamer.setOverwrite(renameParams.isOverWrite());            
                if(renameParams.isIgnoreCase()){
                    renamer.setPatternFlag(Pattern.CASE_INSENSITIVE);
                }else{
                    renamer.removePatternFlag((Pattern.CASE_INSENSITIVE));
                }
                if(renameParams.isRegex()){
                    renamer.removePatternFlag(Pattern.LITERAL);
                }else{
                    renamer.setPatternFlag(Pattern.LITERAL);
                }               
                
                renamer.setRenameListener(new RenameListenerAdapter(){                    
                    @Override
                    public boolean approveList(final ArrayList<RenameFilePair> list){                        
                        numToRename = list.size();
                        if(renameParams.isOverWrite()){
                            return true;
                        }
                        //controleer of er geen risico is op overschrijving                        
                        ArrayList<String>seen = new ArrayList<String>();
                        int numFound = 0;
                        for(RenameFilePair pair:list){
                            if(!seen.contains(pair.getSource().getAbsolutePath())){
                                seen.add(pair.getSource().getAbsolutePath());
                            }else{
                                numFound++;
                            }
                            if(!seen.contains(pair.getTarget().getAbsolutePath())){
                                seen.add(pair.getTarget().getAbsolutePath());
                            }else{
                                numFound++;
                            }
                        }                       
                        
                        boolean approved = true;
                        if(numFound > 0){
                            int answer = JOptionPane.showConfirmDialog(SwingUtils.getFrame(),"Waarschuwing: één of meerdere bestanden zullen overschreven worden. Bent u zeker?");
                            approved = answer == JOptionPane.OK_OPTION;
                            renamer.setOverwrite(approved);
                            getRenameParamsForm().getValueModel("overWrite").setValue(new Boolean(true));
                            getRenameParamsForm().commit();
                            System.out.println("is overwrite now: "+(renamer.isOverwrite() ? "yes":"no"));
                        }
                        return approved;
                    }
                    @Override
                    public ErrorAction onError(RenameFilePair pair, RenameError errorType, String errorStr,int index) {
                        numRenamedError++;
                        return renameParams.getOnErrorAction();
                    }
                    @Override
                    public void onRenameSuccess(RenameFilePair pair,int index) {
                        numRenamedSuccess++;
                    }
                    @Override
                    public void onRenameEnd(RenameFilePair pair,int index) {
                        String status = renamer.isSimulateOnly() ? "simulatie":(pair.isSuccess() ? "successvol":"error");                      
                        int percent = (int)Math.floor( ((index+1) / ((float)numToRename))*100);                                                                        
                        setProgress(percent);                         
                    }
                    @Override
                    public void onEnd(ArrayList<RenameFilePair>renamePairs,int numSuccess){                        
                    }
                });
                renamer.rename();
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }        
}