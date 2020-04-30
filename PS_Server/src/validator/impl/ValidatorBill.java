/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package validator.impl;

import domain.Racun;
import exception.ValidationException;
import java.math.BigDecimal;
import java.util.Date;
import validator.Validator;

/**
 *
 * @author nikol
 */
public class ValidatorBill implements Validator {

    @Override
    public void validate(Object value) throws ValidationException {
        Racun racun = (Racun) value;

        if (racun.getDatumIzdavanja().before(new Date()) || racun.getDatumIzdavanja().after(new Date())) {
            throw new ValidationException("Datum iydavanja racuna ne moze biti ni pre ni posle danasnjeg datuma!");
        }

        if (racun.getUkupnaVrednost().compareTo(BigDecimal.ZERO) == -1) {
            throw new ValidationException("Ukupna vrednost racuna ne moze biti manja od nule!");
        }

        if (racun.getUkupnaVrednostSaPorezom().compareTo(BigDecimal.ZERO) == -1) {
            throw new ValidationException("Ukupna vrednost racuna sa porezom ne moze biti manja od nule!");
        }

        if (racun.getKlijent() == null) {
            throw new ValidationException("Morate izabrati klijenta za kog se izdaje racun!");
        }
    }

}
