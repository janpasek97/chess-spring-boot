package cz.pasekj.pia.fiveinarow.authorization.services.impl;

import cz.pasekj.pia.fiveinarow.data.entity.UserEntity;
import cz.pasekj.pia.fiveinarow.data.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of UserDetailsService for legacy login method
 */
@Service("userDetailsService")
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    /** UserEntity DAO */
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserEntity foundUser = userRepository.findByEmail(s);
        if(foundUser == null) {
            throw new UsernameNotFoundException("User was not found");
        } else {
            List<GrantedAuthority> authorityList = new ArrayList<>();
            foundUser.getRoles().forEach(role -> authorityList.add(new SimpleGrantedAuthority(role.getName())));
            final var user = User.builder()
                    .username(foundUser.getUsername())
                    .password(foundUser.getPassword())
                    .accountLocked(!foundUser.isEnabled())
                    .authorities(authorityList)
                    .build();
            return User.withUserDetails(user).build();
        }
    }

}
