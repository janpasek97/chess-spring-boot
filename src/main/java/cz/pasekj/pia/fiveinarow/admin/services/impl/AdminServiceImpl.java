package cz.pasekj.pia.fiveinarow.admin.services.impl;

import cz.pasekj.pia.fiveinarow.admin.AdminServiceResult;
import cz.pasekj.pia.fiveinarow.admin.services.AdminService;
import cz.pasekj.pia.fiveinarow.authorization.services.PasswordValidationService;
import cz.pasekj.pia.fiveinarow.data.entity.RoleEntity;
import cz.pasekj.pia.fiveinarow.data.entity.UserEntity;
import cz.pasekj.pia.fiveinarow.data.repository.RoleRepository;
import cz.pasekj.pia.fiveinarow.data.repository.UserRepository;
import cz.pasekj.pia.fiveinarow.users.services.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of AdminService for user administaration purposes
 */
@Service("adminService")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminServiceImpl implements AdminService {

    /** UserEntity DAO */
    private final UserRepository userRepository;
    /** RoleEntity DAO */
    private final RoleRepository roleRepository;
    /** Service for password strength validation */
    private final PasswordValidationService passwordValidationService;
    /** PasswordEncoder bean */
    private final PasswordEncoder encoder;
    /** UserInfo service bean for getting information about the current user */
    private final UserInfoService userInfoService;

    @Override
    @Transactional
    public AdminServiceResult changePassword(String email, String password) {
        AdminServiceResult result = new AdminServiceResult();
        UserEntity user = userRepository.findByEmail(email);

        // check if the user being change exists
        if(user == null) {
            result.success = false;
            result.errorMessage = "User not found!";
            return result;
        }

        // validate password strength
        else if(!passwordValidationService.isValid(password)) {
            result.success = false;
            result.errorMessage = "Password does not meet security requirements!";
            return result;
        }

        // save the change
        result.success = true;
        user.setPassword(encoder.encode(password));
        userRepository.saveAndFlush(user);
        return result;
    }

    @Override
    @Transactional
    public AdminServiceResult changeUsername(String email, String username) {
        AdminServiceResult result = new AdminServiceResult();
        UserEntity user = userRepository.findByEmail(email);

        // check if the user being change exists
        if(user == null) {
            result.success = false;
            result.errorMessage = "User not found!";
            return result;
        }

        // check if there's a change in the username
        if(user.getUsername().equals(username)){
            result.success = true;
            return result;
        }

        // check if there's no user with the same username already
        UserEntity existingUser = userRepository.findByUsername(username);
        if(existingUser != null && existingUser != user) {
            result.success = false;
            result.errorMessage = "Username already taken!";
            return result;
        }

        // save the change
        result.success = true;
        user.setUsername(username);
        userRepository.saveAndFlush(user);
        return result;

    }

    @Override
    @Transactional
    public AdminServiceResult changeRoles(String email, List<String> roles) {
        AdminServiceResult result = new AdminServiceResult();
        String currentEmail = userInfoService.getCurrentUserEmail();
        UserEntity user = userRepository.findByEmail(email);

        // check if the user being change exists
        if(user == null) {
            result.success = false;
            result.errorMessage = "User not found!";
            return result;
        }

        // make sure that current admin user cannot remove ADMIN rights to himself
        if(currentEmail.equals(email)) {
            boolean containsAdmin = false;
            for (String role : roles) {
                if (role.equals("ADMIN")) {
                    containsAdmin = true;
                    break;
                }
            }
            if (!containsAdmin) {
                result.success = false;
                result.errorMessage = "Cannot remove admin role from current admin user!";
                return result;
            }
        }

        // user must have at least one role
        if(roles.size() <= 0) {
            result.success = false;
            result.errorMessage = "User must have at least one role!";
            return result;
        }

        // save the changes
        user.clearRoles();
        for (String role : roles) {
            RoleEntity roleEntity = roleRepository.findByName("ROLE_"+role);
            if(roleEntity != null) {
                user.addRole(roleEntity);
            }
        }
        result.success = true;
        userRepository.saveAndFlush(user);
        return result;
    }
}
