package gov.loc.repository.bagger.domain;

import com.anearalone.mets.MdSec;
import java.io.File;
import java.util.ArrayList;
import org.springframework.binding.PropertyAccessStrategy;
import org.springframework.rules.Rules;
import org.springframework.rules.constraint.Constraint;
import org.springframework.rules.constraint.property.AbstractPropertyConstraint;
import org.springframework.rules.constraint.property.PropertyConstraint;
import org.springframework.rules.constraint.property.RequiredIfTrue;
import org.springframework.rules.support.DefaultRulesSource;
import ugent.bagger.params.CSVParseParams;
import ugent.bagger.params.CreateBagsParams;
import ugent.bagger.params.ExportParams;
import ugent.bagger.params.NewBagParams;
import ugent.bagger.params.SaveBagParams;
import ugent.bagger.params.ValidateManifestParams;


public class BaggerValidationRulesSource extends DefaultRulesSource {
	
    public BaggerValidationRulesSource() {
        super();
        
        //ugent bagger      
        
        //mdwrap
        Rules mdWrapRules = new Rules(MdSec.MdWrap.class);
        mdWrapRules.add(required("ID"));
        addRules(mdWrapRules);
        
        //csv rules
        Rules csvParseParamsRules = new Rules(CSVParseParams.class);
        for(String key:new String []{"quoteChar","delimiterChar","surroundingSpacesNeedQuotes","file"}){
            csvParseParamsRules.add(required(key));
        }
        
        addRules(csvParseParamsRules);
        
        //create bags
        Rules createBagsParamsRules = new Rules(CreateBagsParams.class);
        for(String key:new String [] {"version"}){            
            createBagsParamsRules.add(required(key));            
        }
        Constraint directoriesConstraint = new Constraint(){
            @Override
            public boolean test(Object o) {                
                ArrayList<File>directories = (ArrayList<File>)o;
                return directories != null && !directories.isEmpty();
            }            
        };
        createBagsParamsRules.add("directories",directoriesConstraint);                
           
        /*
        PropertyConstraint outputDirConstraint = new AbstractPropertyConstraint("outputDir"){
            @Override
            public boolean isDependentOn(String propertyName){
                System.out.println("propertyName: "+propertyName);
                return propertyName.equals("bagInPlace");
            }
            @Override
            protected boolean test(PropertyAccessStrategy pas) {  
                System.out.println("test outputDir");
                Boolean bagInPlace = (Boolean) pas.getPropertyValue("bagInPlace");
                ArrayList<File>outputDir = (ArrayList<File>) pas.getPropertyValue("outputDir");                
               
                System.out.println("bagInPlace:"+bagInPlace);
                System.out.println("outputDir:"+outputDir);
                if(bagInPlace){
                    System.out.println("return true");
                    return true;
                }else{
                    boolean ok = outputDir != null && !outputDir.isEmpty();
                    System.out.println("ok: "+ok);
                    return ok;
                }
            }
        };
        createBagsParamsRules.add(outputDirConstraint);
        
        PropertyConstraint bagInPlaceOutputDir = new AbstractPropertyConstraint("bagInPlace"){
            @Override
            public boolean isDependentOn(String propertyName){
                System.out.println("propertyName: "+propertyName);
                return propertyName.equals("outputDir");
            }
            @Override
            protected boolean test(PropertyAccessStrategy pas) {    
                System.out.println("test bagInPlace");
                Boolean bagInPlace = (Boolean) pas.getPropertyValue("bagInPlace");
                ArrayList<File>outputDir = (ArrayList<File>) pas.getPropertyValue("outputDir");                
               
                System.out.println("bagInPlace:"+bagInPlace);
                System.out.println("outputDir:"+outputDir);
                if(bagInPlace){
                    System.out.println("return true");
                    return true;
                }else{
                    boolean ok = outputDir != null && !outputDir.isEmpty();
                    System.out.println("ok: "+ok);
                    return ok;
                }
            }
        };       
        createBagsParamsRules.add(bagInPlaceOutputDir);*/
        
        
        Constraint metadataPaths = regexp("^(?:[a-zA-Z0-9_\\-\\.]+(?:,[a-zA-Z0-9_\\-\\.]+)*)*$");
        createBagsParamsRules.add(value("metadataPaths",metadataPaths));        
        addRules(createBagsParamsRules);
        
        //new bag
        Rules newBagParamsRules = new Rules(NewBagParams.class);
        Constraint bagIdRegex = regexp("^[a-zA-Z_][a-zA-Z0-9_\\-:\\.]*$");
        newBagParamsRules.add(value("bagId",bagIdRegex));
        addRules(newBagParamsRules);
        
        //validate manifests
        Rules validateManifestParamsRules = new Rules(ValidateManifestParams.class);        
        Constraint filesConstraint = new Constraint(){
            @Override
            public boolean test(Object o) {
                ArrayList<File>list = (ArrayList<File>)o;                
                return list != null && !list.isEmpty();
            }        
        };
        validateManifestParamsRules.add(value("files",filesConstraint));
        addRules(validateManifestParamsRules);
        
        
        //exportParams
        Rules exportParamsRules = new Rules(ExportParams.class);
        exportParamsRules.add(required("format"));
       
        exportParamsRules.add(value("outputFile",new Constraint(){
            @Override
            public boolean test(Object o) {
                ArrayList<File>list = (ArrayList<File>)o;                
                boolean ok = list != null && !list.isEmpty();
                if(ok){
                    for(File f:list){
                        //moet een bestand zijn
                        if(f.isDirectory()){
                            ok = false;
                            break;
                        }
                        //bestand wordt steeds aangemaakt, maar parent directory moet er zijn
                        else if(!f.getParentFile().exists()){
                            ok = false;
                            break;
                        }
                    }
                }
                return ok;
            }        
        }));
        addRules(exportParamsRules);
        
        //saveBagParams
        Rules saveBagParamsRules = new Rules(SaveBagParams.class);
        saveBagParamsRules.add(value("outputFile",new Constraint(){
            @Override
            public boolean test(Object o) {
                ArrayList<File>list = (ArrayList<File>)o;                
                boolean ok = list != null && !list.isEmpty();
                if(ok){
                    for(File f:list){                        
                        //bestand wordt steeds aangemaakt, maar parent directory moet er zijn
                        if(!f.getParentFile().exists()){
                            ok = false;
                            break;
                        }
                    }
                }
                return ok;
            }        
        }));
        addRules(saveBagParamsRules);
    }     
}
