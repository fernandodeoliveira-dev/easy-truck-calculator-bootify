package br.com.fdo.easy_truck_calculator.events;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class BeforeDeleteItem {

    private UUID id;

}
