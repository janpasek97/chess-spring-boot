package cz.pasekj.pia.fiveinarow.authorization.services;

public interface PasswordResetService {

    void requestLink();
    void requestLink(String email);

    void changePassword();
    void changePassword(String newPassword, String newPasswordConfirmation, String token);

}
