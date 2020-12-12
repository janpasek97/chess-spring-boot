package cz.pasekj.pia.fiveinarow.authorization.controllers;

import cz.pasekj.pia.fiveinarow.authorization.services.SignupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@RequiredArgsConstructor
public class SignupController {

    private final SignupService signupService;

    @PostMapping("/signup")
    public RedirectView registerUser(WebRequest request){
        String username = request.getParameter("newUsername");
        String email = request.getParameter("newEmail");
        String password = request.getParameter("newPassword");
        String passwordConfirmation = request.getParameter("newPasswordConfirm");
        boolean success = signupService.signup(username, email, password, passwordConfirmation);
        if(success) {
            return new RedirectView("/login?success");
        } else {
            return new RedirectView("/login?signuperror="+ URLEncoder.encode(signupService.getErrorMessage(), StandardCharsets.UTF_8));
        }
    }

}
