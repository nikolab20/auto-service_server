/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import domain.DomainObject;

/**
 *
 * @author nikol
 */
public class SOInsertDomainObject extends SystemOperation {

    public SOInsertDomainObject(DomainObject odo) {
        super();
        this.odo = odo;
        //validator
    }

    @Override
    protected void operation() throws Exception {
        odo = dbbr.insertDomainObject(odo);
    }
}
