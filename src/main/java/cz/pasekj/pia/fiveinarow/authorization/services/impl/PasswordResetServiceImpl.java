package cz.pasekj.pia.fiveinarow.authorization.services.impl;

import cz.pasekj.pia.fiveinarow.authorization.services.PasswordResetService;
import cz.pasekj.pia.fiveinarow.authorization.services.PasswordValidationService;
import cz.pasekj.pia.fiveinarow.data.entity.PasswordResetTokenEntity;
import cz.pasekj.pia.fiveinarow.data.entity.UserEntity;
import cz.pasekj.pia.fiveinarow.data.repository.PasswordResetTokenRepository;
import cz.pasekj.pia.fiveinarow.data.repository.UserRepository;
import cz.pasekj.pia.fiveinarow.users.services.UserEmailingService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.UUID;

/**
 * Implementation of a service for password recovery based on emailed token
 */
@Service("passwordResetService")
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {

    /** Email of a user who requested a recovery email - set by JSF */
    @Getter
    @Setter
    private String email;

    /** New password - set by JSF */
    @Getter
    @Setter
    private String newPassword = "";

    /** Confirmation of the new password - set by JSF */
    @Getter
    @Setter
    private String newPasswordConfirmation = "";

    /** Recovery token - set by JSF */
    @Getter
    @Setter
    private String token = "";

    /** UserEntity DOA */
    private final UserRepository userRepository;
    /** PasswordResetTokenEntity DAO */
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    /** service for validating password strength */
    private final PasswordValidationService passwordValidationService;
    /** service for sending emails to users */
    private final UserEmailingService userEmailingService;
    /** password encoder */
    private final PasswordEncoder encoder;

    @Override
    public void requestLink() {
        requestLink(email);
    }

    @Override
    @Transactional(readOnly = false)
    public void requestLink(String email) {
        // check if the user exists
        UserEntity userEntity = userRepository.findByEmail(email);
        if(userEntity == null || !userEntity.isEnabled()) {
            FacesContext.getCurrentInstance().addMessage(":resetPasswordForm:email",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "User with given email was not found!", null));
            return;
        }

        // if there's an old link delete it
        PasswordResetTokenEntity oldTokenEntity = passwordResetTokenRepository.findByUser(userEntity);
        if(oldTokenEntity != null) {
            passwordResetTokenRepository.delete(oldTokenEntity);
        }

        // generate the new token
        String token = UUID.randomUUID().toString();
        PasswordResetTokenEntity tokenEntity = new PasswordResetTokenEntity(token, userEntity);
        passwordResetTokenRepository.saveAndFlush(tokenEntity);

        // build and send the email
        StringBuilder resetMessage = new StringBuilder();
        resetMessage.append("Hello dear user,\n");
        resetMessage.append("we've registered a request for password reset. You can find the link for resetting your password below:\n");
        resetMessage.append("\n");
        resetMessage.append("localhost:8080/password/change?token=");
        resetMessage.append(token);
        resetMessage.append("\n\n\n");
        resetMessage.append("Kind regards,\nFive in a row app support team");

        userEmailingService.sendEmailToEmail(email, "Password reset", resetMessage.toString());

        FacesContext.getCurrentInstance().addMessage(":resetPasswordForm:email",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Email with recovery link has been sent!", null));
        return;
    }

    @Override
    public void changePassword() {
        changePassword(newPassword, newPasswordConfirmation, token);
    }

    @Override
    @Transactional(readOnly = false)
    public void changePassword(String newPassword, String newPasswordConfirmation, String token) {
        // check if the token exists
        PasswordResetTokenEntity passwordResetTokenEntity = passwordResetTokenRepository.findByToken(token);
        if (passwordResetTokenEntity == null) {
            raiseError("Provided recovery token is not valid");
            return;
        }

        // check if the token is not expired
        if(passwordResetTokenEntity.getExpiryDate().before(new Date(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)*1000))) {
            raiseError("Recovery token has already expired!");
            passwordResetTokenRepository.delete(passwordResetTokenEntity);
            return;
        }

        // check if the new password is valid
        if(!passwordValidationService.isValid(newPassword)) {
            raiseError("Entered password does not meet the requirements - length 8-16 characters and at least one character from the following groups: uppercase, lowercase, digit, special!");
            return;
        }

        // check if the new password matches the confirmation
        if(!newPassword.equals(newPasswordConfirmation)) {
            raiseError("Provided passwords does not match!");
            return;
        }

        // save the new password and delete the used token from the database
        passwordResetTokenEntity.getUser().setPassword(encoder.encode(newPassword));
        userRepository.saveAndFlush(passwordResetTokenEntity.getUser());

        passwordResetTokenRepository.delete(passwordResetTokenEntity);

        FacesContext.getCurrentInstance().addMessage(":resetPasswordForm",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Your password was successfully changed!", null));
    }

    /**
     * Raise a JSF error message with given string
     * @param errorMsg string representing the message
     */
    private void raiseError(String errorMsg) {
        FacesContext.getCurrentInstance().addMessage(":resetPasswordForm",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMsg, null));
    }
}
