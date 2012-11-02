package ugent.bagger.panels;


import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.ICsvListReader;
import org.supercsv.prefs.CsvPreference;
import ugent.bagger.forms.CSVParseParamsForm;
import ugent.bagger.helper.CSVUtils;
import ugent.bagger.params.CSVParseParams;

/**
 *
 * @author nicolas
 */
public class CSV1Panel extends JPanel{
    private CSVParseParams csvParseParams;
    private CSVParseParamsForm csvParseParamsForm;
    private JButton okButton;
    private JButton testButton;   
    private JScrollPane scrollerCSVTable;
    private JTable csvTable;
    
    public CSV1Panel(){
        init();
    }

    public JScrollPane getScrollerCSVTable() {
        if(scrollerCSVTable == null){
            scrollerCSVTable = new JScrollPane(getCsvTable());     
            scrollerCSVTable.setPreferredSize(new Dimension(500,300));
        }
        return scrollerCSVTable;
    }
    public void setScrollerCSVTable(JScrollPane scrollerCSVTable) {
        this.scrollerCSVTable = scrollerCSVTable;
    }

    public JTable getCsvTable() {
        if(csvTable == null){
            csvTable = new JTable();            
            csvTable.setFillsViewportHeight(true);
        }
        return csvTable;
    }

    public void setCsvTable(JTable csvTable) {
        this.csvTable = csvTable;
    }
    
    public void init(){
        setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
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
        okButton = new JButton("ok");       
        testButton = new JButton("test");        
        panel.add(okButton);       
        panel.add(testButton);
        
        okButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                CSV1Panel.this.firePropertyChange("ok",null,null);
            }        
        });  
        
        testButton.addActionListener(
                getCsvParseParamsForm().getCommitCommand().getActionAdapter()
        );
        testButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                reloadCSVTable();
            }        
        });
        return panel;
    }
    public void reloadDataCSVTable(){        
        
        try{
            File file = getCsvParseParams().getFiles().get(0);
            
            CsvPreference csvPreference = CSVUtils.createCSVPreference(
                getCsvParseParams().getQuoteChar(),
                getCsvParseParams().getDelimiterChar(),
                getCsvParseParams().getEndOfLineSymbols(),
                getCsvParseParams().isSurroundingSpacesNeedQuotes()
            );            
            
            ICsvListReader listReader = new CsvListReader(new FileReader(file),csvPreference);                       
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
            
        }catch(Exception e){
            e.printStackTrace();            
        }        
              
    }    
}
