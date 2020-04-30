/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package validator.impl;

import domain.Usluga;
import exception.ValidationException;
import validator.Validator;

/**
 *
 * @author nikol
 */
public class ValidatorService implements Validator {

    @Override
    public void validate(Object value) throws ValidationException {
        Usluga usluga = (Usluga) value;

        if (usluga.getNazivUsluge().length() == 0) {
            throw new ValidationException("Naziv usluge mora imata vise od 0 karaktera!");
        }

        if (usluga.getOpisUsluge().length() == 0) {
            throw new ValidationException("Opis mora imata vise od 0 karaktera!");
        }
    }

}
