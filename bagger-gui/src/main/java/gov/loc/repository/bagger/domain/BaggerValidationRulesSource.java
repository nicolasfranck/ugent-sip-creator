package gov.loc.repository.bagger.domain;

import com.anearalone.mets.MdSec;
import org.springframework.rules.Rules;
import org.springframework.rules.support.DefaultRulesSource;
import ugent.bagger.params.CSVParseParams;


public class BaggerValidationRulesSource extends DefaultRulesSource {
    /*boolean isLcProject = false;
    boolean isHoley = false;*/
	
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
    }  
    //Nicolas Franck: deze attributen worden nergens gebruikt!
    /*
    public void init(boolean isLcProject, boolean isHoley) {
    	clear();
    	this.isLcProject = isLcProject;
    	this.isHoley = isHoley;       
        
    }    
    public void clear() {
        System.out.println("\nrules are cleared!!!\n");
    	java.util.List<Rules> empty = new java.util.ArrayList<Rules>();
    	setRules(empty);
    }*/
}
