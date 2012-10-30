package ugent.bagger.panels;


import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.CsvMapReader;
import org.supercsv.io.ICsvListReader;
import org.supercsv.io.ICsvMapReader;
import org.supercsv.prefs.CsvPreference;
import ugent.bagger.forms.CSVParseParamsForm;
import ugent.bagger.helper.CSVUtils;
import ugent.bagger.helper.IteratorListener;
import ugent.bagger.params.CSVParseParams;

/**
 *
 * @author nicolas
 */
public class CSVPanel extends JPanel{
    private CSVParseParams csvParseParams;
    private CSVParseParamsForm csvParseParamsForm;
    private JButton okButton;
    private JButton cancelButton;
    
    public void init(){
        setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
        add(getCsvParseParamsForm().getControl());
        add(createButtonPanel());
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
    public void setCsvParseParamsForm(CSVParseParamsForm csvParseParamsForm) {
        this.csvParseParamsForm = csvParseParamsForm;
    }   
    public JPanel createButtonPanel(){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        okButton = new JButton("ok");
        cancelButton = new JButton("cancel");
        panel.add(okButton);
        panel.add(cancelButton);
        
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
        return panel;
    }
    public JTable createCSVTable(){        
        JTable table = null;
        try{
            File file = getCsvParseParams().getFile();
            CsvPreference csvPreference = CSVUtils.createCSVPreference(
                getCsvParseParams().getQuoteChar(),
                getCsvParseParams().getDelimiterChar(),
                getCsvParseParams().getEndOfLineSymbols(),
                getCsvParseParams().isSurroundingSpacesNeedQuotes()
            );            
            ICsvListReader listReader = new CsvListReader(new FileReader(file),csvPreference);
            final Object [] cols = listReader.getHeader(true);   

            int i = 0;
            int max = 10;
            
            ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
            
            ArrayList<String>list;        
            while((list = (ArrayList<String>) listReader.read()) != null){
                if(i > max){
                    break;
                }
                data.add(list);
            }
            
            table = new JTable(list.toArray(new Object [][] {}),cols);
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return table;        
    }    
}
