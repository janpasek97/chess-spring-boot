package cz.pasekj.pia.fiveinarow.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = {"cz.pasekj.pia.fiveinarow.data.repository"})
@EnableTransactionManagement
public class JpaConfiguration {
}
