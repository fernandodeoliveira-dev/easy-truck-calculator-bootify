package br.com.fdo.easy_truck_calculator.account_config;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AccountConfigRepository extends JpaRepository<AccountConfig, Long> {

    Page<AccountConfig> findAllById(Long id, Pageable pageable);

    AccountConfig findFirstByTenantId(UUID id);

    boolean existsByTenantId(UUID id);

}
