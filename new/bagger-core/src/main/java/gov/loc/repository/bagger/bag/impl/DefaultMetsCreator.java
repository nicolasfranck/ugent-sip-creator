/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.loc.repository.bagger.bag.impl;

import com.anearalone.mets.Mets;
import gov.loc.repository.bagit.Bag;

/**
 *
 * @author nicolas
 */
public class DefaultMetsCreator extends MetsCreator{
    @Override
    public Mets create(Bag bag) {
        return new Mets();
    }    
}
