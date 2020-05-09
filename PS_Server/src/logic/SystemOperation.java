package logic;

import database.DatabaseBroker;
import domain.DomainObject;
import exception.ValidationException;
import validator.Validator;

import java.util.List;
import java.util.Map;

/**
 * @author nikol
 */
public abstract class SystemOperation {

    /**
     * Object of class which implements validator interface.
     */
    Validator validator;

    /**
     * Object of database broker class.
     */
    DatabaseBroker dbbr;

    /**
     * The object over which the operation is performed.
     */
    DomainObject odo;

    /**
     * The list of objects over which the operation is performed.
     */
    List<DomainObject> listOdo;

    /**
     * The map of object over which the operation is performed.
     */
    Map<Object, Object> mapOdo;

    /**
     * Non-parameter constructor for system operation.
     */
    public SystemOperation() {
        dbbr = new DatabaseBroker();
    }

    /**
     * Method for checking conditions from validator.
     *
     * @throws ValidationException if the object fails validation
     */
    protected void checkPreconditions() throws ValidationException {
        if (validator != null) {
            validator.validate(odo);
        }
    }

    /**
     * Method for making connection to database.
     *
     * @throws Exception if problems with communication with the database occur.
     */
    protected void connectStorage() throws Exception {
        dbbr.connect();
    }

    /**
     * Method for disconnection from database.
     *
     * @throws Exception if problems with communication with the database occur.
     */
    protected void disconnectStorage() throws Exception {
        dbbr.disconnect();
    }

    /**
     * Method for describing a system operation.
     *
     * @throws Exception if a problem occurs while performing the operation
     */
    protected abstract void operation() throws Exception;

    /**
     * Template method for execution this operation.
     *
     * @throws Exception if a problem occurs while performing the operation
     */
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

    /**
     * Method for getting object as result of operation.
     *
     * @return object as result of operation.
     */
    public DomainObject getDomainObject() {
        return odo;
    }

    /**
     * Method for getting list of objects as result of operation.
     *
     * @return list of objects as result of operation.
     */
    public List<DomainObject> getListDomainObject() {
        return listOdo;
    }

    /**
     * Method for getting map of objects as result of operation.
     *
     * @return map of objects as result of operation.
     */
    public Map<Object, Object> getMapDomainObject() {
        return mapOdo;
    }
}
