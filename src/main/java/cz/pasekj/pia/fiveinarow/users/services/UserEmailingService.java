package cz.pasekj.pia.fiveinarow.users.services;

public interface UserEmailingService {

    void sendEmailToUser(String username, String subject, String text);
    void sendEmailToEmail(String email, String subject, String text);

}
