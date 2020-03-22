package logic;

import database.DatabaseBroker;
import domain.Radnik;

/**
 * @author nikol
 */

public class SOWorkerLogin extends SystemOperation {

    private String username;
    private String password;

    public SOWorkerLogin(String username, String password) {
        super();
        this.username = username;
        this.password = password;
        //validator=new ValidatorCredentials(username, password);
    }


    @Override
    protected void operation() throws Exception {
        odo = dbbr.loginWorker(username, password);
    }
}
