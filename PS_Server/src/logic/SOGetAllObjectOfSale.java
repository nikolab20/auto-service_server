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
public class SOGetAllObjectOfSale extends SystemOperation {

    private String criteria;

    public SOGetAllObjectOfSale(String criteria) {
        super();
        this.criteria = criteria;
    }

    @Override
    protected void operation() throws Exception {
        mapOdo = dbbr.getAllObjectOfSaleWithNames(criteria);
    }
}
