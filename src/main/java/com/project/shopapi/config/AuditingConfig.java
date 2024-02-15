package com.project.shopapi.config;

import com.project.shopapi.security.SpringSecurityAuditAwareImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class AuditingConfig {
    @Bean
    public AuditorAware<String> auditorAware() {
        return new SpringSecurityAuditAwareImpl();
    }
}
