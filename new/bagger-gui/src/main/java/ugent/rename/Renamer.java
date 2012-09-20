package ugent.rename;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FileUtils;

/*
 *  Nicolas Franck
 */


public class Renamer {
    private String source;
    private String destination;
    private Pattern sourcePattern;   
    private boolean ignoreCase = false;
    private ArrayList<File> inputFiles;
    private boolean simulateOnly = false;
    private RenameListener renameListener;
    private boolean copy = false;
    private boolean copyDirectoryContent = false;
    private boolean overwrite = false;    
    
    public Renamer(){
        
    }
    public void copy(File in,File out) throws FileNotFoundException, IOException{
        if(in.isFile()){
            FileUtils.copyFile(in, out);
        }else if(in.isDirectory()){
            if(copyDirectoryContent) {
                FileUtils.copyDirectory(in, out);
            }
            else {
                FileUtils.copyFile(in, out);
            }
        }
    }
    public boolean isCopyDirectoryContent() {
        return copyDirectoryContent;
    }
    public void setCopyDirectoryContent(boolean copyDirectoryContent) {
        this.copyDirectoryContent = copyDirectoryContent;
    }    
    public boolean isOverwrite() {
        return overwrite;
    }
    public void setOverwrite(boolean overwrite) {
        this.overwrite = overwrite;
    }    
    public boolean isCopy() {
        return copy;
    }
    public void setCopy(boolean copy) {
        this.copy = copy;
    }    
    public boolean isIgnoreCase() {
        return ignoreCase;
    }
    public void setIgnoreCase(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
    }
    
    public ArrayList<File> getInputFiles() {
        return inputFiles;
    }
    public void setInputFiles(ArrayList<File> inputFiles) {
        this.inputFiles = inputFiles;
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
    public boolean isSimulateOnly() {
        return simulateOnly;
    }
    public void setSimulateOnly(boolean simulateOnly) {
        this.simulateOnly = simulateOnly;
    }

    private Pattern getSourcePattern() {                    
        try{
            if(isIgnoreCase()){
                sourcePattern = Pattern.compile(source,Pattern.CASE_INSENSITIVE);
            }else{
                sourcePattern = Pattern.compile(source);               
            }               
        }catch(Exception e){
            e.printStackTrace();
        }       
        return sourcePattern;
    }
    public void setSourcePattern(Pattern sourcePattern) {
        this.sourcePattern = sourcePattern;
    }    
    public String getSource() {
        if(source == null){
            source = "";
        }
        return source;
    }
    public void setSource(String source) {        
        this.source = source;               
    }  
    public String getDestination(){
        if(destination == null){
            destination = "";
        }
        return destination;
    }
    public void setDestination(String destination) {
        this.destination = destination;
               
    }
    public void rename(){
        ArrayList<RenameFilePair>pairs = getFilePairs();
        if(pairs == null){
            return;
        }
        System.out.println("pairs: "+pairs.size());
        RenameListener l = getRenameListener();
        if(!l.approveList(pairs)){
            return;
        }
        System.out.println("approved!");
        int numSuccess = 0;
        ErrorAction action = ErrorAction.undoAll;
        for(int i = 0;i < pairs.size();i++){
            RenameFilePair pair = pairs.get(i);
            pair.setSuccess(false);
            l.onRenameStart(pair, i);            
            try{
                if(isSimulateOnly()){
                    if(!isOverwrite() && pair.getTarget().exists()){
                        pair.setSuccess(false);
                        action = l.onError(pair,RenameError.TARGET_EXISTS,"target file "+pair.getTarget().getAbsolutePath()+" already exists",i);                        
                    }else if(!pair.getTarget().getParentFile().canWrite()){
                        action = l.onError(pair,RenameError.IO_EXCEPTION,"cannot write to "+pair.getTarget().getParentFile().getAbsolutePath(),i);
                    }else{
                        pair.setSuccess(true);
                    }
                }else{
                    if(!isOverwrite() && pair.getTarget().exists()){
                        pair.setSuccess(false);
                        action = getRenameListener().onError(pair,RenameError.TARGET_EXISTS,"target file "+pair.getTarget().getAbsolutePath()+" already exists",i);
                    }else{
                        pair.getTarget().getParentFile().mkdirs();
                        String error = null;
                        try{
                            if(isCopy()){
                                copy(pair.getSource(),pair.getTarget());
                            }else{
                                /*
                                    *  TODO: in Windows wordt target niet overschreven.
                                    Zie: http://stackoverflow.com/questions/595631/how-to-atomically-rename-a-file-in-java-even-if-the-dest-file-already-exists
                                    */                              
                                pair.setSuccess(pair.getSource().renameTo(pair.getTarget()));                              
                            }
                        }catch(Exception e){                            
                            pair.setSuccess(false);
                            error = e.getMessage();
                        }
                        if(!pair.isSuccess()){
                            action = getRenameListener().onError(pair,RenameError.SYSTEM_ERROR,error,i);
                        }
                    }
                }
                if(pair.isSuccess()){
                    numSuccess++;
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            /* check if renaming operation was successful */
            if(!pair.isSuccess()){
                System.out.println("failed!");
                /* take action */
                if(action == ErrorAction.retry){
                    /* retry rename operation */
                    i--;
                }else if(action == ErrorAction.skip){
                    /* skip to next file/directory */
                    continue;
                }else if(action == ErrorAction.undoAll){

                    if(!isSimulateOnly()){                        
                        for(int j = i ; j >= 0; j--){
                            final RenameFilePair t = pairs.get(j);
                            if(t.success){
                                String error = null;
                                t.source.getParentFile().mkdirs();
                                try{
                                    if(isCopy()){
                                        if(t.target.exists()){
                                            t.target.delete();
                                        }
                                    }else{
                                        t.success = !t.target.renameTo(t.source);
                                    }
                                }catch(SecurityException e){                                      
                                    t.success = true;
                                    error = e.getMessage();
                                }                               
                            }
                        }
                    }
                    break;
                }else if(action == ErrorAction.abort){
                    /* abort */
                    break;
                }
            }
            l.onRenameEnd(pair, i);
        }        
        l.onEnd(pairs,numSuccess);
    }
    private ArrayList<RenameFilePair> getFilePairs(){
        Pattern sp = getSourcePattern();       
        if(
            sp == null ||             
            getInputFiles().size() <= 0
        ){            
            return null;
        }
        ArrayList<RenameFilePair>pairs = new ArrayList<RenameFilePair>();        
        for(int i = 0;i<getInputFiles().size();i++){
            File sourceFile = getInputFiles().get(i);
            String baseName = sourceFile.getName();
            Matcher matcher = sp.matcher(baseName);                            
            String newName = matcher.replaceAll(getDestination()); 
            if(newName.compareTo(baseName) == 0){
                continue;
            }
            File destinationFile = new File(sourceFile.getParentFile(),newName);
            pairs.add(new RenameFilePair(sourceFile,destinationFile));
        }
        return pairs;
    }
    /*
    public static void main(String [] args){
        Renamer renamer = new Renamer();
        ArrayList<File>files = FUtils.listFiles(new File("/home/nicolas/xml"));
        System.out.println("inputFiles: "+files.size());
        renamer.setInputFiles(files);
        renamer.setSource("pp.xml");
        renamer.setDestination(".xml");
        //renamer.setSimulateOnly(true);
        renamer.setRenameListener(new RenameListener(){
            @Override
            public boolean approveList(ArrayList<RenameFilePair> list) {
                return true;
            }
            @Override
            public ErrorAction onError(RenameFilePair pair, RenameError errorType, String errorStr, int index) {
                return ErrorAction.undoAll;
            }
            @Override
            public void onRenameStart(RenameFilePair pair, int index) {
                System.out.println("source: "+pair.getSource()+" => "+pair.getTarget());
            }
            @Override
            public void onRenameSuccess(RenameFilePair pair, int index) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
            @Override
            public void onRenameEnd(RenameFilePair pair, int index) {
                
            }
            @Override
            public void onEnd(ArrayList<RenameFilePair> list, int numSuccess) {
                
            }            
        });
        renamer.rename();
    }*/
}