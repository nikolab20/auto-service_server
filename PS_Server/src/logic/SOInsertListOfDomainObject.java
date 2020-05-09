package logic;

import domain.DomainObject;
import java.util.List;

/**
 *
 * @author nikol
 */
public class SOInsertListOfDomainObject extends SystemOperation {

    /**
     * Parameterized constructor for this system operation.
     *
     * @param listOdo is list object that user need to insert.
     */
    public SOInsertListOfDomainObject(List<DomainObject> listOdo) {
        this.listOdo = listOdo;
    }

    @Override
    protected void operation() throws Exception {
        dbbr.insertListOfDomainObject(listOdo);
    }

}
