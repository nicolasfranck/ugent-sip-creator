/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package renaming;

/**
 *
 * @author nicolas
 */
public class RenamePair {
    private String sourcePattern;
    private String destinationPattern;
    public String getDestinationPattern() {
        return destinationPattern;
    }
    public void setDestinationPattern(String destinationPattern) {
        this.destinationPattern = destinationPattern;
    }
    public String getSourcePattern() {
        return sourcePattern;
    }
    public void setSourcePattern(String sourcePattern) {
        this.sourcePattern = sourcePattern;
    }
}
