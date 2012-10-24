package ugent.bagger.bagitmets;

import gov.loc.repository.bagit.Manifest;
import gov.loc.repository.bagit.Manifest.Algorithm;
import gov.loc.repository.bagit.utilities.MessageDigestHelper;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import org.swordapp.client.AuthCredentials;
import org.swordapp.client.Deposit;
import org.swordapp.client.DepositReceipt;
import org.swordapp.client.Endpoints;
import org.swordapp.client.SWORDClient;
import org.swordapp.client.SWORDCollection;
import org.swordapp.client.SWORDWorkspace;
import org.swordapp.client.ServiceDocument;
import org.swordapp.client.UriRegistry;

/**
 *
 * @author nicolas
 */
public class SwordDepositTest {
    public static void main(String [] args){
        try{
            String username = "nicolas.franck@ugent.be";
            String password = "admin";            
            String sdURL = "http://localhost:8080/swordv2/servicedocument";
            AuthCredentials authc = new AuthCredentials(username,password);
            
            SWORDClient client = new SWORDClient();
            ServiceDocument sd = client.getServiceDocument(sdURL,authc);
            for(SWORDWorkspace ws:sd.getWorkspaces()){
                System.out.println("workspace: "+ws.getTitle());
                for(SWORDCollection sc:ws.getCollections()){
                    System.out.println("\ttitle:"+sc.getTitle());
                    System.out.println("\tabstract:"+sc.getAbstract());                    
                    System.out.println("\tpolicy:"+sc.getCollectionPolicy());
                    System.out.println("\taccepts nothing:"+sc.acceptsNothing());
                    System.out.println("\tmediation:"+sc.allowsMediation());
                    System.out.println("\taccept packaging:"+sc.getAcceptPackaging());
                    
                }
            }
            SWORDCollection col = sd.getWorkspaces().get(0).getCollections().get(0);
            
            File file = new File("/home/nicolas/test/archive-ugent-be-0D38FA8E-1BFC-11E0-A53B-D862A2B3687C.zip");
            String checksum = MessageDigestHelper.generateFixity(file,Algorithm.MD5);
            
            Deposit deposit = new Deposit();
            deposit.setFile(new FileInputStream(file));
            deposit.setFilename(file.getName());
            deposit.setMimeType("application/zip");
            deposit.setPackaging(UriRegistry.PACKAGE_SIMPLE_ZIP);
            deposit.setMd5(checksum);
            deposit.setInProgress(true);
            
            DepositReceipt receipt = client.deposit(col,deposit,authc);
            
            
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
