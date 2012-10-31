package ugent.bagger.panels;


import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
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
public class CSVPanel extends JPanel{
    private CSVParseParams csvParseParams;
    private CSVParseParamsForm csvParseParamsForm;
    private JButton okButton;
    private JButton testButton;
    private JButton cancelButton;
    private JScrollPane scrollerCSVTable;
    
    public CSVPanel(){
        init();
    }
    
    public void init(){
        setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
        add(getCsvParseParamsForm().getControl());
        add(createButtonPanel());
        scrollerCSVTable = new JScrollPane();
        add(scrollerCSVTable);
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
        System.out.println("has errors: "+getCsvParseParamsForm().hasErrors());
       
        if(!getCsvParseParamsForm().hasErrors()){
            getCsvParseParamsForm().commit();
            remove(scrollerCSVTable);
            JTable table = createCSVTable();
            table.setPreferredSize(new Dimension(500,300));
            scrollerCSVTable = new JScrollPane(table);            
            table.setFillsViewportHeight(true);
            add(scrollerCSVTable);
            revalidate();
            repaint();
        }
    }
    public void setCsvParseParamsForm(CSVParseParamsForm csvParseParamsForm) {
        this.csvParseParamsForm = csvParseParamsForm;
    }   
    public JPanel createButtonPanel(){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        okButton = new JButton("ok");
        cancelButton = new JButton("cancel");
        testButton = new JButton("test");
        panel.add(okButton);
        panel.add(cancelButton);
        panel.add(testButton);
        
        okButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                CSVPanel.this.firePropertyChange("ok",null,null);
            }        
        });
        cancelButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                CSVPanel.this.firePropertyChange("cancel",null,null);
            }        
        });
        testButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                reloadCSVTable();
            }        
        });
        return panel;
    }
    public JTable createCSVTable(){        
        JTable table = null;
        try{
            File file = getCsvParseParams().getFiles().get(0);
            System.out.println("file to parse: "+file);
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
            
            for(Object [] row:rows){
                for(Object col:row){
                    System.out.println(col+" ");
                }
                System.out.println();
            }
            
            table = new JTable(rows,cols);
        }catch(Exception e){
            e.printStackTrace();
            table = new JTable();
        }
        
        return table;        
    }    
}
