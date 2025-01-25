package com.weinne.finance_system.infrastructure.multitenancy.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.weinne.finance_system.infrastructure.multitenancy.context.TenantContext;
import com.weinne.finance_system.security.JwtService;
import lombok.RequiredArgsConstructor;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * TenantFilter is a servlet filter that intercepts HTTP requests to ensure that a valid tenant ID is provided.
 * It checks the presence and validity of the "X-Tenant-ID" header and sets the tenant context accordingly.
 * 
 * This filter ignores requests to public endpoints (those starting with "/api/public").
 * If the tenant ID is missing or invalid, it responds with an appropriate HTTP error status.
 * 
 * Order(1) ensures that this filter is executed early in the filter chain.
 * 
 * Dependencies:
 * - EntityManagerFactory: Used to validate the existence of the tenant.
 * 
 * Exceptions:
 * - IOException: If an I/O error occurs during the filtering process.
 * - ServletException: If a servlet error occurs during the filtering process.
 * 
 * Usage:
 * {@code
 * @Component
 * @Order(1)
 * @RequiredArgsConstructor
 * public class TenantFilter implements Filter {
 *     // Implementation details
 * }
 * }
 * 
 * @author 
 * @since 
 */
@Component
@Order(1)
@RequiredArgsConstructor
public class TenantFilter implements Filter {

    private final JwtService jwtService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        
        // Ignora endpoints públicos
        if (req.getRequestURI().startsWith("/api/public")) {
            chain.doFilter(request, response);
            return;
        }

        String authHeader = req.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String jwt = authHeader.substring(7);
        String tenantId = jwtService.extractTenantId(jwt);

        if (tenantId == null) {
            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_BAD_REQUEST, "Tenant ID não encontrado no token");
            return;
        }

        TenantContext.setCurrentTenant(tenantId);
        chain.doFilter(request, response);
        TenantContext.clear();
    }
}
