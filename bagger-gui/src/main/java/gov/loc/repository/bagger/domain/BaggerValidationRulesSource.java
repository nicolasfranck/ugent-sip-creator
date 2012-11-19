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


public class BaggerValidationRulesSource extends DefaultRulesSource {
	
    public BaggerValidationRulesSource() {
        super();
        
        //ugent bagger      
        
        Rules mdWrapRules = new Rules(MdSec.MdWrap.class);
        mdWrapRules.add(required("ID"));
        addRules(mdWrapRules);
        
        Rules csvParseParamsRules = new Rules(CSVParseParams.class);
        for(String key:new String []{"quoteChar","delimiterChar","surroundingSpacesNeedQuotes","file"}){
            csvParseParamsRules.add(required(key));
        }
        
        addRules(csvParseParamsRules);
        
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
                System.out.println("bagInPlace: "+o);
                return !((Boolean)o);
            }        
        }),value("outputDir",new Constraint(){
            @Override
            public boolean test(Object o) {
                ArrayList<File>list = (ArrayList<File>)o;
                System.out.println("outputDir: "+o);
                return list != null && !list.isEmpty();
            }        
        })); 
        createBagsParamsRules.add(new RequiredIfTrue("outputDir",createBagsParamsRules.all(new Constraint [] {con})));
        
        addRules(createBagsParamsRules);
    }     
}
