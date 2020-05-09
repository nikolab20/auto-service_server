package logic;

/**
 * @author nikol
 */
public class SOWorkerLogin extends SystemOperation {

    /**
     * Username of requested user.
     */
    private final String username;

    /**
     * Password of requested user.
     */
    private final String password;

    /**
     * Parameterized constructor for this system operation.
     *
     * @param username is username of requested user.
     * @param password is password of requested user.
     */
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
