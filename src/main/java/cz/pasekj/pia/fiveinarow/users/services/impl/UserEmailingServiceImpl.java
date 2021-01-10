package cz.pasekj.pia.fiveinarow.users.services.impl;

import cz.pasekj.pia.fiveinarow.data.entity.UserEntity;
import cz.pasekj.pia.fiveinarow.data.repository.UserRepository;
import cz.pasekj.pia.fiveinarow.users.services.UserEmailingService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * UserEmailingService implementation
 */
@Service("userEmailingService")
@RequiredArgsConstructor
public class UserEmailingServiceImpl implements UserEmailingService {

    /** Mail sender service */
    private final JavaMailSender mailSender;
    /** UserEntity DAO */
    private final UserRepository userRepository;


    @Override
    public void sendEmailToUser(String username, String subject, String text) {
        UserEntity userEntity = userRepository.findByUsername(username);
        if(userEntity == null) return;
        sendEmailToEmail(userEntity.getEmail(), subject, text);
    }

    @Override
    public void sendEmailToEmail(String email, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("Five-In-A-Row: " + subject);
        message.setText(text);
        message.setTo(email);
        message.setFrom("kiv.fiveinarow@support.com");
        mailSender.send(message);
    }
}
