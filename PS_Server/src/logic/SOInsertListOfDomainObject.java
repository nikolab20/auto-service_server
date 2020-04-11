/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import domain.DomainObject;
import java.util.List;

/**
 *
 * @author nikol
 */
public class SOInsertListOfDomainObject extends SystemOperation {

    public SOInsertListOfDomainObject(List<DomainObject> listOdo) {
        this.listOdo = listOdo;
    }

    @Override
    protected void operation() throws Exception {
        dbbr.insertListOfDomainObject(listOdo);
    }

}
