package ugent.bagger.wizards;

import java.io.IOException;
import java.net.SocketException;
import org.apache.commons.net.ftp.*;
import org.springframework.richclient.wizard.AbstractWizard;
import org.springframework.richclient.wizard.Wizard;
import org.springframework.richclient.wizard.WizardListener;
import ugent.bagger.forms.FTPParamsForm;
import ugent.bagger.params.FTPParams;

/**
 *
 * @author nicolas
 */
public class FTPWizard extends AbstractWizard{
    FTPParams ftpParams = new FTPParams();
    FTPParamsForm ftpParamsForm = new FTPParamsForm(ftpParams);
    FTPClient ftp;
    public FTPWizard(){       
       
        addForm(ftpParamsForm);        
        addWizardListener(new WizardListener(){
            @Override
            public void onPerformFinish(Wizard wizard, boolean bln) {
                if(!ftpParamsForm.hasErrors()){
                    ftpParamsForm.commit();
                }
                checkConnection();
            }
            @Override
            public void onPerformCancel(Wizard wizard, boolean bln) {
                
            }            
        });
    }
    @Override
    protected boolean onFinish() {        
        return true;
    }
    protected void listFiles() throws IOException{  
        for(FTPFile f:ftp.listFiles()){
            listFiles(f);
        }
    }
    protected void listFiles(FTPFile file) throws IOException{        
        System.out.println("name: "+file.getName());
        if(file.isDirectory()){
            ftp.changeWorkingDirectory(file.getName());
            for(FTPFile f:ftp.listFiles()){
                listFiles(f);
            }
            ftp.changeToParentDirectory();
        }
    }
    private void checkConnection(){
        
        System.out.println("starting ftp connection");
        if(ftpParams.getProtocol() != FTPParams.FTPS_PROTOCOL.NONE){
            ftp = new FTPSClient(ftpParams.getProtocol().toString()); 
        }else{
            ftp = new FTPClient();
        }
        try{
            ftp.connect(ftpParams.getHost(),ftpParams.getPort());            
            System.out.println("ftp connection success");
            int reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)){
                ftp.disconnect();
                System.err.println("FTP server refused connection.");
                return;
            }
            System.out.println("username: "+ftpParams.getUserName());
            System.out.println("password: "+ftpParams.getPassword());
            boolean loggedIn = ftp.login(ftpParams.getUserName(),ftpParams.getPassword());            
            System.out.println("logged in: "+loggedIn);
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)){
                ftp.disconnect();
                System.err.println("FTP login failed");
                return;
            }            
            System.out.println("ftp login successfull");
            
            System.out.println("Remote system is " + ftp.getSystemType());
            
            ftp.enterLocalPassiveMode();            
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            
            
            listFiles();
            
           
            
        }catch(SocketException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            if(ftp != null && ftp.isConnected()){
                try{
                    ftp.disconnect();
                }catch(IOException e){
                    e.printStackTrace();
                }                
            }
        }
    }
}
