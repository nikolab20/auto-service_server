package validator.impl;

import domain.PoreskaStopa;
import exception.ValidationException;
import java.math.BigDecimal;
import validator.Validator;

/**
 *
 * @author nikol
 */
public class ValidatorTax implements Validator {

    @Override
    public void validate(Object value) throws ValidationException {
        PoreskaStopa poreskaStopa = (PoreskaStopa) value;

        if (poreskaStopa.getOznaka().length() == 0) {
            throw new ValidationException("Oznaka poreske stope mora imata vise od 0 karaktera!");
        }

        if (poreskaStopa.getVrednost().compareTo(BigDecimal.ZERO) == 0) {
            throw new ValidationException("Vrednost poreske stope mora biti razlicita od nule!");
        }
    }

}
