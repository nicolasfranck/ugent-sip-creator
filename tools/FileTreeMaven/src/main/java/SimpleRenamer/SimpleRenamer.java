/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package SimpleRenamer;

import RenameWandLib.RenameFilePair;
import RenameWandLib.RenameListener;
import RenameWandLib.RenameListenerAdapter;
import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author nicolas
 */
public class SimpleRenamer {
    private String name;
    private String suffix;
    private String prefix;
    private int numPadding;
    private boolean renumber;
    private boolean simulateOnly = false;
    private ArrayList<File>renameCandidates = new ArrayList<File>();    
    private RenameListener renameListener;

    public ArrayList<File> getRenameCandidates() {
        return renameCandidates;
    }
    public void setRenameCandidates(ArrayList<File> renameCandidates) {
        this.renameCandidates = renameCandidates;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getNumPadding() {
        return numPadding;
    }
    public void setNumPadding(int numPadding) {
        this.numPadding = numPadding;
    }
    public String getPrefix() {
        return prefix;
    }
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
    public boolean isRenumber() {
        return renumber;
    }
    public void setRenumber(boolean renumber) {
        this.renumber = renumber;
    }
    public String getSuffix() {
        return suffix;
    }
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public RenameListener getRenameListener() {
        if(renameListener == null)
            renameListener = new RenameListenerAdapter();
        return renameListener;
    }
    public void setRenameListener(RenameListener renameListener) {
        this.renameListener = renameListener;
    }
    public boolean isSimulateOnly() {
        return simulateOnly;
    }
    public void setSimulateOnly(boolean simulateOnly) {
        this.simulateOnly = simulateOnly;
    }
    public ArrayList<RenameFilePair>findRenamePairs(){
        ArrayList<RenameFilePair>pairs = new ArrayList<RenameFilePair>();
        for(File file:getRenameCandidates()){
            //extra parts
            String currentBasename = file.getName();
            String currentExtension = "";
            String currentName = "";
            int posDot = currentBasename.lastIndexOf('.');
            if(posDot >= 0){
                currentExtension = currentBasename.substring(posDot + 1);
                currentName = currentBasename.substring(0,posDot);
            }else{
                currentName = currentBasename;
            }

            //start
            
        }
        return pairs;
    }
    public void rename(){
        
    }
}
