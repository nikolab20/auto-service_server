package logic;

/**
 *
 * @author nikol
 */
public class SOGetAllEmployees extends SystemOperation {

    /**
     * Default constructor without parameters which calls super class
     * constructor.
     */
    public SOGetAllEmployees() {
        super();
    }

    @Override
    protected void operation() throws Exception {
        listOdo = dbbr.selectAllEmployees();
    }
}
