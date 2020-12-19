package cz.pasekj.pia.fiveinarow.users.services.impl;

import cz.pasekj.pia.fiveinarow.data.entity.RoleEntity;
import cz.pasekj.pia.fiveinarow.data.entity.UserEntity;
import cz.pasekj.pia.fiveinarow.data.repository.UserRepository;
import cz.pasekj.pia.fiveinarow.users.services.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

@Service("currentUserService")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class CurrentUserServiceImpl implements CurrentUserService {

    private final UserRepository userRepository;

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
        UserEntity user = userRepository.findByUsername(currentUserName);
        if(user == null) {
            return "User details not found!";
        } else {
            return user.getEmail();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getCurrentUserRoles() {
        List<String> roles = new LinkedList<>();
        String currentUserName = getCurrentUserName();
        UserEntity user = userRepository.findByUsername(currentUserName);
        if(user != null) {
            for (RoleEntity role : user.getRoles()) {
                String roleName = role.getName().substring(5).toLowerCase();
                roles.add((new SimpleGrantedAuthority(roleName)).getAuthority());
            }
        }
        return roles;
    }
}
