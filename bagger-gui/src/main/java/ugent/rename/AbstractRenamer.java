package ugent.rename;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.OverlappingFileLockException;
import java.util.ArrayList;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author nicolas
 */
abstract public class AbstractRenamer {
    private RenameListener renameListener;
    private boolean copy = false;
    private boolean copyDirectoryContent = false;
    private boolean overwrite = false;
    private ArrayList<File> inputFiles;
    private boolean simulateOnly = false;
    
    public boolean isSimulateOnly() {
        return simulateOnly;
    }
    public void setSimulateOnly(boolean simulateOnly) {
        this.simulateOnly = simulateOnly;
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
    public ArrayList<File> getInputFiles() {
        if(inputFiles == null){
            inputFiles = new ArrayList<File>();
        }
        return inputFiles;
    }
    public void setInputFiles(ArrayList<File> inputFiles) {
        this.inputFiles = inputFiles;
    }
    public void rename(){
        ArrayList<RenameFilePair>pairs = getFilePairs();
        if(pairs == null){
            return;
        }
        
        int numSuccess = 0;
        RenameListener l = getRenameListener();
        
        if(!l.approveList(pairs)){
            l.onEnd(pairs,numSuccess);
            return;
        }        
        
        ErrorAction action = ErrorAction.undoAll;
        System.out.println("AbstractRenamer::rename => pair.size() is "+pairs.size());
        for(int i = 0;i < pairs.size();i++){
            RenameFilePair pair = pairs.get(i);
            System.out.println(pair.getSource()+" => "+pair.getTarget());
            pair.setSuccess(false);
            l.onRenameStart(pair,i);            
            try{
                if(isSimulateOnly()){
                    if(!isOverwrite() && pair.getTarget().exists()){                        
                        throw new TargetExistsException("target file "+pair.getTarget().getAbsolutePath()+" already exists");                        
                    }else if(!pair.getTarget().getParentFile().canWrite()){
                        throw new ParentNotWritableException("cannot write to "+pair.getTarget().getParentFile().getAbsolutePath());                        
                    }else{
                        pair.setSuccess(true);
                    }
                }else{
                    //zorg ervoor dat geen enkel ander process bezig is met deze bestanden!
                    //vooral van belang in Windows, niet in Linux
                    //FileChannel channelSource = new RandomAccessFile(pair.getSource(),"rw").getChannel();                                        
                    //channelSource.lock();                    
                    
                    if(!isOverwrite() && pair.getTarget().exists()){
                        System.out.println("target file "+pair.getTarget().getAbsolutePath()+" already exists");
                        throw new TargetExistsException("target file "+pair.getTarget().getAbsolutePath()+" already exists");                        
                    }else{
                        System.out.println("making target directories");
                        pair.getTarget().getParentFile().mkdirs();                                           
                        System.out.println("making target directories ok");
                        if(isCopy()){
                            System.out.println("copy!");
                            copy(pair.getSource(),pair.getTarget());
                        }else{
                            System.out.println("move!");
                            /*
                                *  TODO: in Windows wordt target niet overschreven.
                                Zie: http://stackoverflow.com/questions/595631/how-to-atomically-rename-a-file-in-java-even-if-the-dest-file-already-exists
                                */                              
                            pair.setSuccess(pair.getSource().renameTo(pair.getTarget()));                              
                        }                        
                    }
                }
                if(pair.isSuccess()){
                    numSuccess++;
                }
            }catch(TargetExistsException e){
                System.out.println("error occurred: "+e.getMessage());
                e.printStackTrace();
                action = l.onError(pair,RenameError.TARGET_EXISTS,e.getMessage(),i);                                        
            }catch(ParentNotWritableException e){                
                System.out.println("error occurred: "+e.getMessage());
                e.printStackTrace();
                action = l.onError(pair,RenameError.PARENT_NOT_WRITABLE,e.getMessage(),i);
            }catch(FileNotFoundException e){
                System.out.println("error occurred: "+e.getMessage());
                e.printStackTrace();
                action = l.onError(pair,RenameError.FILE_NOT_FOUND,e.getMessage(),i);
            }catch(IOException e){
                System.out.println("error occurred: "+e.getMessage());
                e.printStackTrace();
                action = l.onError(pair,RenameError.IO_EXCEPTION,e.getMessage(),i);
            }catch(SecurityException e){
                System.out.println("error occurred: "+e.getMessage());
                e.printStackTrace();
                action = l.onError(pair,RenameError.SECURITY_EXCEPTION,e.getMessage(),i);
            }
            /*
            catch(OverlappingFileLockException e){
                System.out.println("error occurred: "+e.getMessage());
                e.printStackTrace();
                action = l.onError(pair,RenameError.FILELOCK_EXCEPTION,e.getMessage(),i);
        }*/
            catch(Exception e){
                System.out.println("error occurred: "+e.getMessage());
                e.printStackTrace();
                action = l.onError(pair,RenameError.UNKNOWN_ERROR,e.getMessage(),i);
            }
            System.out.println("is Success: "+(pair.isSuccess() ? "yes":"no"));
            System.out.println("target "+pair.getTarget()+" exists: "+(pair.getTarget().exists()));
            System.out.println("action to take:" + action);
            /* check if renaming operation was successful */
            if(!pair.isSuccess()){                    
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
                            if(t.isSuccess()){                                
                                t.getSource().getParentFile().mkdirs();
                                try{
                                    if(isCopy()){
                                        if(t.getTarget().exists()){
                                            t.getTarget().delete();
                                        }
                                    }else{
                                        t.setSuccess(!t.getTarget().renameTo(t.getSource()));
                                    }
                                }catch(Exception e){                                      
                                    l.onError(pair,RenameError.UNDO_ERROR,e.getMessage(), i);
                                }                               
                            }
                        }
                    }
                    l.onRenameEnd(pair, i);
                    break;
                }else if(action == ErrorAction.abort){
                    /* abort */
                    l.onRenameEnd(pair, i);
                    break;
                }
            }
            l.onRenameEnd(pair, i);
        }        
        l.onEnd(pairs,numSuccess);
    }    
    abstract protected ArrayList<RenameFilePair> getFilePairs();
}
