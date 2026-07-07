package br.com.fdo.easy_truck_calculator.item;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ItemDTO {

    private UUID id;

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    @Digits(integer = 10, fraction = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(type = "string", example = "74.08")
    private BigDecimal volumeM3;

    @Digits(integer = 10, fraction = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(type = "string", example = "84.08")
    private BigDecimal weightKg;

    @NotNull
    private Boolean stackable;

    @NotNull
    private Boolean fragile;

    @NotNull
    private Boolean heavy;

    @NotNull
    private Boolean active;

    private Long itemCategory;

}
