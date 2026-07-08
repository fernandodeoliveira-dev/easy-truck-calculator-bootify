package br.com.fdo.easy_truck_calculator.tenant;

import br.com.fdo.easy_truck_calculator.events.BeforeDeleteTenant;
import br.com.fdo.easy_truck_calculator.util.NotFoundException;
import java.util.UUID;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class TenantService {

    private final TenantRepository tenantRepository;
    private final ApplicationEventPublisher publisher;
    private final TenantMapper tenantMapper;

    public TenantService(final TenantRepository tenantRepository,
            final ApplicationEventPublisher publisher, final TenantMapper tenantMapper) {
        this.tenantRepository = tenantRepository;
        this.publisher = publisher;
        this.tenantMapper = tenantMapper;
    }

    public Page<TenantDTO> findAll(final String filter, final Pageable pageable) {
        Page<Tenant> page;
        if (filter != null) {
            UUID uuidFilter = null;
            try {
                uuidFilter = UUID.fromString(filter);
            } catch (final IllegalArgumentException illegalArgumentException) {
                // keep null - no parseable input
            }
            page = tenantRepository.findAllById(uuidFilter, pageable);
        } else {
            page = tenantRepository.findAll(pageable);
        }
        return new PageImpl<>(page.getContent()
                .stream()
                .map(tenant -> tenantMapper.updateTenantDTO(tenant, new TenantDTO()))
                .toList(),
                pageable, page.getTotalElements());
    }

    public TenantDTO get(final UUID id) {
        return tenantRepository.findById(id)
                .map(tenant -> tenantMapper.updateTenantDTO(tenant, new TenantDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final TenantDTO tenantDTO) {
        final Tenant tenant = new Tenant();
        tenantMapper.updateTenant(tenantDTO, tenant);
        return tenantRepository.save(tenant).getId();
    }

    public void update(final UUID id, final TenantDTO tenantDTO) {
        final Tenant tenant = tenantRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        tenantMapper.updateTenant(tenantDTO, tenant);
        tenantRepository.save(tenant);
    }

    public void delete(final UUID id) {
        final Tenant tenant = tenantRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        publisher.publishEvent(new BeforeDeleteTenant(id));
        tenantRepository.delete(tenant);
    }

}
