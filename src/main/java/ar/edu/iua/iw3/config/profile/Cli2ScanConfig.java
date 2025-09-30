package ar.edu.iua.iw3.config.profile;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
//Repositorios
@EnableJpaRepositories(basePackages = "ar.edu.iua.iw3",
excludeFilters = {
    @ComponentScan.Filter(type = FilterType.REGEX, pattern = "ar\\.edu\\.iua\\.iw3\\.integration\\.cli1\\..*")
})
//Entidades
@EntityScan(basePackages = {
    "ar.edu.iua.iw3.model",
    "ar.edu.iua.iw3.auth",
    "ar.edu.iua.iw3.integration.cli2.model"
},
basePackageClasses = {
    
})
//@ConditionalOnExpression(value = "'${spring.profiles.active:-}'=='cli1")
@Profile("cli2")
public class Cli2ScanConfig {

}
