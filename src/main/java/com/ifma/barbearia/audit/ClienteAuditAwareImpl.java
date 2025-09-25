package com.ifma.barbearia.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("clienteAuditAwareImpl")
public class ClienteAuditAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of("CLIENTE_ENTITY");
    }

}
