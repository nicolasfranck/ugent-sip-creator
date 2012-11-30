package ugent.bagger.bagitmets;

import gov.loc.repository.bagit.Manifest.Algorithm;
import gov.loc.repository.bagit.utilities.MessageDigestHelper;
import java.io.File;
import java.io.FileInputStream;
import org.swordapp.client.AuthCredentials;
import org.swordapp.client.Deposit;
import org.swordapp.client.DepositReceipt;
import org.swordapp.client.SWORDClient;
import org.swordapp.client.SWORDCollection;
import org.swordapp.client.SWORDWorkspace;
import org.swordapp.client.ServiceDocument;
import org.swordapp.client.UriRegistry;


/**
 *
 * @author nicolas
 */
public class SwordDepositSimpleArchive {
    public static void main(String [] args){
        try{
            String username = "nicolas.franck@ugent.be";
            String password = "admin";            
            String sdURL = "http://localhost:8080/swordv2/servicedocument";
            AuthCredentials authc = new AuthCredentials(username,password);
            
            System.out.println("test1");
            SWORDClient client = new SWORDClient();
            System.out.println("test2");
            ServiceDocument sd = client.getServiceDocument(sdURL,authc);
            System.out.println("test3");
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
            
            System.out.println("test4");
            
            File file = new File("/home/nicolas/simplezip.zip");
            String checksum = MessageDigestHelper.generateFixity(file,Algorithm.MD5);
            
            Deposit deposit = new Deposit();
            deposit.setFile(new FileInputStream(file));
            deposit.setFilename(file.getName());
            deposit.setMimeType("application/zip");
            
            
            deposit.setPackaging(UriRegistry.PACKAGE_SIMPLE_ZIP);
            deposit.setMd5(checksum);
            deposit.setInProgress(true);
            
            System.out.println("test5");
            
            DepositReceipt receipt = client.deposit(col,deposit,authc);
            
            System.out.println("status code: "+receipt.getStatusCode());
            System.out.println("treatment: "+receipt.getTreatment());
            System.out.println("link: "+receipt.getContentLink().getHref());
                        
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
