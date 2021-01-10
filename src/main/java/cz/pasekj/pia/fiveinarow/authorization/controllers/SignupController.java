package cz.pasekj.pia.fiveinarow.authorization.controllers;

import cz.pasekj.pia.fiveinarow.authorization.services.SignupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Controller of the registration page
 */
@Controller
@RequiredArgsConstructor
public class SignupController {

    /** Service that handles user registration */
    private final SignupService signupService;

    /**
     * Handles POST action for user registration
     * @param request HTTP request
     * @return RedirectView to be rendered
     */
    @PostMapping("/signup")
    public RedirectView registerUser(WebRequest request){
        // get request parameters
        String username = request.getParameter("newUsername");
        String email = request.getParameter("newEmail");
        String password = request.getParameter("newPassword");
        String passwordConfirmation = request.getParameter("newPasswordConfirm");

        // call the service and display result to the user
        boolean success = signupService.signup(username, email, password, passwordConfirmation);
        if(success) {
            return new RedirectView("/login?success");
        } else {
            return new RedirectView("/login?signuperror="+ URLEncoder.encode(signupService.getErrorMessage(), StandardCharsets.UTF_8));
        }
    }

}
