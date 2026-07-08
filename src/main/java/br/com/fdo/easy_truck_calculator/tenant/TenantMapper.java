package br.com.fdo.easy_truck_calculator.tenant;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;


@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface TenantMapper {

    TenantDTO updateTenantDTO(Tenant tenant, @MappingTarget TenantDTO tenantDTO);

    @Mapping(target = "id", ignore = true)
    Tenant updateTenant(TenantDTO tenantDTO, @MappingTarget Tenant tenant);

}
