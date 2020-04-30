/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package validator.impl;

import domain.Klijent;
import exception.ValidationException;
import java.math.BigDecimal;
import validator.Validator;

/**
 *
 * @author nikol
 */
public class ValidatorCustomer implements Validator {

    @Override
    public void validate(Object value) throws ValidationException {
        Klijent klijent = (Klijent) value;

        if (!klijent.getImeKlijenta().matches("[A-Z][a-z]*")) {
            throw new ValidationException("Ime klijenta mora poceti velikim slovom i mora"
                    + "sadrzati samo slova!");
        }

        if (!klijent.getPrezimeKlijenta().matches("[A-Z][a-z]*")) {
            throw new ValidationException("Prezime klijenta mora poceti velikim slovom i mora"
                    + "sadrzati samo slova!");
        }

        if (klijent.getBrojPoseta() < 0) {
            throw new ValidationException("Broj poseta ne moze biti manji od nule!");
        }

        if (klijent.getDug().compareTo(BigDecimal.ZERO) != 1) {
            throw new ValidationException("Dug ne moze biti manji od nule!");
        }
    }

}
