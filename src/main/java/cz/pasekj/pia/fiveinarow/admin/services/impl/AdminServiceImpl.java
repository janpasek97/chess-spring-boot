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

@Service("adminService")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordValidationService passwordValidationService;
    private final UserInfoService userInfoService;
    private final PasswordEncoder encoder;

    @Override
    public AdminServiceResult changePassword(String email, String password) {
        AdminServiceResult result = new AdminServiceResult();
        UserEntity user = userRepository.findByEmail(email);

        if(user == null) {
            result.success = false;
            result.errorMessage = "User not found!";
            return result;
        }

        else if(!passwordValidationService.isValid(password)) {
            result.success = false;
            result.errorMessage = "Password does not meet security requirements!";
            return result;
        }

        result.success = true;
        user.setPassword(encoder.encode(password));
        userRepository.saveAndFlush(user);
        return result;
    }

    @Override
    public AdminServiceResult changeUsername(String email, String username) {
        AdminServiceResult result = new AdminServiceResult();
        UserEntity user = userRepository.findByEmail(email);

        if(user == null) {
            result.success = false;
            result.errorMessage = "User not found!";
            return result;
        }

        if(user.getUsername().equals(username)){
            result.success = true;
            return result;
        }

        UserEntity existingUser = userRepository.findByUsername(username);
        if(existingUser != null && existingUser != user) {
            result.success = false;
            result.errorMessage = "Username already taken!";
            return result;
        }

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

        if(user == null) {
            result.success = false;
            result.errorMessage = "User not found!";
            return result;
        }

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

        if(roles.size() <= 0) {
            result.success = false;
            result.errorMessage = "User must have at least one role!";
            return result;
        }

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
