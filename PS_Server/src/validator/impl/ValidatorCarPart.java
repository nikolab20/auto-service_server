package validator.impl;

import domain.Deo;
import exception.ValidationException;
import validator.Validator;

/**
 *
 * @author nikol
 */
public class ValidatorCarPart implements Validator {

    @Override
    public void validate(Object value) throws ValidationException {
        try {
            Deo deo = (Deo) value;

            if (deo.getNazivDela().length() == 0) {
                throw new ValidationException("Naziv dela mora imata vise od 0 karaktera!");
            }

            if (deo.getProizvodjac().length() == 0) {
                throw new ValidationException("Proizvodjac mora imata vise od 0 karaktera!");
            }

            if (deo.getOpis().length() == 0) {
                throw new ValidationException("Opis mora imata vise od 0 karaktera!");
            }

            if (deo.getStanje() < 0) {
                throw new ValidationException("Stanje ne moze biti manje od nule!");
            }
        } catch (Exception e) {
            throw new ValidationException("Tip objekta nije adekvatan!");
        }
    }

}
