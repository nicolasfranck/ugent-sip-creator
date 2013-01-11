package ugent.bagger.panels;

import com.anearalone.mets.MdSec;
import com.anearalone.mets.Mets;
import gov.loc.repository.bagger.bag.BagInfoField;
import gov.loc.repository.bagger.bag.impl.MetsBag;
import gov.loc.repository.bagger.ui.BagView;
import gov.loc.repository.bagit.utilities.namevalue.NameValueReader;
import gov.loc.repository.bagit.utilities.namevalue.impl.NameValueReaderImpl;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import ugent.bagger.helper.Context;
import ugent.bagger.helper.MetsUtils;
import ugent.bagger.helper.SwingUtils;
import ugent.bagger.helper.XML;
import ugent.bagger.helper.XSLT;
import ugent.bagger.tables.MdSecPropertiesTable;

/**
 *
 * @author nicolas
 */
public class MdSecSourcePanel extends JPanel{
    static final Log log = LogFactory.getLog(MdSecSourcePanel.class);
    MdSecPropertiesTable mdSecPropertiesTable;
    Mets mets;
    JButton okButton;
    JButton cancelButton;    
    ArrayList<MdSec>dcMdSecs = new ArrayList<MdSec>();
    ArrayList<MdSec>dcCandidateMdSecs = new ArrayList<MdSec>();    
   
    public MdSecSourcePanel(Mets mets){        
        Assert.notNull(mets);
        setMets(mets);
        init();
    }           
    private void init() {        
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        add(new JScrollPane(getMdSecPropertiesTable().getControl()));
        add(createButtonPanel());        
    }
    public MdSecPropertiesTable getMdSecPropertiesTable() {
        if(mdSecPropertiesTable == null){
            
            MetsUtils.findDCMdSec(getMets(),dcMdSecs,dcCandidateMdSecs);            
            ArrayList<MdSec>list = new ArrayList<MdSec>();
            for(MdSec mdSec:dcMdSecs){
                list.add(mdSec);
            }
            for(MdSec mdSec:dcCandidateMdSecs){
                list.add(mdSec);
            }            
            mdSecPropertiesTable = new MdSecPropertiesTable(
                list,
                new String [] {
                    "namespace","MDTYPE"
                },
                "mdSecTable"
            );
            mdSecPropertiesTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);                       
            mdSecPropertiesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent lse) {                                                                                   
                    okButton.setEnabled(mdSecPropertiesTable.getSelected() != null);
                }
            });
        }
        return mdSecPropertiesTable;
    }
    public void setMdSecPropertiesTable(MdSecPropertiesTable mdSecPropertiesTable) {
        this.mdSecPropertiesTable = mdSecPropertiesTable;
    }

    public Mets getMets() {
        return mets;
    }
    public void setMets(Mets mets) {
        this.mets = mets;
    }    
    protected JComponent createButtonPanel(){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        okButton = new JButton(Context.getMessage("ok"));
        okButton.setEnabled(false);
        cancelButton = new JButton(Context.getMessage("cancel"));
        panel.add(okButton);
        panel.add(cancelButton);
        
        okButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {                
                SwingUtils.ShowBusy();
                try{
                    loadFromMdSec(getMdSecPropertiesTable().getSelected().getMdSec());
                }catch(Exception e){
                    log.error(e.getMessage());
                }                                
                SwingUtils.ShowDone();
                firePropertyChange("ok",null,null);
            }            
        });        
        cancelButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                firePropertyChange("cancel",null,null);
            }            
        });
        
        return panel;
    }
    protected void loadFromMdSec(MdSec mdSec) throws ParserConfigurationException, Exception{
        Element sourceElement = mdSec.getMdWrap().getXmlData().get(0);
        Document dcDoc;
        
        //zet om naar DC indien nodig
        if(dcCandidateMdSecs.contains(mdSec)){
            String xsltPath = MetsUtils.getXsltPath(sourceElement,MetsUtils.NAMESPACE_DC);
            xsltPath = xsltPath != null ? xsltPath : MetsUtils.getXsltPath(sourceElement,MetsUtils.NAMESPACE_OAI_DC);                
            if(xsltPath == null){
                throw new Exception("no crosswalk found");
            }                
            URL xsltURL = Context.getResource(xsltPath);                
            Document xsltDoc = XML.XMLToDocument(xsltURL);                
            dcDoc = XSLT.transform(sourceElement,xsltDoc);            
        }else{
            dcDoc = XML.createDocument();
            Node node = dcDoc.importNode(sourceElement,true);
            dcDoc.appendChild(node);            
        }        
        //schrijf naar bag-info
        try{
            
            ByteArrayInputStream baginfoIn = new ByteArrayInputStream(MetsUtils.DCToBagInfo(dcDoc));
            NameValueReaderImpl reader = new NameValueReaderImpl(
                "UTF-8",baginfoIn,"bagInfoTxt"
            );
            
            final BagView bagView = BagView.getInstance();
            MetsBag metsBag = bagView.getBag();
            
            metsBag.getInfo().clearFields();
            
            while(reader.hasNext()){
                NameValueReader.NameValue pair = reader.next();                                           
                BagInfoField field = new BagInfoField();
                field.setName(pair.getName());
                field.setLabel(pair.getName());
                field.setValue(pair.getValue());
                field.setComponentType(BagInfoField.TEXTFIELD_COMPONENT);
                metsBag.addField(field);                
            }             
            
            
            SwingUtilities.invokeLater(new Runnable(){
                @Override
                public void run() {
                    bagView.getInfoFormsPane().getInfoInputPane().getBagInfoForm().resetFields();
                }            
            });
            
            
        }catch(Exception e){
            log.error(e.getMessage());
        }        
    }
}