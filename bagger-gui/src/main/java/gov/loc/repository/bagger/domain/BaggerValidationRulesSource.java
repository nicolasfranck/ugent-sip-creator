package gov.loc.repository.bagger.domain;

import com.anearalone.mets.MdSec;
import java.io.File;
import java.util.ArrayList;
import org.springframework.rules.Rules;
import org.springframework.rules.constraint.Constraint;
import org.springframework.rules.constraint.property.RequiredIfTrue;
import org.springframework.rules.support.DefaultRulesSource;
import ugent.bagger.params.CSVParseParams;
import ugent.bagger.params.CreateBagsParams;
import ugent.bagger.params.NewBagParams;
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
        Constraint con = ifTrue(value("bagInPlace",new Constraint(){
            @Override
            public boolean test(Object o) {                
                return !((Boolean)o);
            }        
        }),value("outputDir",new Constraint(){
            @Override
            public boolean test(Object o) {
                ArrayList<File>list = (ArrayList<File>)o;                
                return list != null && !list.isEmpty();
            }        
        })); 
        createBagsParamsRules.add(new RequiredIfTrue("outputDir",createBagsParamsRules.all(new Constraint [] {con})));        
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
    }     
}
