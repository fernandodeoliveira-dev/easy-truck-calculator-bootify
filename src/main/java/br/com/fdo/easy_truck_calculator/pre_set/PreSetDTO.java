package br.com.fdo.easy_truck_calculator.pre_set;

import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PreSetDTO {

    private UUID id;

    @Size(max = 255)
    private String name;

    private Boolean active;

    private List<UUID> items;

}
