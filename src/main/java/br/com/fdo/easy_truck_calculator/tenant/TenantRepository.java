package br.com.fdo.easy_truck_calculator.tenant;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TenantRepository extends JpaRepository<Tenant, UUID> {

    Page<Tenant> findAllById(UUID id, Pageable pageable);

}
