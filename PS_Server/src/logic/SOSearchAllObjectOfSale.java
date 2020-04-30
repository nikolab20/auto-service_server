/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

/**
 *
 * @author nikol
 */
public class SOSearchAllObjectOfSale extends SystemOperation {

    private final String criteria;

    public SOSearchAllObjectOfSale(String criteria) {
        super();
        this.criteria = criteria;
    }

    @Override
    protected void operation() throws Exception {
        mapOdo = dbbr.getAllObjectOfSaleWithNames();
    }
}
