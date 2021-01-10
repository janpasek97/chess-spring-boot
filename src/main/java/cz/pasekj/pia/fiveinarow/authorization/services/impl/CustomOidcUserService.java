package cz.pasekj.pia.fiveinarow.authorization.services.impl;

import cz.pasekj.pia.fiveinarow.authorization.CustomUser;
import cz.pasekj.pia.fiveinarow.data.entity.UserEntity;
import cz.pasekj.pia.fiveinarow.data.repository.RoleRepository;
import cz.pasekj.pia.fiveinarow.data.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Oidc user details service that processes Facebook and Github login and possibly registers the user in the
 * app database
 */
@Service("oidcUser")
@RequiredArgsConstructor
public class CustomOidcUserService extends OidcUserService {

    /** name of the user role */
    @Value("${userRole}")
    private String userRoleName;
    /** UserEntity DAO */
    private final UserRepository userRepository;
    /** RoleEntity DAO */
    private final RoleRepository roleRepository;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);
        String authProvider = userRequest.getClientRegistration().getRegistrationId();
        UserEntity userEntity = null;

        if(authProvider.equals("google")) {
            userEntity = processGoogleUser(oidcUser);
        }

        List<GrantedAuthority> authorityList = new ArrayList<>();
        userEntity.getRoles().forEach(role -> authorityList.add(new SimpleGrantedAuthority(role.getName())));
        CustomUser user = new CustomUser(
                userEntity.getUsername(),
                userEntity.getPassword(),
                !userEntity.isEnabled(),
                true,
                true,
                true,
                authorityList
        );

        return user;
    }

    /**
     * Process login of a google user
     * Check's whether the user already exists in the database. If not it stores it's information.
     * In case of collision with legacy user and exception is thrown
     * @param user logged in OidcUser
     * @return resulting UserEntity
     * @throws OAuth2AuthenticationException in case of login failure (eg. email collision)
     */
    private UserEntity processGoogleUser(OidcUser user) throws OAuth2AuthenticationException{
        String name = user.getAttribute("name");
        String email = user.getEmail();

        UserEntity userEntity = userRepository.findByEmail(email);
        if(userEntity != null && userEntity.isEnabled()) throw new OAuth2AuthenticationException(new OAuth2Error("Already exists"), "User with given email already exists");

        if(userEntity == null) {
            if(userRepository.findByUsername(name) != null) throw new OAuth2AuthenticationException(new OAuth2Error("Already exists"), "User with given username already exists");
            userEntity = new UserEntity(name, email, "oauth", false);
            userEntity.addRole(roleRepository.findByName(userRoleName));
            userRepository.saveAndFlush(userEntity);
        }

        return userEntity;
    }
}
