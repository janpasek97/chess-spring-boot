package cz.pasekj.pia.fiveinarow.users.services;

/**
 * Service for sending emails to users
 */
public interface UserEmailingService {

    /**
     * Send an email to a user specified by username
     * @param username username
     * @param subject subject of the email
     * @param text text of the email
     */
    void sendEmailToUser(String username, String subject, String text);

    /**
     * Send an email to a specified email
     * @param email target email
     * @param subject subject of the email
     * @param text text of the email
     */
    void sendEmailToEmail(String email, String subject, String text);

}
