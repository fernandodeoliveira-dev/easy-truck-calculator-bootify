package br.com.fdo.easy_truck_calculator.unit;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;


@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UnitMapper {

    UnitDTO updateUnitDTO(Unit unit, @MappingTarget UnitDTO unitDTO);

    @Mapping(target = "id", ignore = true)
    Unit updateUnit(UnitDTO unitDTO, @MappingTarget Unit unit);

}
