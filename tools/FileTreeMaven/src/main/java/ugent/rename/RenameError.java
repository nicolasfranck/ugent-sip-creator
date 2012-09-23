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
    SECURITY_EXCEPTION;    
}
