package ugent.bagger.params;

import java.io.File;
import java.util.ArrayList;
import org.supercsv.prefs.CsvPreference;

/**
 *
 * @author nicolas
 */
public class CSVParseParams { 
    
    CsvPreference csvPreference = CsvPreference.STANDARD_PREFERENCE;    
    CSVDelimiterChar delimiterChar = CSVDelimiterChar.COMMA;
    CSVQuoteChar quoteChar = CSVQuoteChar.DOUBLE_QUOTE;
    String endOfLineSymbols = CsvPreference.STANDARD_PREFERENCE.getEndOfLineSymbols();
    boolean surroundingSpacesNeedQuotes = CsvPreference.STANDARD_PREFERENCE.isSurroundingSpacesNeedQuotes();
    ArrayList<File> files;
    
    public ArrayList<File> getFiles() {
        if(files == null){
            files = new ArrayList<File>();
        }
        return files;
    }
    public void setFiles(ArrayList<File> files) {        
        this.files = files;
    }    
    public String getEndOfLineSymbols() {
        return endOfLineSymbols;
    }
    public void setEndOfLineSymbols(String endOfLineSymbols) {
        this.endOfLineSymbols = endOfLineSymbols;
    }    
    public boolean isSurroundingSpacesNeedQuotes() {
        return surroundingSpacesNeedQuotes;
    }
    public void setSurroundingSpacesNeedQuotes(boolean surroundingSpacesNeedQuotes) {
        this.surroundingSpacesNeedQuotes = surroundingSpacesNeedQuotes;
    }
    public CSVDelimiterChar getDelimiterChar() {
        return delimiterChar;
    }
    public void setDelimiterChar(CSVDelimiterChar delimiterChar) {
        this.delimiterChar = delimiterChar;
    }
    public CSVQuoteChar getQuoteChar() {
        return quoteChar;
    }
    public void setQuoteChar(CSVQuoteChar quoteChar) {
        this.quoteChar = quoteChar;
    }
}
