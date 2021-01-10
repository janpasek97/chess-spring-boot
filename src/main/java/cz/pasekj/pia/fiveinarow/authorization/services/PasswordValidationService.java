package cz.pasekj.pia.fiveinarow.authorization.services;

/**
 * Service for password strength validation
 */
public interface PasswordValidationService {

    /**
     * Validates the strength of the password
     * @param password password to be validated
     * @return boolean flag indicating validity of the password
     */
    boolean isValid(String password);
}
