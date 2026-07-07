package br.com.fdo.easy_truck_calculator.item_category;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ItemCategoryDTO {

    private Long id;

    @Size(max = 255)
    private String name;

    private Boolean active;

}
