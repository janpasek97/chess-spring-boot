package cz.pasekj.pia.fiveinarow.configuration;

import cz.pasekj.pia.fiveinarow.authorization.AuthenticationFailureMessageHandler;
import cz.pasekj.pia.fiveinarow.authorization.CustomUser;
import cz.pasekj.pia.fiveinarow.authorization.services.impl.CustomOAuth2UserServiceImpl;
import cz.pasekj.pia.fiveinarow.authorization.services.impl.CustomOidcUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity
@EnableGlobalAuthentication
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@RequiredArgsConstructor
@ComponentScan
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Value("${logInUrl}")
    private String loginUrl;

    @Value("${logOutUrl}")
    private String logoutUrl;

    private final OAuth2UserService<OAuth2UserRequest, OAuth2User> customOAuth2UserService;
    private final CustomOidcUserService customOidcUserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests(auth -> auth
                    .antMatchers("/webjars/**").permitAll()
                    .antMatchers("/js/**").permitAll()
                    .antMatchers("/css/**").permitAll()
                    .antMatchers("/wav/**").permitAll()
                    .antMatchers("/javax.faces.resource/**").permitAll()
                    .antMatchers(loginUrl).permitAll()
                    .antMatchers("/signup").permitAll()
                    .antMatchers("/password/reset").permitAll()
                    .antMatchers("/password/change").permitAll()
                    .antMatchers("/admin**").hasRole("ADMIN")
                    .anyRequest().authenticated())
            .formLogin()
            .loginPage(loginUrl)
            .defaultSuccessUrl("/")
            .and()
            .logout()
            .logoutUrl(logoutUrl)
            .logoutSuccessUrl("/")
            .and()
            .oauth2Login()
            .loginPage(loginUrl)
            .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.userService(customOAuth2UserService))
            .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.oidcUserService(customOidcUserService));
        http.sessionManagement().maximumSessions(1).sessionRegistry(sessionRegistry());
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public AuthenticationFailureHandler getAuthenticationFailureHandler() {
        return new AuthenticationFailureMessageHandler();
    }
}
