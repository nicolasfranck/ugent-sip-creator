package ugent.rename;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 *  Nicolas Franck
 */


public class Renamer extends AbstractRenamer{
    private String source;
    private String destination;   
    private int patternFlags = Pattern.CANON_EQ;       
    
    public int getPatternFlags() {        
        return patternFlags;
    }    
    public void setPatternFlag(int patternFlag) {        
        this.patternFlags |= patternFlag;
    }    
    public void removePatternFlag(int patternFlag){
        this.patternFlags &= ~patternFlag;
    }   

    private Pattern compileSourcePattern() {                  
        Pattern sourcePattern = null;
        try{                        
            sourcePattern = Pattern.compile(source,getPatternFlags());                           
        }catch(Exception e){
            e.printStackTrace();
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
    
    @Override
    protected ArrayList<RenameFilePair> getFilePairs(){
        Pattern sp = compileSourcePattern();       
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
    
    public static void main(String [] args){
        Renamer renamer = new Renamer();
        ArrayList<File>files = FUtils.listFiles(new File("/home/nicolas/xml"));
        System.out.println("inputFiles: "+files.size());
        renamer.setInputFiles(files);
        renamer.setSource("\\.XML");
        renamer.setDestination("pp.xml");
        renamer.setSimulateOnly(true);
        renamer.setPatternFlag(Pattern.LITERAL);
        renamer.setRenameListener(new RenameListener(){
            @Override
            public boolean approveList(ArrayList<RenameFilePair> list) {
                return true;
            }
            @Override
            public ErrorAction onError(RenameFilePair pair, RenameError errorType, String errorStr, int index) {
                System.out.println("error: "+errorStr);
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
    }
}