package br.com.fdo.easy_truck_calculator.pre_set;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PreSetRepository extends JpaRepository<PreSet, UUID> {

    Page<PreSet> findAllById(UUID id, Pageable pageable);

    List<PreSet> findAllByItemsId(UUID id);

}
