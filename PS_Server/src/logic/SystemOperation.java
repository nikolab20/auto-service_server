package logic;

import database.DatabaseBroker;
import domain.DomainObject;
import exception.ValidationException;
import validator.Validator;

import java.util.List;

/**
 * @author nikol
 */

public abstract class SystemOperation {
    Validator validator;
    DatabaseBroker dbbr;
    DomainObject odo;
    List<DomainObject> listOdo;

    public SystemOperation() {
        dbbr = new DatabaseBroker();
    }

    protected void checkPreconditions() throws ValidationException {
        if (validator != null) {
            validator.validate(odo);
        }
    }

    protected void connectStorage() throws Exception {
        dbbr.connect();
    }

    protected void disconnectStorage() throws Exception {
        dbbr.disconnect();
    }

    protected abstract void operation() throws Exception;

    public void execute() throws Exception {
        checkPreconditions();
        connectStorage();
        try {
            operation();
            dbbr.commit();
        } catch (Exception ex) {
            dbbr.rollback();
            throw ex;
        } finally {
            disconnectStorage();
        }
    }

    public DomainObject getDomainObject() {
        return odo;
    }

    public List<DomainObject> getListDomainObject() {
        return listOdo;
    }
}
