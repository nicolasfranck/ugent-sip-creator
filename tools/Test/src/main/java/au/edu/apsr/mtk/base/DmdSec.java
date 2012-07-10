/**
 * 
 * Copyright 2008 The Australian National University (ANU)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package au.edu.apsr.mtk.base;

import org.w3c.dom.Node;

/**
 * Class representing the METS dmdSec element
 * 
 * @author Scott Yeadon
 *
 */
public class DmdSec extends MdSec
{
    /**
     * Construct a METS dmdSec
     * 
     * @param n 
     *        A w3c Node, typically an Element
     * 
     * @exception METSException
     */ 
    public DmdSec(Node n) throws METSException
    {
        super(n, Constants.ELEMENT_DMDSEC);
    }
}