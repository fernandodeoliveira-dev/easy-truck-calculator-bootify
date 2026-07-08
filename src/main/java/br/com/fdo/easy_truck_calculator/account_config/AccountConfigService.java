package br.com.fdo.easy_truck_calculator.account_config;

import br.com.fdo.easy_truck_calculator.events.BeforeDeleteTenant;
import br.com.fdo.easy_truck_calculator.tenant.TenantRepository;
import br.com.fdo.easy_truck_calculator.util.NotFoundException;
import br.com.fdo.easy_truck_calculator.util.ReferencedException;
import java.util.UUID;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class AccountConfigService {

    private final AccountConfigRepository accountConfigRepository;
    private final TenantRepository tenantRepository;
    private final AccountConfigMapper accountConfigMapper;

    public AccountConfigService(final AccountConfigRepository accountConfigRepository,
            final TenantRepository tenantRepository,
            final AccountConfigMapper accountConfigMapper) {
        this.accountConfigRepository = accountConfigRepository;
        this.tenantRepository = tenantRepository;
        this.accountConfigMapper = accountConfigMapper;
    }

    public Page<AccountConfigDTO> findAll(final String filter, final Pageable pageable) {
        Page<AccountConfig> page;
        if (filter != null) {
            Long longFilter = null;
            try {
                longFilter = Long.parseLong(filter);
            } catch (final NumberFormatException numberFormatException) {
                // keep null - no parseable input
            }
            page = accountConfigRepository.findAllById(longFilter, pageable);
        } else {
            page = accountConfigRepository.findAll(pageable);
        }
        return new PageImpl<>(page.getContent()
                .stream()
                .map(accountConfig -> accountConfigMapper.updateAccountConfigDTO(accountConfig, new AccountConfigDTO()))
                .toList(),
                pageable, page.getTotalElements());
    }

    public AccountConfigDTO get(final Long id) {
        return accountConfigRepository.findById(id)
                .map(accountConfig -> accountConfigMapper.updateAccountConfigDTO(accountConfig, new AccountConfigDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final AccountConfigDTO accountConfigDTO) {
        final AccountConfig accountConfig = new AccountConfig();
        accountConfigMapper.updateAccountConfig(accountConfigDTO, accountConfig, tenantRepository);
        return accountConfigRepository.save(accountConfig).getId();
    }

    public void update(final Long id, final AccountConfigDTO accountConfigDTO) {
        final AccountConfig accountConfig = accountConfigRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        accountConfigMapper.updateAccountConfig(accountConfigDTO, accountConfig, tenantRepository);
        accountConfigRepository.save(accountConfig);
    }

    public void delete(final Long id) {
        final AccountConfig accountConfig = accountConfigRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        accountConfigRepository.delete(accountConfig);
    }

    public boolean tenantExists(final UUID id) {
        return accountConfigRepository.existsByTenantId(id);
    }

    @EventListener(BeforeDeleteTenant.class)
    public void on(final BeforeDeleteTenant event) {
        final ReferencedException referencedException = new ReferencedException();
        final AccountConfig tenantAccountConfig = accountConfigRepository.findFirstByTenantId(event.getId());
        if (tenantAccountConfig != null) {
            referencedException.setKey("tenant.accountConfig.tenant.referenced");
            referencedException.addParam(tenantAccountConfig.getId());
            throw referencedException;
        }
    }

}
