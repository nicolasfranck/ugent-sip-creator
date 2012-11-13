package ugent.bagger.params;

/**
 *
 * @author nicolas
 */
public class FTPParams {
    private String host = "";
    private int port = 21;
    private String userName = "";
    private String password = "";
    private FTPS_PROTOCOL protocol = FTPS_PROTOCOL.NONE;
    private boolean passiveMode = true;
    private String initialDirectory; 

    public String getInitialDirectory() {
        return initialDirectory;
    }
    public void setInitialDirectory(String initialDirectory) {
        this.initialDirectory = initialDirectory;
    }    
    public boolean isPassiveMode() {
        return passiveMode;
    }
    public void setPassiveMode(boolean passiveMode) {
        this.passiveMode = passiveMode;
    }
    public String getHost() {
        return host;
    }
    public void setHost(String host) {
        this.host = host;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public int getPort() {
        return port;
    }
    public void setPort(int port) {
        this.port = port > 0 && port <= 65536 ? port:21;
    }
    public FTPS_PROTOCOL getProtocol() {
        return protocol;
    }
    public void setProtocol(FTPS_PROTOCOL protocol) {
        this.protocol = protocol;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }    
    public enum FTPS_PROTOCOL {
        NONE("none"),TLS("TLS"),SSL("SSL");
        private String c;
        private FTPS_PROTOCOL(String c){
            this.c = c;
        }
    }
}