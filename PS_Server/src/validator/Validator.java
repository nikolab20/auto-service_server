package validator;

import exception.ValidationException;

/**
 * @author nikol
 */
public interface Validator {

    /**
     * Method for domain object validation.
     *
     * @param value is object for validation.
     * @throws ValidationException if the object fails validation
     */
    public void validate(Object value) throws ValidationException;
}
