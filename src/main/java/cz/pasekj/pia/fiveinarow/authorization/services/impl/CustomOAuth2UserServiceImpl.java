package cz.pasekj.pia.fiveinarow.authorization.services.impl;

import cz.pasekj.pia.fiveinarow.authorization.CustomUser;
import cz.pasekj.pia.fiveinarow.data.entity.UserEntity;
import cz.pasekj.pia.fiveinarow.data.repository.RoleRepository;
import cz.pasekj.pia.fiveinarow.data.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * OAuth2 user details service that processes Facebook and Github login and possibly registers the user in the
 * app database
 */
@Service("oAuth2Service")
@RequiredArgsConstructor
public class CustomOAuth2UserServiceImpl implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    /** name of the user role */
    @Value("${userRole}")
    private String userRoleName;
    /** default OAuth2 user service used for userRequest processing */
    private final DefaultOAuth2UserService defaultOAuth2UserService = new DefaultOAuth2UserService();
    /** UserEntity DAO */
    private final UserRepository userRepository;
    /** RoleEntity DAO */
    private final RoleRepository roleRepository;

    @Override
    public CustomUser loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // Get the logged in user
        OAuth2User oAuth2User = defaultOAuth2UserService.loadUser(userRequest);
        String authProvider = userRequest.getClientRegistration().getRegistrationId();
        UserEntity userEntity = null;

        if(authProvider.equals("github")) {
            userEntity = processGithubUser(oAuth2User);
        } else if(authProvider.equals("facebook")) {
            userEntity = processFacebookUser(oAuth2User);
        }

        // create and instance of CustomUser
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
     * Process Github login - convert attributes to username and email and stores the user in the database
     * @param user OAuth2 user to be processed
     * @return resulting UserEntity
     * @throws OAuth2AuthenticationException in case of login failure (eg. email collision)
     */
    private UserEntity processGithubUser(OAuth2User user) throws OAuth2AuthenticationException{
        String username = user.getAttribute("login");
        String email = "github-"+username+"@domain.com";
        return processGeneralUser(username, email);
    }

    /**
     * Process Facebook login - convert attributes to username and email and stores the user in the database
     * @param user OAuth2 user to be processed
     * @return resulting UserEntity
     * @throws OAuth2AuthenticationException in case of login failure (eg. email collision)
     */
    private UserEntity processFacebookUser(OAuth2User user) throws OAuth2AuthenticationException {
        String username = user.getAttribute("name");
        String email = user.getAttribute("email");
        return processGeneralUser(username, email);
    }

    /**
     * Check's whether the user already exists in the database. If not it stores it's information.
     * In case of collision with legacy user and exception is thrown
     * @param name name of the logged in user
     * @param email email of the logged in user
     * @return resulting UserEntity
     * @throws OAuth2AuthenticationException in case of login failure (eg. email collision)
     */
    private UserEntity processGeneralUser(String name, String email) throws OAuth2AuthenticationException{
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
