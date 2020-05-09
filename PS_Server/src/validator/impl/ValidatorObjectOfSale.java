package validator.impl;

import domain.PredmetProdaje;
import exception.ValidationException;
import java.math.BigDecimal;
import validator.Validator;

/**
 *
 * @author nikol
 */
public class ValidatorObjectOfSale implements Validator {

    @Override
    public void validate(Object value) throws ValidationException {
        PredmetProdaje predmetProdaje = (PredmetProdaje) value;

        if (predmetProdaje.getCena().compareTo(BigDecimal.ZERO) == -1) {
            throw new ValidationException("Cena predmeta prodaje ne moze biti manja od nule!");
        }

        if (predmetProdaje.getCenaSaPorezom().compareTo(BigDecimal.ZERO) == -1) {
            throw new ValidationException("Cena predmeta prodaje sa porezom ne moze biti manja od nule!");
        }
    }

}
