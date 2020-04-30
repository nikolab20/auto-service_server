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
public class SOGetAllCarParts extends SystemOperation {

    public SOGetAllCarParts() {
        super();
    }

    @Override
    protected void operation() throws Exception {
        listOdo = dbbr.selectAllCarParts();
    }
}
