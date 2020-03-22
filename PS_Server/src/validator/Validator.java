package validator;

import exception.ValidationException;

/**
 * @author nikol
 */

public interface Validator {
    public void validate(Object value) throws ValidationException;
}
