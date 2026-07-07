package br.com.fdo.easy_truck_calculator.item;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ItemRepository extends JpaRepository<Item, UUID> {

    Page<Item> findAllById(UUID id, Pageable pageable);

    Item findFirstByItemCategoryId(Long id);

}
