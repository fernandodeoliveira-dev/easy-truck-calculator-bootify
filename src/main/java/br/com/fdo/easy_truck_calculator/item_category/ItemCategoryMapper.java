package br.com.fdo.easy_truck_calculator.item_category;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;


@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ItemCategoryMapper {

    ItemCategoryDTO updateItemCategoryDTO(ItemCategory itemCategory,
            @MappingTarget ItemCategoryDTO itemCategoryDTO);

    @Mapping(target = "id", ignore = true)
    ItemCategory updateItemCategory(ItemCategoryDTO itemCategoryDTO,
            @MappingTarget ItemCategory itemCategory);

}
