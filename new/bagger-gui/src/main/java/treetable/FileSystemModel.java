/*
 * %W% %E%
 *
 * Copyright 1997, 1998 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer. 
 *   
 * - Redistribution in binary form must reproduce the above
 *   copyright notice, this list of conditions and the following
 *   disclaimer in the documentation and/or other materials
 *   provided with the distribution. 
 *   
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.  
 * 
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
 * DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR
 * RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THIS SOFTWARE OR
 * ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE 
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,   
 * SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER  
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF 
 * THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS 
 * BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 * 
 * You acknowledge that this software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any nuclear facility.
 */
package treetable;
import eu.medsea.mimeutil.MimeType;
import eu.medsea.mimeutil.MimeUtil;
import java.io.File;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import javax.swing.tree.TreePath;
import org.apache.log4j.Logger;
import ugent.bagger.helper.FileUtils;

/**
 * FileSystemModel is a TreeTableModel representing a hierarchical file 
 * system. Nodes in the FileSystemModel are FileNodes which, when they 
 * are directory nodes, cache their children to avoid repeatedly querying 
 * the real file system. 
 * 
 * @version %I% %G%
 *
 * @author Philip Milne
 * @author Scott Violet
 */

public class FileSystemModel extends AbstractTreeTableModel 
                             implements TreeTableModel {
    static {
        MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");
    }
    
    private static Logger logger = Logger.getLogger(FileSystemModel.class);

    // Names of the columns.
    static protected String[]  cNames = {"Name", "Size", "Type", "Modified"};

    // Types of the columns.
    static protected Class[]  cTypes = {TreeTableModel.class, Integer.class, String.class, Date.class};

    // The the returned file length for directories. 
    public static final Integer ZERO = new Integer(0); 

    public FileSystemModel() {        
	super(new FileNode(new File(File.separator)));        
    }
    public FileSystemModel(File file){
        super(new FileNode(file));
    }
    //
    // Some convenience methods. 
    //

    protected File getFile(Object node) {
	FileNode fileNode = ((FileNode)node);
        logger.debug("getFile: fileNode: "+fileNode);
	return fileNode.getFile();       
    }

    protected Object[] getChildren(Object node) {
	FileNode fileNode = ((FileNode)node);
        logger.debug("getChildren: fileNode: "+fileNode);
        logger.debug("getChildren: fileNode.children: "+fileNode.getChildren());       
	return fileNode.getChildren(); 
    }

    //
    // The TreeModel interface
    //

    @Override
    public int getChildCount(Object node) { 
	Object[] children = getChildren(node);
        logger.debug("getChildCount: children: "+children);
	return (children == null) ? 0 : children.length;
    }

    @Override
    public Object getChild(Object node, int i){
        Object [] children = getChildren(node);
        logger.debug("getChild: children: "+children);
        if(children == null)return new Object [] {};
        else if(i >= children.length)return new Object [] {};
        else return children[i];
	//return getChildren(node)[i];
    }

    // The superclass's implementation would work, but this is more efficient. 
    @Override
    public boolean isLeaf(Object node) {
        File file = getFile(node);
        return file == null ? false:file.isFile();
        //return getFile(node).isFile();
    }

    //
    //  The TreeTableNode interface. 
    //

    @Override
    public int getColumnCount() {
	return cNames.length;
    }

    @Override
    public String getColumnName(int column) {
	return cNames[column];
    }

    @Override
    public Class getColumnClass(int column) {
	return cTypes[column];
    }
 
    @Override
    public Object getValueAt(Object node, int column) {
	File file = getFile(node);
        
        logger.debug("getValueAt: node: "+node);
        logger.debug("getValueAt: column: "+column);
        logger.debug("getValueAt: file: "+file);
        logger.debug("getValueAt: file.path: "+file.getAbsolutePath());
        logger.debug("getValueAt: file.list: "+file.list());

        if(file == null)return null;
        else if(!file.exists()){
            this.fireTreeNodesRemoved(this,new TreePath(this.getRoot()).getPath(),new int [] {},new Object [] {});
            return null;
        }
        

        String [] listFileNames = null;
        try{
            listFileNames = file.list();
        }catch(Exception e){
            e.printStackTrace();
        }

	try {
	    switch(column) {
	    case 0:
		return file.isFile() ? file.getName():"";
	    case 1:
                if(file.isFile())return FileUtils.sizePretty(file.length());
                else if(file.isDirectory()){
                    if(!file.canRead() || listFileNames == null)return "(access denied)";
                    return listFileNames.length+" files";
                }                
	    case 2:
                if(file.isFile()){                    
                    Collection mimes = MimeUtil.getMimeTypes(file);
                    if(!mimes.isEmpty()){
                        Iterator it = mimes.iterator();
                        while(it.hasNext()){                          
                            return ((MimeType)it.next()).toString();
                        }
                    }else{
                        return "application/octet-stream";
                    }
                }else{
                    return "";
                }		
	    case 3:
		return new Date(file.lastModified());
	    }
	}
	catch(SecurityException se){ 
            se.printStackTrace();
        }   
	return null; 
    }
}