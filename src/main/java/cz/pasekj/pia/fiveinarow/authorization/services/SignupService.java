package cz.pasekj.pia.fiveinarow.authorization.services;

public interface SignupService {
    String getErrorMessage();
    boolean signup(String username, String email, String password, String passwordConfirmation);
    boolean signupOAuth2(String username, String email);
}
