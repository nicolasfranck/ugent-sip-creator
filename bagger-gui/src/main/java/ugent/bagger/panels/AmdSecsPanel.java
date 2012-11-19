package ugent.bagger.panels;

import com.anearalone.mets.AmdSec;
import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.springframework.richclient.command.ActionCommandExecutor;
import org.springframework.util.Assert;
import ugent.bagger.helper.Context;
import ugent.bagger.helper.SwingUtils;
import ugent.bagger.tables.ClassTable;

/**
 *
 * @author nicolas
 */
public class AmdSecsPanel extends JPanel{
    private ArrayList<AmdSec>data;    
    private ClassTable<AmdSecParts>amdSecPartsTable;
    
    public AmdSecsPanel(ArrayList<AmdSec>data){        
        Assert.notNull(data);
        this.data = data;
        init();
    }
    public void init(){
        setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
        add(createButtonPanel());
        add(new JScrollPane(getAmdSecPartsTable().getControl()));
    }
    public void reset(ArrayList<AmdSec>data){
        getAmdSecPartsTable().reset(toAmdSecParts(data));
        this.data = data;
    }
    public ArrayList<AmdSec> getData() {
        return data;
    }      
    public ClassTable<AmdSecParts> getAmdSecPartsTable() {
        if(amdSecPartsTable == null){
            amdSecPartsTable = new ClassTable<AmdSecParts>(
                toAmdSecParts(getData()),
                new String [] {"numTechMD","numRightsMD","numDigiprovMD","numSourceMD"},
                "amdSecPartsTable"
            );
            amdSecPartsTable.setDoubleClickHandler(new ActionCommandExecutor() {
                @Override
                public void execute() {
                    AmdSecParts parts = amdSecPartsTable.getSelected();                    
                    if(parts == null || parts.amdSec == null){
                        return;
                    }                    
                    startAmdSecDialog(parts.getAmdSec());
                }
            });            
        }
        return amdSecPartsTable;
    }    
    public static ArrayList<AmdSecParts> toAmdSecParts(ArrayList<AmdSec>list){        
        ArrayList<AmdSecParts>parts = new ArrayList<AmdSecParts>();        
        for(AmdSec amdSec:list){        
            parts.add(new AmdSecParts(amdSec));
        }
        return parts;
    }
    public static void startAmdSecDialog(AmdSec amdSec){
        JDialog dialog = new JDialog(SwingUtils.getFrame(),true);
        dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setLayout(new BorderLayout());                                        
        AmdSecPanel panel = new AmdSecPanel(amdSec);
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));                    
        dialog.setContentPane(panel);
        dialog.pack();
        dialog.setVisible(true);
    }
    public JComponent createButtonPanel(){
        final JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton(Context.getMessage("AmdSecsPanel.addButton.label"));
        JButton removeButton = new JButton(Context.getMessage("AmdSecsPanel.removeButton.label"));
        panel.add(addButton);
        panel.add(removeButton);
        
        addButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                AmdSec amdSec = new AmdSec();
                getData().add(amdSec);
                reset(getData());
                startAmdSecDialog(amdSec);
            }            
        });
        
        removeButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                deleteSelected();
                reset(getData());
            }            
        });
        
        return panel;
    }
    public void deleteSelected(){
        ArrayList<AmdSecParts> selections = (ArrayList<AmdSecParts>) getAmdSecPartsTable().getSelections();
        for(AmdSecParts amdSecParts:selections){
            getData().remove(amdSecParts.getAmdSec());
        }
    }
    public static class AmdSecParts {
        private AmdSec amdSec;
        
        public AmdSecParts(AmdSec amdSec){
            Assert.notNull(amdSec);
            this.amdSec = amdSec;
        }
        public int getNumSourceMD() {
            return amdSec.getSourceMD().size();
        }
        public int getNumTechMD() {
            return amdSec.getTechMD().size();
        }
        public int getNumDigiprovMD() {
            return amdSec.getDigiprovMD().size();
        }
        public int getNumRightsMD() {
            return amdSec.getRightsMD().size();
        } 
        public AmdSec getAmdSec(){
            return amdSec;
        }
    }
}