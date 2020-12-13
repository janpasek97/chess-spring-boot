package cz.pasekj.pia.fiveinarow.users.services.impl;

import cz.pasekj.pia.fiveinarow.users.services.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service("currentUserService")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class CurrentUserServiceImpl implements CurrentUserService {

    @Override
    public String getCurrentUserName() {
        Object currentUserPrincipal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentUsername;
        if(currentUserPrincipal instanceof UserDetails) {
            currentUsername = ((UserDetails)currentUserPrincipal).getUsername();
        } else {
            currentUsername = currentUserPrincipal.toString();
        }
        return currentUsername;
    }
}
