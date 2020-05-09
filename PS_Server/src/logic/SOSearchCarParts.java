package logic;

/**
 *
 * @author nikol
 */
public class SOSearchCarParts extends SystemOperation {

    /**
     * Criteria for car parts search.
     */
    private final Long serialNumber;

    /**
     * Parameterized constructor for this system operation.
     *
     * @param serialNumber is criteria for search.
     */
    public SOSearchCarParts(Long serialNumber) {
        super();
        this.serialNumber = serialNumber;
    }

    @Override
    protected void operation() throws Exception {
        listOdo = dbbr.searchCarPart(serialNumber);
    }
}
