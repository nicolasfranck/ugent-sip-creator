/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ugent.bagger.validation;

import ugent.bagger.params.AdvancedRenameParams;
import ugent.bagger.params.CleanParams;
import com.anearalone.mets.MdSec.MdWrap;
import com.anearalone.mets.MetsHdr.Agent;
import org.springframework.rules.Rules;
import org.springframework.rules.support.DefaultRulesSource;
import ugent.bagger.params.RenameParams;

/**
 *
 * @author nicolas
 */
public class MyValidationRulesSource extends DefaultRulesSource{
    public MyValidationRulesSource(){

        Rules advancedRenameParamsRules = new Rules(AdvancedRenameParams.class);
        advancedRenameParamsRules
                .add(required("sourcePattern"))
                .add(required("destinationPattern"));
        addRules(advancedRenameParamsRules);

        Rules cleanParamsRules = new Rules(CleanParams.class);
        addRules(cleanParamsRules);
        
        Rules agentRules = new Rules(Agent.class);
        agentRules
                .add(required("name"))
                .add(required("ID"));
        addRules(agentRules);
        
        Rules mdWrapRules = new Rules(MdWrap.class);
        mdWrapRules.add(required("ID"));
        addRules(mdWrapRules);
        
        Rules renameParamsRules = new Rules(RenameParams.class);
        renameParamsRules.add(required("source"));
        renameParamsRules.add(required("destination"));
        addRules(renameParamsRules);
                
    }
}