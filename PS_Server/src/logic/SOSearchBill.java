package logic;

/**
 *
 * @author nikol
 */
public class SOSearchBill extends SystemOperation {

    /**
     * Criteria for bills search.
     */
    private final Long criteria;

    /**
     * Parameterized constructor for this system operation.
     *
     * @param criteria is criteria for search.
     */
    public SOSearchBill(Long criteria) {
        super();
        this.criteria = criteria;
    }

    @Override
    protected void operation() throws Exception {
        listOdo = dbbr.searchBill(criteria);
    }

}
