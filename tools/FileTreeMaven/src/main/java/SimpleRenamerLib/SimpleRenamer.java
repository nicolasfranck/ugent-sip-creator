/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package SimpleRenamerLib;

import RenameWandLib.ErrorAction;
import RenameWandLib.RenameError;
import RenameWandLib.RenameFilePair;
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
    private RenameListener renameListener;
    private boolean overWrite = false;
    private boolean number = true;

    public boolean isNumber() {
        return number;
    }
    public void setNumber(boolean number) {
        this.number = number;
    }
    public boolean isOverWrite() {
        return overWrite;
    }

    public void setOverWrite(boolean overWrite) {
        this.overWrite = overWrite;
    }


    public RenameListener getRenameListener() {
        if(renameListener == null){
            renameListener = new RenameListenerAdapter();
        }
        return renameListener;
    }
    public void setRenameListener(RenameListener renameListener) {
        this.renameListener = renameListener;
    }
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
        if(jumpNumber == 0)throw new IllegalArgumentException("jumpNumber must be greater or lesser than 0");        
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
     
    public void addRenameCandidate(File file){
        if(file == null || !file.isFile())throw new IllegalArgumentException("argument must be a regular file");
        this.renameCandidates.add(file);
    }
    public void removeRenameCandidate(File file){
        this.renameCandidates.remove(file);
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        if(name != null && !name.isEmpty())
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
        if(prefix != null && !prefix.isEmpty())
            this.prefix = prefix;
    }
    public String getSuffix() {
        return suffix;
    }
    public void setSuffix(String suffix) {
        if(suffix != null && !suffix.isEmpty())
            this.suffix = suffix;
    }   
    public boolean isSimulateOnly() {
        return simulateOnly;
    }
    public void setSimulateOnly(boolean simulateOnly) {
        this.simulateOnly = simulateOnly;
    }
    private ArrayList<RenameFilePair>findRenamePairs(ArrayList<File>files){

        ArrayList<RenameFilePair>pairs = new ArrayList<RenameFilePair>();

        if(!numberingWorks(files, getStartNumber(),getJumpNumber())){
            return pairs;
        }

        int i = getStartNumber();
        
        for(File file:files){                 
            
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
            if(getName() != null && !getName().isEmpty())
                newName = getName();
                     
            String paddedNum = "";
            if(isNumber())
                paddedNum = getNumPadding() > 0 ? numWithPadding(i,getTokenPadding(),getNumPadding()):""+i;

            String newFileName = 
                    getPrefix()+
                    newName+                    
                    paddedNum+
                    getSuffix()+
                    (isPreserveExtension() && posDot >= 0 ? "."+currentExtension:"");
            
            RenameFilePair pair = new RenameFilePair(file,new File(
                    file.getParent()+"/"+newFileName
            ));
            pair.setSimulateOnly(isSimulateOnly());
            pairs.add(pair);
            i += getJumpNumber();
        }
        return pairs;
    }
    private static boolean numberingWorks(ArrayList<File>files,int start,int jump){
        int endNumber = start+((files.size() - 1)*jump);
        return endNumber >= 0;
    }
    private static String numWithPadding(int num,char tokenPadding,int numPadding){
        String format = "%"+tokenPadding+numPadding+"d";       
        return String.format(format,num);
    }
    public void rename(){
        ArrayList<RenameFilePair>pairs = findRenamePairs(renameCandidates);
        getRenameListener().onInit(pairs);
        int numSuccess = _rename(pairs);
        getRenameListener().onEnd(pairs,numSuccess);
    }
    
    private int _rename(ArrayList<RenameFilePair>pairs){

        int numSuccess = 0;

        for(int i = 0;i < pairs.size();i++){

            RenameFilePair pair = pairs.get(i);            

            ErrorAction errorAction = ErrorAction.ignore;
            boolean abort = false;            
            
            getRenameListener().onRenameStart(pair,i);

            RenameError renameError = null;

            if(!pair.isSimulateOnly()){
                String errorStr = null;

                if(!isOverWrite() && pair.getTarget().exists()){
                    pair.setSuccess(false);
                    errorAction  = getRenameListener().onError(pair,RenameError.TARGET_EXISTS,"target file "+pair.getTarget().getAbsolutePath()+" already exists");
                    renameError = RenameError.TARGET_EXISTS;
                }else if(!pair.getTarget().getParentFile().canWrite()){
                    pair.setSuccess(false);
                    errorAction = getRenameListener().onError(pair,RenameError.IO_EXCEPTION,"cannot write to "+pair.getTarget().getParentFile().getAbsolutePath());
                    renameError = RenameError.IO_EXCEPTION;
                }else{

                    try{
                        pair.getTarget().getParentFile().mkdirs();
                        pair.setSuccess(pair.getSource().renameTo(pair.getTarget()));
                    }catch(SecurityException e){
                        pair.setSuccess(false);
                        errorStr = e.getMessage();
                        renameError = RenameError.SECURITY_EXCEPTION;
                    }
                    if(!pair.isSuccess()){
                       if(renameError == null)renameError = RenameError.SYSTEM_ERROR;
                        errorAction = getRenameListener().onError(pair,renameError,errorStr);
                    }
                }
                
            }else{
                if(!isOverWrite() && pair.getTarget().exists()){
                    pair.setSuccess(false);
                    errorAction  = getRenameListener().onError(pair,RenameError.TARGET_EXISTS,"target file "+pair.getTarget().getAbsolutePath()+" already exists");
                    renameError = RenameError.TARGET_EXISTS;
                }else if(!pair.getTarget().getParentFile().canWrite()){
                    pair.setSuccess(false);
                    errorAction = getRenameListener().onError(pair,RenameError.IO_EXCEPTION,"cannot write to "+pair.getTarget().getParentFile().getAbsolutePath());
                    renameError = RenameError.IO_EXCEPTION;
                }else{
                    pair.setSuccess(true);
                }
            }

            getRenameListener().onRenameEnd(pair,i);            

            if(pair.isSuccess())numSuccess++;
            else{
                if(errorAction == ErrorAction.abort)abort = true;
                else if(errorAction == ErrorAction.undoAll){
                    rewind(pairs,i);
                    abort = true;
                }else if(errorAction == ErrorAction.retry)
                    i--;
            }

            if(abort)break;
        }

        return numSuccess;

    }   
    private void rewind(ArrayList<RenameFilePair>pairs){
        rewind(pairs,pairs.size()-1);
    }
    private void rewind(ArrayList<RenameFilePair>pairs,int last){
        System.out.println("rewinding!!");
        for(int i = last;i >= 0;i--){
            RenameFilePair pair = pairs.get(i);
            System.out.println(pair.getTarget().getAbsolutePath()+" back to "+pair.getSource().getAbsolutePath());
            if(!pair.isSuccess() || pair.isSimulateOnly())continue;            
            pair.getTarget().renameTo(pair.getSource());
        }
    }
    public static void main(String [] args){
        try{
            SimpleRenamer renamer = new SimpleRenamer();
            ArrayList<File>files = helper.FileUtils.listFiles("/home/nicolas/brol");
            for(File file:files){                
                renamer.addRenameCandidate(file);
            }
            renamer.setPrefix("prefix-");
            renamer.setNumPadding(0);
            //renamer.setSimulateOnly(true);
            renamer.setName("base-");
            renamer.setRenameListener(new RenameListener(){
                @Override
                public void onInit(ArrayList<RenameFilePair> pairs) {
                    System.out.println("onInit");
                }
                @Override
                public ErrorAction onError(RenameFilePair pair, RenameError errorType, String errorStr) {
                    System.out.println("onError: "+errorType+", "+errorStr);
                    return ErrorAction.undoAll;
                }
                @Override
                public void onRenameStart(RenameFilePair pair,int index) {
                    System.out.println("onStart:"+pair.getSource().getAbsolutePath()+" => "+pair.getTarget().getAbsolutePath());
                }
                @Override
                public void onRenameSuccess(RenameFilePair pair,int index) {
                    System.out.println("onRenameSuccess");
                }
                @Override
                public void onRenameEnd(RenameFilePair pair,int index) {
                    System.out.println("onRenameEnd");
                }
                @Override
                public void onEnd(ArrayList<RenameFilePair> pairs, int numSuccess) {
                    System.out.println("onEnd");
                }
            });
            renamer.rename();
        }catch(Exception e){
            e.printStackTrace();
        }            
    }
}
