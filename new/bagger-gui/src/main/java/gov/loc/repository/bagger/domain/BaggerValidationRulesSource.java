package gov.loc.repository.bagger.domain;

import com.anearalone.mets.MdSec;
import com.anearalone.mets.MetsHdr;
import org.springframework.rules.Rules;
import org.springframework.rules.support.DefaultRulesSource;
import ugent.bagger.params.AdvancedRenameParams;


public class BaggerValidationRulesSource extends DefaultRulesSource {
    /*boolean isLcProject = false;
    boolean isHoley = false;*/
	
    public BaggerValidationRulesSource() {
        super();
        
        //ugent bagger          
        Rules advancedRenameParamsRules = new Rules(AdvancedRenameParams.class);
        advancedRenameParamsRules
                .add(required("sourcePattern"))
                .add(required("destinationPattern"));
        addRules(advancedRenameParamsRules);
       
        Rules agentRules = new Rules(MetsHdr.Agent.class);
        agentRules
                .add(required("name"))
                .add(required("ID"));
        addRules(agentRules);        
        Rules mdWrapRules = new Rules(MdSec.MdWrap.class);
        mdWrapRules.add(required("ID"));
        addRules(mdWrapRules);
       
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
