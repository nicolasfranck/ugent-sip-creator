package ugent.bagger.panels;


import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.ICsvListReader;
import org.supercsv.prefs.CsvPreference;
import ugent.bagger.exceptions.FileNotReadableException;
import ugent.bagger.forms.CSVParseParamsForm;
import ugent.bagger.helper.CSVUtils;
import ugent.bagger.helper.Context;
import ugent.bagger.helper.SwingUtils;
import ugent.bagger.params.CSVParseParams;

/**
 *
 * @author nicolas
 */
public class CSV1Panel extends JPanel{
    static final Log log = LogFactory.getLog(CSV1Panel.class);
    CSVParseParams csvParseParams;
    CSVParseParamsForm csvParseParamsForm;    
    JButton testButton;   
    JScrollPane scrollerCSVTable;
    JTable csvTable;
    
    public CSV1Panel(){
        init();
    }

    public JScrollPane getScrollerCSVTable() {
        if(scrollerCSVTable == null){
            scrollerCSVTable = new JScrollPane(getCsvTable(),JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);     
            scrollerCSVTable.setPreferredSize(new Dimension(500,300));
        }
        return scrollerCSVTable;
    }
    public void setScrollerCSVTable(JScrollPane scrollerCSVTable) {
        this.scrollerCSVTable = scrollerCSVTable;
    }

    public JTable getCsvTable() {
        if(csvTable == null){
            csvTable = new JTable(){
                @Override
                public boolean getScrollableTracksViewportWidth(){
                    return getPreferredSize().width < getParent().getWidth();
                }
            };   
            //verhinder dat kolombreedten aangepast worden opdat het in de view van de scrollpane zou kunnen
            csvTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);            
            csvTable.setFillsViewportHeight(true);
        }
        return csvTable;
    }

    public void setCsvTable(JTable csvTable) {
        this.csvTable = csvTable;
    }
    
    public void init(){
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        add(getCsvParseParamsForm().getControl());
        add(createButtonPanel());       
        add(getScrollerCSVTable());
    }
     
    public CSVParseParams getCsvParseParams() {
        if(csvParseParams == null){            
            csvParseParams = new CSVParseParams();            
        }
        return csvParseParams;
    }
    public void setCsvParseParams(CSVParseParams csvParseParams) {
        this.csvParseParams = csvParseParams;
    }
    public CSVParseParamsForm getCsvParseParamsForm() {
        if(csvParseParamsForm == null){
            csvParseParamsForm = new CSVParseParamsForm(getCsvParseParams());                                     
            csvParseParamsForm.addFormValueChangeListener("files",new PropertyChangeListener(){
                @Override
                public void propertyChange(PropertyChangeEvent pce) {
                    reloadCSVTable();
                }                
            });
        }   
        return csvParseParamsForm;
    }
    public void reloadCSVTable(){
        if(!getCsvParseParamsForm().hasErrors()){
            getCsvParseParamsForm().commit();
            reloadDataCSVTable();
        }
    }
    public void setCsvParseParamsForm(CSVParseParamsForm csvParseParamsForm) {
        this.csvParseParamsForm = csvParseParamsForm;
    }   
    public JPanel createButtonPanel(){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));        
        testButton = new JButton(Context.getMessage("test"));                
        panel.add(testButton);        
        
        testButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                reloadCSVTable();
            }        
        });
        return panel;
    }
    public void reloadDataCSVTable(){        
        String error = null;
        try{
            File file = getCsvParseParams().getFiles().get(0);            
            
            if(!file.exists()){
                throw new FileNotFoundException();
            }
            if(!file.canRead()){
                throw new FileNotReadableException(file);
            } 
            
            CsvPreference csvPreference = CSVUtils.createCSVPreference(
                getCsvParseParams().getQuoteChar().getChar(),
                getCsvParseParams().getDelimiterChar().getChar(),
                getCsvParseParams().getEndOfLineSymbols(),
                getCsvParseParams().isSurroundingSpacesNeedQuotes()
            );            
            
            ICsvListReader listReader = new CsvListReader(new InputStreamReader(new FileInputStream(file),"UTF8"),csvPreference);                       
            final Object [] cols = listReader.getHeader(true);               
            ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
            
            int count = 0;
            int max = 20;
            ArrayList<String>list;        
            while((list = (ArrayList<String>) listReader.read()) != null){
                if(max > 0 && count > max){
                    break;
                }
                //belangrijk: de reader geeft altijd dezelfde rij-referentie terug, tenzij hij leeg is (dan null)
                //indien de reader niets vindt, dan wordt die interne rij geledigd, en wordt null terug gegeven
                data.add(new ArrayList<String>(list));               
                count++;
            }
            
            Object [][] rows = new Object[data.size()][];
            for(int i = 0;i < data.size();i++){
                ArrayList<String>row = data.get(i);                
                if(row.size() < cols.length){
                    row.addAll(new ArrayList<String>(cols.length - row.size()));                    
                }                
                rows[i] = row.toArray(new String [] {});
            }                                 
            
            DefaultTableModel tableModel = (DefaultTableModel) getCsvTable().getModel();
            tableModel.getDataVector().clear();
            tableModel.setDataVector(rows,cols);            
            tableModel.fireTableDataChanged();
            
            HashMap<String,String>record = new HashMap<String,String>();
            if(rows.length > 0){                
                for(int i = 0;i<cols.length;i++){
                    record.put(
                        (String) cols[i],
                        (String) rows[0][i]
                    );
                }
            }
            firePropertyChange("record",null,record);
            
        }catch(FileNotFoundException e){
            log.error(e.getMessage());
            error = Context.getMessage(
                 "CSVWizard.FileNotFoundException.message",
                 new Object []{e.getMessage()}
            );                            
         }catch(FileNotReadableException e){
            log.error(e.getMessage());
            error = Context.getMessage(
                 "CSVWizard.FileNotReadableException.message",
                 new Object []{e.getFile()}
            );                            
         }catch(IOException e){
             log.error(e.getMessage());
             error = Context.getMessage(
                 "CSVWizard.IOException.message",
                 new Object []{e.getMessage()}
             );                            
         }catch(Exception e){
             log.error(e.getMessage());
             error = Context.getMessage(
                 "CSVWizard.Exception.message",
                 new Object []{e.getMessage()}
             );                            
         }       
         if(error != null){
             SwingUtils.ShowError(null,error);
         }
    }    
}
