package cz.pasekj.pia.fiveinarow.authorization.services.impl;

import cz.pasekj.pia.fiveinarow.authorization.services.RolesService;
import cz.pasekj.pia.fiveinarow.data.entity.RoleEntity;
import cz.pasekj.pia.fiveinarow.data.entity.UserEntity;
import cz.pasekj.pia.fiveinarow.data.repository.RoleRepository;
import cz.pasekj.pia.fiveinarow.data.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * RolesService implementation for checking privileges and finding all possible role based on the database
 */
@Service("rolesService")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class RolesServiceImpl implements RolesService {

    /** RoleEntity DAO */
    private final RoleRepository roleRepository;
    /** UserEntity DAO */
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public boolean hasRole(String role, String username) {
        AtomicBoolean hasRole = new AtomicBoolean(false);
        UserEntity user = userRepository.findByUsername(username);
        user.getRoles().forEach(roleEntity -> {
            if (roleEntity.getName().contains(username)) {
                hasRole.set(true);
            }
        });

        return hasRole.get();
    }

    @Override
    public List<String> getRoles() {
        List<RoleEntity> roleEntities = roleRepository.findAll();
        List<String> roleNames = new ArrayList<>();
        for(RoleEntity role : roleEntities) {
            roleNames.add(role.getName().substring(5));
        }
        return roleNames;
    }
}
