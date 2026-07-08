package br.com.fdo.easy_truck_calculator.account_config;

import br.com.fdo.easy_truck_calculator.tenant.Tenant;
import br.com.fdo.easy_truck_calculator.tenant.TenantRepository;
import br.com.fdo.easy_truck_calculator.util.NotFoundException;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;


@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface AccountConfigMapper {

    @Mapping(target = "tenant", ignore = true)
    AccountConfigDTO updateAccountConfigDTO(AccountConfig accountConfig,
            @MappingTarget AccountConfigDTO accountConfigDTO);

    @AfterMapping
    default void afterUpdateAccountConfigDTO(AccountConfig accountConfig,
            @MappingTarget AccountConfigDTO accountConfigDTO) {
        accountConfigDTO.setTenant(accountConfig.getTenant() == null ? null : accountConfig.getTenant().getId());
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenant", ignore = true)
    AccountConfig updateAccountConfig(AccountConfigDTO accountConfigDTO,
            @MappingTarget AccountConfig accountConfig, @Context TenantRepository tenantRepository);

    @AfterMapping
    default void afterUpdateAccountConfig(AccountConfigDTO accountConfigDTO,
            @MappingTarget AccountConfig accountConfig,
            @Context TenantRepository tenantRepository) {
        final Tenant tenant = accountConfigDTO.getTenant() == null ? null : tenantRepository.findById(accountConfigDTO.getTenant())
                .orElseThrow(() -> new NotFoundException("tenant not found"));
        accountConfig.setTenant(tenant);
    }

}
