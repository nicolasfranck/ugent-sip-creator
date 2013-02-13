package ugent.rename;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

/*
 *  Nicolas Franck
 */


public class Renamer extends AbstractRenamer{      
    String source;
    String destination;  
    String prefix;
    String postfix;    
    boolean renameExtension = false;
    boolean recursive = false;
    int patternFlags = Pattern.CANON_EQ;         

    public boolean isRecursive() {
        return recursive;
    }
    public void setRecursive(boolean recursive) {
        this.recursive = recursive;
    }    
    public boolean isRenameExtension() {
        return renameExtension;
    }
    public void setRenameExtension(boolean renameExtension) {
        this.renameExtension = renameExtension;
    }   
    public String getPrefix() {
        return prefix;
    }
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
    public String getPostfix() {
        return postfix;
    }
    public void setPostfix(String postfix) {
        this.postfix = postfix;
    }    
    public int getPatternFlags() {        
        return patternFlags;
    }    
    public void setPatternFlag(int patternFlag) {        
        this.patternFlags |= patternFlag;
    }    
    public void removePatternFlag(int patternFlag){
        this.patternFlags &= ~patternFlag;
    }   

    Pattern compileSourcePattern() {                  
        Pattern sourcePattern = null;
        try{                        
            sourcePattern = Pattern.compile(getSource(),getPatternFlags());                           
        }catch(Exception e){
            log.error(e.getMessage());
        }       
        return sourcePattern;
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
    protected ArrayList<RenameFilePair>getFilePairs(ArrayList<File>list){
        if(                         
            list.size() <= 0
        ){            
            return null;
        }
        ArrayList<RenameFilePair>pairs = new ArrayList<RenameFilePair>();        
        for(int i = 0;i<list.size();i++){
            File sourceFile = list.get(i);
            
            //recursief?
            if(isRecursive() && sourceFile.isDirectory()){
                File [] children = sourceFile.listFiles();
                if(children != null){
                    ArrayList<File>lchildren = new ArrayList<File>(Arrays.asList(children));
                    ArrayList<RenameFilePair>p = getFilePairs(lchildren);
                    if(p != null){
                        pairs.addAll(p);
                    }                    
                }                
            }
            
            String baseName = sourceFile.getName();
            String extension = null;            
            
            //extensie ook mee betrekken in rename?
            if(sourceFile.isFile() && !isRenameExtension()){
                boolean isDoubleExtension = false;
                //controleer speciale gevallen
                for(String n:doubleFileExtension){
                    if(baseName.toLowerCase().endsWith("."+n)){
                        //en niet 'extension = n', want de test wordt case-insensitive uitgevoerd..
                        extension = baseName.substring(baseName.length() - n.length());
                        isDoubleExtension = true;
                        baseName = baseName.substring(0,baseName.length() - n.length() - 1);
                        break;
                    }
                }
                if(!isDoubleExtension){
                    int pos = baseName.lastIndexOf('.');
                    extension = pos >= 0 ? baseName.substring(pos + 1):"";
                    baseName = pos >= 0 ? baseName.substring(0,pos) : baseName;                    
                }                
            }            
            
            String newName = null;
            
            //indien source == "", dan worden alle lettergrenzen vervangen door de destination
            //dus niet zomaar vervangen..
            if(!getSource().isEmpty()){
                Pattern sp = compileSourcePattern();
                if(sp != null){                    
                    newName = sp.matcher(baseName).replaceAll(getDestination());                     
                }else{
                    newName = baseName;
                }               
            }else{
                newName = baseName;
            }
            
            
            if(getPrefix() != null){
                newName = getPrefix() + newName;
            }
            if(getPostfix() != null){                
                newName += getPostfix();                
            }
            if(sourceFile.isFile() && !isRenameExtension()){
                newName += (extension != null ? "."+extension : "");
            }
            
            if(newName.compareTo(sourceFile.getName()) == 0){                
                continue;
            }
            
            File destinationFile = new File(sourceFile.getParentFile(),newName);
            pairs.add(new RenameFilePair(sourceFile,destinationFile));
        }
        return pairs;
    }
    @Override
    protected ArrayList<RenameFilePair> getFilePairs(){               
        return getFilePairs(getInputFiles());        
    }
    /*
    public static void main(String [] args){
        Renamer renamer = new Renamer();
        
        
        ArrayList<File>files = new ArrayList<File>(
            Arrays.asList(
                new File("/home/njfranck/test/torename").listFiles()
            )
        );        
        
        log.debug("inputFiles: "+files.size());
        renamer.setInputFiles(files);
        renamer.setSource("TXT");
        renamer.setDestination("txt");        
        renamer.setSimulateOnly(false); 
        renamer.setRenameExtension(true);
        renamer.setRecursive(true);
        //renamer.setPrefix("rug01-");
        //renamer.setPostfix("-post");
        
        ArrayList<RenameFilePair>pairs = renamer.getFilePairs();        
        
        renamer.setRenameListener(new RenameListenerAdapter(){            
            @Override
            public boolean approveList(final ArrayList<RenameFilePair> list){
                return true;
            }
            @Override
            public ErrorAction onError(RenameFilePair pair, RenameError errorType, String errorStr, int index) {
                log.debug("error: "+errorStr);                
                return ErrorAction.skip;
            }
            @Override
            public void onRenameStart(RenameFilePair pair, int index) {
                log.debug("source: "+pair.getSource()+" => "+pair.getTarget());
            }            
        });
        renamer.rename();        
    }*/
}