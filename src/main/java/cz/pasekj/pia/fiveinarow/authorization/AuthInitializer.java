package cz.pasekj.pia.fiveinarow.authorization;

import cz.pasekj.pia.fiveinarow.data.entity.RoleEntity;
import cz.pasekj.pia.fiveinarow.data.entity.UserEntity;
import cz.pasekj.pia.fiveinarow.data.repository.RoleRepository;
import cz.pasekj.pia.fiveinarow.data.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional
@RequiredArgsConstructor
public class AuthInitializer implements InitializingBean {
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder encoder;

    @Value("${defUsername}")
    private String adminUsername;
    @Value("${defPassword}")
    private String adminPassword;
    @Value("${defEmail}")
    private String adminEmail;
    @Value("${userRole}")
    private String userRoleName;
    @Value("${adminRole}")
    private String adminRoleName;


    @Override
    public void afterPropertiesSet() throws Exception {
        String[] necessaryRoles = {adminRoleName, userRoleName};
        for(String role : necessaryRoles) {
            RoleEntity currentRole = roleRepo.findByName(role);
            if (currentRole == null) {
                currentRole = new RoleEntity(role);
                roleRepo.save(currentRole);
            }
        }
        roleRepo.flush();

        RoleEntity adminRole = roleRepo.findByName(adminRoleName);
        UserEntity adminUser = userRepo.findByUsername(adminUsername);
        if(adminUser == null) {
            adminUser = new UserEntity(adminUsername, adminEmail, encoder.encode(adminPassword), true);
            adminUser.addRole(adminRole);
            userRepo.saveAndFlush(adminUser);
        }
    }
}
