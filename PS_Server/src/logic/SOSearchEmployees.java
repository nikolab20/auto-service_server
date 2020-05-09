package logic;

/**
 *
 * @author nikol
 */
public class SOSearchEmployees extends SystemOperation {

    /**
     * Criteria for employees search.
     */
    private final Long employeeID;

    /**
     * Parameterized constructor for this system operation.
     *
     * @param employeeID is criteria for search.
     */
    public SOSearchEmployees(Long employeeID) {
        super();
        this.employeeID = employeeID;
    }

    @Override
    protected void operation() throws Exception {
        listOdo = dbbr.searchEmployees(employeeID);
    }

}
