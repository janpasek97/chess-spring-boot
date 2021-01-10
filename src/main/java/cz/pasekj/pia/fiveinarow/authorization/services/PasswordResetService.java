package cz.pasekj.pia.fiveinarow.authorization.services;

/**
 * Password recovery service
 */
public interface PasswordResetService {

    /**
     * Request recovery link - parameters provided using JSF form
     */
    void requestLink();

    /**
     * Request recovery link for user identified by email
     * @param email email of the user whose password shall be recovered
     */
    void requestLink(String email);

    /**
     * Change password based on recovery link - parameters provided using JSF form
     */
    void changePassword();

    /**
     * Change password based on recovery link
     * @param newPassword new password of the user
     * @param newPasswordConfirmation confirmation of the user's password
     * @param token password recovery token from the received email
     */
    void changePassword(String newPassword, String newPasswordConfirmation, String token);

}
