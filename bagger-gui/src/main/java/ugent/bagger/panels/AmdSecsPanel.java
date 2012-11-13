package ugent.bagger.panels;

import com.anearalone.mets.AmdSec;
import java.awt.BorderLayout;
import java.awt.Dialog;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.springframework.richclient.command.ActionCommandExecutor;
import org.springframework.util.Assert;
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
                    JDialog dialog = new JDialog(SwingUtils.getFrame(),true);
                    dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
                    dialog.setLayout(new BorderLayout());
                    
                    AmdSecPanel panel = new AmdSecPanel(amdSecPartsTable.getSelected().amdSec);
                    panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
                    
                    dialog.setContentPane(panel);
                    dialog.pack();
                    dialog.setVisible(true);
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
    }
}
