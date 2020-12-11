package cz.pasekj.pia.fiveinarow.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Value("${logInUrl}")
    private String loginUrl;

    @Value("${logOutUrl}")
    private String logoutUrl;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests(auth -> auth
                    .antMatchers("/webjars/**").permitAll()
                    .antMatchers("/js/**").permitAll()
                    .antMatchers(loginUrl).permitAll()
                    .antMatchers("/admin**").hasRole("ADMIN")
                    .anyRequest().authenticated())
            .formLogin()
            .loginPage(loginUrl)
            .defaultSuccessUrl("/")
            .failureUrl(loginUrl)
            .and()
            .logout()
            .logoutUrl(logoutUrl)
            .logoutSuccessUrl("/");
    }
}
