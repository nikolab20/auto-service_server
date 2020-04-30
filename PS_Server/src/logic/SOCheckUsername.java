/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

/**
 *
 * @author nikol
 */
public class SOCheckUsername extends SystemOperation {

    private final String username;

    public SOCheckUsername(String username) {
        super();
        this.username = username;
    }

    @Override
    protected void operation() throws Exception {
        odo = dbbr.checkUsername(username);
    }

}
