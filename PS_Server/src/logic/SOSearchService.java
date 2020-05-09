package logic;

/**
 *
 * @author nikol
 */
public class SOSearchService extends SystemOperation {

    /**
     * Criteria for services search.
     */
    private final Long criteria;

    /**
     * Parameterized constructor for this system operation.
     *
     * @param criteria is criteria for search.
     */
    public SOSearchService(Long criteria) {
        super();
        this.criteria = criteria;
    }

    @Override
    protected void operation() throws Exception {
        listOdo = dbbr.searchService(criteria);
    }
}
