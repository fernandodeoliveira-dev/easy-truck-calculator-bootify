package br.com.fdo.easy_truck_calculator.item_category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ItemCategoryRepository extends JpaRepository<ItemCategory, Long> {

    Page<ItemCategory> findAllById(Long id, Pageable pageable);

}
