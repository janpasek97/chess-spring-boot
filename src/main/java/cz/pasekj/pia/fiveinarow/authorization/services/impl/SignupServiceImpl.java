package cz.pasekj.pia.fiveinarow.authorization.services.impl;

import cz.pasekj.pia.fiveinarow.authorization.services.PasswordValidationService;
import cz.pasekj.pia.fiveinarow.authorization.services.SignupService;
import cz.pasekj.pia.fiveinarow.data.entity.UserEntity;
import cz.pasekj.pia.fiveinarow.data.repository.RoleRepository;
import cz.pasekj.pia.fiveinarow.data.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

@Service("signupService")
@Getter
@Setter
@RequiredArgsConstructor
@RequestScope
public class SignupServiceImpl implements SignupService {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordValidationService passwordValidationService;
    private final PasswordEncoder encoder;

    @Value("${userRole}")
    private String userRoleName;

    String errorMessage = "";

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public boolean signup(String username, String email, String password, String passwordConfirmation) {
        boolean error = false;

        // validate email
        if(!EmailValidator.getInstance().isValid(email)) {
            error = true;
            errorMessage = "Email has incorrect format!";
        } else if (userRepo.findByEmail(email) != null) {
            error = true;
            errorMessage = "Email already taken!";
        }

        // validate username
        if(username.length() <= 0 || userRepo.findByUsername(username) != null) {
            error = true;
            errorMessage = username.length() <= 0 ? "Username must not be empty!" : "Username already taken!";
        }

        // validate password
        if(!passwordValidationService.isValid(password)) {
            error = true;
            errorMessage = "Entered password does not meet the requirements - length 8-16 characters and at least one character from the following groups: uppercase, lowercase, digit, special!";
        } else if(!password.equals(passwordConfirmation)) {
            error = true;
            errorMessage = "Passwords does not match!";
        }

        if(!error) {
            UserEntity newUser = new UserEntity(username, email, encoder.encode(password), true);
            newUser.addRole(roleRepo.findByName(userRoleName));
            userRepo.saveAndFlush(newUser);
        }
        return !error;
    }
}