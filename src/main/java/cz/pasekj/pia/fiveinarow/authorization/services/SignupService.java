package cz.pasekj.pia.fiveinarow.authorization.services;

/**
 * Service handling user registration
 */
public interface SignupService {

    /**
     * Get error message generated when processing user registration
     * @return last error message
     */
    String getErrorMessage();

    /**
     * Register a new user into the system
     * @param username user's name
     * @param email user's email
     * @param password user's password
     * @param passwordConfirmation user's password confirmation
     * @return bool flag indicating successful registration
     */
    boolean signup(String username, String email, String password, String passwordConfirmation);

    /**
     * Store details about a new user logged in via Google, Facebook or Github OAuth2 method
     * @param username user's name
     * @param email user's email
     * @return bool flag indicating successful registration
     */
    boolean signupOAuth2(String username, String email);
}
