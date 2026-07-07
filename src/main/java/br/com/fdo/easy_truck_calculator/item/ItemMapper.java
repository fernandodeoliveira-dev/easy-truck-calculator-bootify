package br.com.fdo.easy_truck_calculator.item;

import br.com.fdo.easy_truck_calculator.item_category.ItemCategory;
import br.com.fdo.easy_truck_calculator.item_category.ItemCategoryRepository;
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
public interface ItemMapper {

    @Mapping(target = "itemCategory", ignore = true)
    ItemDTO updateItemDTO(Item item, @MappingTarget ItemDTO itemDTO);

    @AfterMapping
    default void afterUpdateItemDTO(Item item, @MappingTarget ItemDTO itemDTO) {
        itemDTO.setItemCategory(item.getItemCategory() == null ? null : item.getItemCategory().getId());
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "itemCategory", ignore = true)
    Item updateItem(ItemDTO itemDTO, @MappingTarget Item item,
            @Context ItemCategoryRepository itemCategoryRepository);

    @AfterMapping
    default void afterUpdateItem(ItemDTO itemDTO, @MappingTarget Item item,
            @Context ItemCategoryRepository itemCategoryRepository) {
        final ItemCategory itemCategory = itemDTO.getItemCategory() == null ? null : itemCategoryRepository.findById(itemDTO.getItemCategory())
                .orElseThrow(() -> new NotFoundException("itemCategory not found"));
        item.setItemCategory(itemCategory);
    }

}
