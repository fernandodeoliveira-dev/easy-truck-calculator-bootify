package br.com.fdo.easy_truck_calculator.unit;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UnitRepository extends JpaRepository<Unit, UUID> {

    Page<Unit> findAllById(UUID id, Pageable pageable);

}
