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

@Service("passwordResetService")
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {

    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private String newPassword = "";

    @Getter
    @Setter
    private String newPasswordConfirmation = "";

    @Getter
    @Setter
    private String token = "";

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordValidationService passwordValidationService;
    private final UserEmailingService userEmailingService;
    private final PasswordEncoder encoder;

    @Override
    public void requestLink() {
        requestLink(email);
    }

    @Override
    @Transactional(readOnly = false)
    public void requestLink(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if(userEntity == null || !userEntity.isEnabled()) {
            FacesContext.getCurrentInstance().addMessage(":resetPasswordForm:email",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "User with given email was not found!", null));
            return;
        }

        PasswordResetTokenEntity oldTokenEntity = passwordResetTokenRepository.findByUser(userEntity);
        if(oldTokenEntity != null) {
            passwordResetTokenRepository.delete(oldTokenEntity);
        }

        String token = UUID.randomUUID().toString();
        PasswordResetTokenEntity tokenEntity = new PasswordResetTokenEntity(token, userEntity);
        passwordResetTokenRepository.saveAndFlush(tokenEntity);

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
        PasswordResetTokenEntity passwordResetTokenEntity = passwordResetTokenRepository.findByToken(token);
        if (passwordResetTokenEntity == null) {
            raiseError("Provided recovery token is not valid");
            return;
        }

        if(passwordResetTokenEntity.getExpiryDate().before(new Date(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)*1000))) {
            raiseError("Recovery token has already expired!");
            passwordResetTokenRepository.delete(passwordResetTokenEntity);
            return;
        }

        if(!passwordValidationService.isValid(newPassword)) {
            raiseError("Entered password does not meet the requirements - length 8-16 characters and at least one character from the following groups: uppercase, lowercase, digit, special!");
            return;
        }

        if(!newPassword.equals(newPasswordConfirmation)) {
            raiseError("Provided passwords does not match!");
            return;
        }

        passwordResetTokenEntity.getUser().setPassword(encoder.encode(newPassword));
        userRepository.saveAndFlush(passwordResetTokenEntity.getUser());

        passwordResetTokenRepository.delete(passwordResetTokenEntity);

        FacesContext.getCurrentInstance().addMessage(":resetPasswordForm",
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Your password was successfully changed!", null));
    }

    private void raiseError(String errorMsg) {
        FacesContext.getCurrentInstance().addMessage(":resetPasswordForm",
                new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMsg, null));
    }
}
