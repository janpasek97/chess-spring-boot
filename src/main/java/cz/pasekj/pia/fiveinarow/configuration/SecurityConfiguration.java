package cz.pasekj.pia.fiveinarow.configuration;

import cz.pasekj.pia.fiveinarow.authorization.AuthenticationFailureMessageHandler;
import lombok.RequiredArgsConstructor;
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

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests(auth -> auth
                    .antMatchers("/webjars/**").permitAll()
                    .antMatchers("/js/**").permitAll()
                    .antMatchers("/css/**").permitAll()
                    .antMatchers("/javax.faces.resource/**").permitAll()
                    .antMatchers(loginUrl).permitAll()
                    .antMatchers("/signup").permitAll()
                    .antMatchers("/admin**").hasRole("ADMIN")
                    .anyRequest().authenticated())
            .formLogin()
            .loginPage(loginUrl)
            .defaultSuccessUrl("/")
            .and()
            .logout()
            .logoutUrl(logoutUrl)
            .logoutSuccessUrl("/");
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
