package com.project.shopapi.security;

import com.project.shopapi.entity.User;
import com.project.shopapi.security.service.UserDetailsImpl;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class SpringSecurityAuditAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (
                authentication != null
                        && authentication.isAuthenticated()
                        && !authentication.getPrincipal().equals(null)
        ) return Optional.of(((UserDetailsImpl) authentication.getPrincipal()).getUsername());
        return Optional.of("");
    }
}
