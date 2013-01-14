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
    char delimiterChar = (char) CsvPreference.STANDARD_PREFERENCE.getDelimiterChar();
    char quoteChar = (char) CsvPreference.STANDARD_PREFERENCE.getQuoteChar();
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
    public char getDelimiterChar() {
        return delimiterChar;
    }
    public void setDelimiterChar(char delimiterChar) {
        this.delimiterChar = delimiterChar;
    }
    public String getEndOfLineSymbols() {
        return endOfLineSymbols;
    }
    public void setEndOfLineSymbols(String endOfLineSymbols) {
        this.endOfLineSymbols = endOfLineSymbols;
    }
    public char getQuoteChar() {
        return quoteChar;
    }
    public void setQuoteChar(char quoteChar) {
        this.quoteChar = quoteChar;
    }
    public boolean isSurroundingSpacesNeedQuotes() {
        return surroundingSpacesNeedQuotes;
    }
    public void setSurroundingSpacesNeedQuotes(boolean surroundingSpacesNeedQuotes) {
        this.surroundingSpacesNeedQuotes = surroundingSpacesNeedQuotes;
    }
}
