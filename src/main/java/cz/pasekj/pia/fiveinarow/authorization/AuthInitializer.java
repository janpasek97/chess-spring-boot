package cz.pasekj.pia.fiveinarow.authorization;

import cz.pasekj.pia.fiveinarow.data.entity.RoleEntity;
import cz.pasekj.pia.fiveinarow.data.entity.UserEntity;
import cz.pasekj.pia.fiveinarow.data.repository.RoleRepository;
import cz.pasekj.pia.fiveinarow.data.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.WebApplicationInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.List;

/**
 * Initializer that runs after the application starts up and verify if all important roles and users are
 * in the database
 */
@Component
@Transactional
public class AuthInitializer implements InitializingBean, WebApplicationInitializer {

    /** UserEntity DAO */
    @Autowired
    private UserRepository userRepo;
    /** RoleEntity DAO */
    @Autowired
    private RoleRepository roleRepo;
    /** password encoder */
    @Autowired
    private PasswordEncoder encoder;

    // default login configurations
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

    /**
     * Checks if the Admin user is in the system and checks if all roles are in the database (USER and ADMIN)
     * If something is missing it is automatically inserted into the database
     * @throws Exception N/A
     */
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

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        servletContext.addListener(HttpSessionEventPublisher.class);
    }
}
