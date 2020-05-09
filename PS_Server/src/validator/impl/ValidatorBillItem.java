package validator.impl;

import domain.StavkaRacuna;
import exception.ValidationException;
import java.math.BigDecimal;
import validator.Validator;

/**
 *
 * @author nikol
 */
public class ValidatorBillItem implements Validator {

    @Override
    public void validate(Object value) throws ValidationException {
        StavkaRacuna stavkaRacuna = (StavkaRacuna) value;

        if (stavkaRacuna.getKolicina().compareTo(BigDecimal.ZERO) == -1) {
            throw new ValidationException("Kolicina ne moze biti manja od nule!");
        }

        if (stavkaRacuna.getUkupnaCena().compareTo(BigDecimal.ZERO) == -1) {
            throw new ValidationException("Ukupna cena stavke ne moze biti manja od nule!");
        }

        if (stavkaRacuna.getUkupnaCenaSaPorezom().compareTo(BigDecimal.ZERO) == -1) {
            throw new ValidationException("Ukupna cena stavke sa porezom ne moze biti manja od nule!");
        }
    }

}
