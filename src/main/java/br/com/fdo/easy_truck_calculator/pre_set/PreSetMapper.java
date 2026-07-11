package br.com.fdo.easy_truck_calculator.pre_set;

import br.com.fdo.easy_truck_calculator.item.Item;
import br.com.fdo.easy_truck_calculator.item.ItemRepository;
import br.com.fdo.easy_truck_calculator.util.NotFoundException;
import java.util.HashSet;
import java.util.List;
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
public interface PreSetMapper {

    @Mapping(target = "items", ignore = true)
    PreSetDTO updatePreSetDTO(PreSet preSet, @MappingTarget PreSetDTO preSetDTO);

    @AfterMapping
    default void afterUpdatePreSetDTO(PreSet preSet, @MappingTarget PreSetDTO preSetDTO) {
        preSetDTO.setItems(preSet.getItems().stream()
                .map(item -> item.getId())
                .toList());
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "items", ignore = true)
    PreSet updatePreSet(PreSetDTO preSetDTO, @MappingTarget PreSet preSet,
            @Context ItemRepository itemRepository);

    @AfterMapping
    default void afterUpdatePreSet(PreSetDTO preSetDTO, @MappingTarget PreSet preSet,
            @Context ItemRepository itemRepository) {
        final List<Item> items = itemRepository.findAllById(
                preSetDTO.getItems() == null ? List.of() : preSetDTO.getItems());
        if (items.size() != (preSetDTO.getItems() == null ? 0 : preSetDTO.getItems().size())) {
            throw new NotFoundException("one of items not found");
        }
        preSet.setItems(new HashSet<>(items));
    }

}
