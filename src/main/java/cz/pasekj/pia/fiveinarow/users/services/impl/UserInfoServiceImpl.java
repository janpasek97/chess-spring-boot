package cz.pasekj.pia.fiveinarow.users.services.impl;

import cz.pasekj.pia.fiveinarow.data.entity.RoleEntity;
import cz.pasekj.pia.fiveinarow.data.entity.UserEntity;
import cz.pasekj.pia.fiveinarow.data.repository.UserRepository;
import cz.pasekj.pia.fiveinarow.users.services.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("currentUserService")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class UserInfoServiceImpl implements UserInfoService {

    private final UserRepository userRepository;

    @Override
    public UserEntity getCurrentUser() {
        return userRepository.findByUsername(getCurrentUserName());
    }

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

    @Override
    public String getCurrentUserEmail() {
        String currentUserName = getCurrentUserName();
        return getEmailOf(currentUserName);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getCurrentUserRoles() {
        String currentUserName = getCurrentUserName();
        return getRolesOf(currentUserName);
    }

    @Override
    public String getEmailOf(String username) {
        UserEntity user = userRepository.findByUsername(username);
        if(user != null) {
            return user.getEmail();
        } else {
            return null;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getRolesOf(String username) {
        UserEntity user = userRepository.findByUsername(username);
        List<String> roles = new ArrayList<>();
        if(user != null) {
            for (RoleEntity role : user.getRoles()) {
                roles.add(role.getName().substring(5));
            }
        }
        return roles;
    }
}
