package ugent.rename;

/**
 *
 * @author nicolas
 */
public enum RenameError {
    TARGET_EXISTS,
    PARENT_NOT_WRITABLE,
    SYSTEM_ERROR,
    UNKNOWN_ERROR,
    FILE_NOT_FOUND,
    IO_EXCEPTION,
    UNDO_ERROR,
    SECURITY_EXCEPTION,
    //in Windows kan je bestand niet hernoemen wanneer die geopend is door een ander programma
    //in Linux wel, omdat enkel de directory entry wordt hernoemd, terwijl het andere programma naar de inode schrijf
    FILELOCK_EXCEPTION
    ;    
}
