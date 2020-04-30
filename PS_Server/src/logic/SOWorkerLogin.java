package logic;

/**
 * @author nikol
 */
public class SOWorkerLogin extends SystemOperation {

    private final String username;
    private final String password;

    public SOWorkerLogin(String username, String password) {
        super();
        this.username = username;
        this.password = password;
    }

    @Override
    protected void operation() throws Exception {
        odo = dbbr.loginWorker(username, password);
    }
}
