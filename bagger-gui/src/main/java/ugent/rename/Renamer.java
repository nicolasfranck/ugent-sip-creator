package ugent.rename;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ugent.bagger.helper.SwingUtils;

/*
 *  Nicolas Franck
 */


public class Renamer extends AbstractRenamer{
    private static final Log log = LogFactory.getLog(Renamer.class);    
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
            log.debug(e.getMessage());
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
        String [] inputFiles = new String [] {
            "/pruts/a-2.txt","/pruts/b-3.txt"
        };
        ArrayList<File>files = new ArrayList<File>();
        for(String s:inputFiles){
            files.add(new File(s));
        }
        
        System.out.println("inputFiles: "+files.size());
        renamer.setInputFiles(files);
        renamer.setSource("\\d+");
        renamer.setDestination("");        
        renamer.setSimulateOnly(true);        
        renamer.setPatternFlag(Pattern.CANON_EQ);
        renamer.setRenameListener(new RenameListenerAdapter(){            
            @Override
            public boolean approveList(final ArrayList<RenameFilePair> list){
                System.out.println("approveList");
                //controleer of target niet voorkomt in source list
                int numFound = 0;
                ArrayList<String>seen = new ArrayList<String>();
                for(RenameFilePair pair:list){
                    if(!seen.contains(pair.getSource().getAbsolutePath())){
                        seen.add(pair.getSource().getAbsolutePath());
                    }else{
                        numFound++;
                    }
                    if(!seen.contains(pair.getTarget().getAbsolutePath())){
                        seen.add(pair.getTarget().getAbsolutePath());
                    }else{
                        numFound++;
                    }
                }
                System.out.println("equals? "+("a".equals("a") ? "yes":"no"));
                System.out.println("seen.size: "+seen.size());
                System.out.println("numFound: "+numFound);
                boolean approved = true;
                if(numFound > 0){
                    int answer = JOptionPane.showConfirmDialog(SwingUtils.getFrame(),"Waarschuwing: "+numFound+" dreigen overschreven te worden. Bent u zeker?");
                    approved = answer == JOptionPane.OK_OPTION;
                }
                return approved;
            }
            @Override
            public ErrorAction onError(RenameFilePair pair, RenameError errorType, String errorStr, int index) {
                System.out.println("error: "+errorStr);
                //return ErrorAction.undoAll;
                return ErrorAction.ignore;
            }
            @Override
            public void onRenameStart(RenameFilePair pair, int index) {
                System.out.println("source: "+pair.getSource()+" => "+pair.getTarget());
            }            
        });
        renamer.rename();
    }
}