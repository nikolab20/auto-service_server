package logic;

/**
 *
 * @author nikol
 */
public class SOGetAllCarParts extends SystemOperation {

    /**
     * Default constructor without parameters which calls super class
     * constructor.
     */
    public SOGetAllCarParts() {
        super();
    }

    @Override
    protected void operation() throws Exception {
        listOdo = dbbr.selectAllCarParts();
    }
}
