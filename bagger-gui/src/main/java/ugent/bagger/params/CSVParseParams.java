package ugent.bagger.params;

import java.io.File;
import org.supercsv.prefs.CsvPreference;

/**
 *
 * @author nicolas
 */
public class CSVParseParams {   
    private CsvPreference csvPreference = CsvPreference.STANDARD_PREFERENCE;
    private char delimiterChar = (char) CsvPreference.STANDARD_PREFERENCE.getDelimiterChar();
    private char quoteChar = (char) CsvPreference.STANDARD_PREFERENCE.getQuoteChar();
    private String endOfLineSymbols = CsvPreference.STANDARD_PREFERENCE.getEndOfLineSymbols();
    private boolean surroundingSpacesNeedQuotes = CsvPreference.STANDARD_PREFERENCE.isSurroundingSpacesNeedQuotes();
    private File file;

    public File getFile() {
        return file;
    }
    public void setFile(File file) {
        this.file = file;
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
