package logic;

import java.util.Date;

/**
 *
 * @author nikol
 */
public class SOSearchNewClientsFromDate extends SystemOperation {

    /**
     * Criteria for clients search.
     */
    private final Date date;

    /**
     * Parameterized constructor for this system operation.
     *
     * @param date is criteria for search.
     */
    public SOSearchNewClientsFromDate(Date date) {
        super();
        this.date = date;
    }

    @Override
    protected void operation() throws Exception {
        listOdo = dbbr.searchNewClientsFromDate(date);
    }

}
