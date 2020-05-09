package logic;

/**
 *
 * @author nikol
 */
public class SOGetAllServices extends SystemOperation {

    /**
     * Default constructor without parameters which calls super class
     * constructor.
     */
    public SOGetAllServices() {
        super();
    }

    @Override
    protected void operation() throws Exception {
        listOdo = dbbr.selectAllServices();
    }
}
