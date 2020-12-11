package cz.pasekj.pia.fiveinarow.configuration;

import org.ocpsoft.rewrite.config.ConfigurationBuilder;
import org.ocpsoft.rewrite.servlet.config.DispatchType;
import org.ocpsoft.rewrite.servlet.config.HttpConfigurationProvider;
import org.ocpsoft.rewrite.servlet.config.Path;
import org.ocpsoft.rewrite.servlet.config.SendStatus;
import org.ocpsoft.rewrite.servlet.config.rule.Join;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.ServletContext;

@Configuration
public class AppConfiguration extends HttpConfigurationProvider {

    @Override
    public org.ocpsoft.rewrite.config.Configuration getConfiguration(ServletContext context) {
        return ConfigurationBuilder.begin()
                .addRule().when(DispatchType.isRequest().and(Path.matches("/{path}")))
                .perform(SendStatus.code(404))
                .where("path").matches("^/jsf/.*\\.xhtml$")
                .addRule(Join.path("/").to("/jsf/index.xhtml"))
                .addRule(Join.path("/login").to("/jsf/login.xhtml"));
    }

    @Override
    public int priority() {
        return 10;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
