package com.weinne.finance_system.infrastructure.multitenancy.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.weinne.finance_system.infrastructure.multitenancy.context.TenantContext;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

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

    private final EntityManagerFactory emf;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        
        // Ignora endpoints públicos
        if (req.getRequestURI().startsWith("/api/public")) {
            chain.doFilter(request, response);
            return;
        }

        String tenantId = req.getHeader("X-Tenant-ID");
        if (tenantId == null) {
            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_BAD_REQUEST, "Tenant ID não informado");
            return;
        }

        EntityManager em = emf.createEntityManager();
        try {
            boolean exists = em.createQuery("SELECT COUNT(c) > 0 FROM Church c WHERE c.schemaName = :schema")
                .setParameter("schema", tenantId)
                .getSingleResult().equals(1L);

            if (!exists) {
                ((HttpServletResponse) response).sendError(HttpServletResponse.SC_NOT_FOUND, "Tenant não encontrado");
                return;
            }

            TenantContext.setCurrentTenant(tenantId);
            chain.doFilter(request, response);
        } finally {
            em.close();
            TenantContext.clear();
        }
    }
}
