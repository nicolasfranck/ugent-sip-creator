package ugent.bagger.helper;

import java.io.File;

/* A FileNode is a derivative of the File class - though we delegate to 
 * the File object rather than subclassing it. It is used to maintain a 
 * cache of a directory's children and therefore avoid repeated access 
 * to the underlying file system during rendering. 
 */
class FileNode { 
    File     file; 
    Object[] children; 
    public FileNode(File file) { 
	this.file = file; 
    }
    // Used to sort the file names.
    static private MergeSort  fileMS = new MergeSort() {
        @Override
	public int compareElementsAt(int a, int b) {
	    return ((String)toSort[a]).compareTo((String)toSort[b]);
	}
    };
    /**
     * Returns the the string to be used to display this leaf in the JTree.
     */
    @Override
    public String toString() { 
	return file.getName();
    }
    public File getFile() {
	return file; 
    }
    /**
     * Loads the children, caching the results in the children ivar.
     */
    protected Object[] getChildren() {
	if (children != null) {
	    return children; 
	}
	try {
	    String[] files = file.list();
	    if(files != null) {
		fileMS.sort(files); 
		children = new FileNode[files.length]; 
		String path = file.getPath();
		for(int i = 0; i < files.length; i++) {
		    File childFile = new File(path, files[i]); 
		    children[i] = new FileNode(childFile);
		}
	    }
	} catch (SecurityException se) {}
	return children; 
    }
}