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
public class SOSearchTax extends SystemOperation {

    private final Long id;

    public SOSearchTax(Long id) {
        super();
        this.id = id;
    }

    @Override
    protected void operation() throws Exception {
        listOdo = dbbr.searchTax(id);
    }
}
