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
    private String name = "";
    private String suffix = "";
    private String prefix = "";
    private int numPadding = -1;
    private char tokenPadding = '0';
    private boolean simulateOnly = false;
    private ArrayList<File>renameCandidates = new ArrayList<File>();        
    private int startNumber = 0;    
    private int jumpNumber = 1;
    private boolean preserveExtension = true;

    public boolean isPreserveExtension() {
        return preserveExtension;
    }
    public void setPreserveExtension(boolean preserveExtension) {
        this.preserveExtension = preserveExtension;
    }    
    public int getJumpNumber() {
        return jumpNumber;
    }
    public void setJumpNumber(int jumpNumber) {        
        if(jumpNumber < 0)throw new IllegalArgumentException("jumpNumber must be greater or equal to zero");
        this.jumpNumber = jumpNumber;
    }
    public int getStartNumber() {
        return startNumber;
    }
    public void setStartNumber(int startNumber) {
        this.startNumber = startNumber;
    }
    public char getTokenPadding() {
        return tokenPadding;
    }
    public void setTokenPadding(char tokenPadding) {
        this.tokenPadding = tokenPadding;
    }    
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
        if(numPadding < 0)throw new IllegalArgumentException("numPadding must be greater or equal to zero");
        this.numPadding = numPadding;
    }
    public String getPrefix() {
        return prefix;
    }
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
    public String getSuffix() {
        return suffix;
    }
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }   
    public boolean isSimulateOnly() {
        return simulateOnly;
    }
    public void setSimulateOnly(boolean simulateOnly) {
        this.simulateOnly = simulateOnly;
    }
    public ArrayList<RenameFilePair>findRenamePairs(ArrayList<File>files){
        ArrayList<RenameFilePair>pairs = new ArrayList<RenameFilePair>();
        int i = getStartNumber();
        
        for(File file:files){     
            
            //extract parts
            String currentBasename = file.getName();
            String currentExtension = "";
            String newName = "";
            int posDot = currentBasename.lastIndexOf('.');
            if(posDot >= 0){
                currentExtension = currentBasename.substring(posDot + 1);
                newName = currentBasename.substring(0,posDot);
            }else{
                newName = currentBasename;
            }

            //start
            if(getName() != null && !getName().isEmpty())
                newName = getName();            
            String paddedNum = getNumPadding() > 0 ? numWithPadding(i,getTokenPadding(),getNumPadding()):""+i;            
            String newFileName = 
                    getPrefix()+
                    newName+                    
                    paddedNum+
                    getSuffix()+
                    (isPreserveExtension() ? "."+currentExtension:".");            
            
            RenameFilePair pair = new RenameFilePair(file,new File(
                    file.getParent()+"/"+newFileName
            ));
            pair.setSimulateOnly(isSimulateOnly());
            pairs.add(pair);
            i += getJumpNumber();
        }
        return pairs;
    }
    public static String numWithPadding(int num,char tokenPadding,int numPadding){
        String format = "%"+tokenPadding+numPadding+"d";       
        return String.format(format,num);
    }
    public void rename(ArrayList<RenameFilePair>pairs){
        for(int i = 0;i < pairs.size();i++){          
            RenameFilePair pair = pairs.get(i);
        
            System.out.println(
                    pair.getSource().getAbsolutePath()+
                    " => "+
                    pair.getTarget().getAbsolutePath()
            );
            if(!pair.isSimulateOnly()){                
                pair.getTarget().mkdirs();
                pair.setSuccess(
                        pair.getSource().renameTo(pair.getTarget())
                );
                if(!pair.isSuccess())rewind(pairs,i);
            }                
        }
    }   
    public void rewind(ArrayList<RenameFilePair>pairs){
        rewind(pairs,pairs.size()-1);
    }
    public void rewind(ArrayList<RenameFilePair>pairs,int last){
        for(int i = last;i >= 0;i--){
            RenameFilePair pair = pairs.get(i);
            if(!pair.isSuccess())continue;
            pair.getTarget().renameTo(pair.getSource());
        }
    }
    public static void main(String [] args){
        try{
            SimpleRenamer renamer = new SimpleRenamer();
            renamer.setRenameCandidates(helper.FileUtils.listFiles("/home/nicolas/bhsl-pap"));
            renamer.setPrefix("prefix-");
            renamer.setNumPadding(4);
            renamer.setSimulateOnly(true);
            renamer.rename(
                renamer.findRenamePairs(renamer.getRenameCandidates())
            );
        }catch(Exception e){
            e.printStackTrace();
        }            
    }
}
