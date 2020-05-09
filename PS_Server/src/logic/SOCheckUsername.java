package logic;

/**
 *
 * @author nikol
 */
public class SOCheckUsername extends SystemOperation {

    /**
     * Username of requested user.
     */
    private final String username;

    /**
     * Parameterized constructor for this system operation.
     *
     * @param username is username of requested user.
     */
    public SOCheckUsername(String username) {
        super();
        this.username = username;
    }

    @Override
    protected void operation() throws Exception {
        odo = dbbr.checkUsername(username);
    }

}
