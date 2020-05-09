package validator.impl;

import domain.Radnik;
import exception.ValidationException;
import validator.Validator;

/**
 *
 * @author nikol
 */
public class ValidatorEmployee implements Validator {

    @Override
    public void validate(Object value) throws ValidationException {
        Radnik radnik = (Radnik) value;

        if (!radnik.getImeRadnika().matches("[A-Z][ a-z]*")) {
            throw new ValidationException("Ime radnika mora poceti velikim slovom i mora"
                    + "sadrzati samo slova!");
        }

        if (!radnik.getPrezimeRadnika().matches("[A-Z][ a-z]*")) {
            throw new ValidationException("Prezime radnika mora poceti velikim slovom i mora"
                    + "sadrzati samo slova!");
        }

        if (!radnik.getAdresa().matches("[A-Z][ a-z0-9-]*")) {
            throw new ValidationException("Adresa radnika mora poceti velikim slovom i mora"
                    + "sadrzati samo slova!");
        }

        if (!radnik.getTelefon().matches("^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\s\\./0-9]*$")) {
            throw new ValidationException("Telefon radnika nije unet u dobrom formatu!");
        }

        if (radnik.getJMBG().length() != 13) {
            throw new ValidationException("JMBG radnika mora imati 13 cifara!");
        }

        if (radnik.getUsername().length() < 3) {
            throw new ValidationException("Korisnicko ime radnika mora imati 13 cifara!");
        }

        if (radnik.getPassword().length() < 8 && radnik.getPassword().matches("[A-Z]*")) {
            throw new ValidationException("Sifra radnika mora imati bar 8 karaktera i "
                    + "bar jedno veliko slovo!");
        }
    }

}
