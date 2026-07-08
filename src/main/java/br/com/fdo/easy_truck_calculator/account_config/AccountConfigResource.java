package br.com.fdo.easy_truck_calculator.account_config;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/accountConfigs", produces = MediaType.APPLICATION_JSON_VALUE)
public class AccountConfigResource {

    private final AccountConfigService accountConfigService;

    public AccountConfigResource(final AccountConfigService accountConfigService) {
        this.accountConfigService = accountConfigService;
    }

    @Operation(
            parameters = {
                    @Parameter(
                            name = "page",
                            in = ParameterIn.QUERY,
                            schema = @Schema(implementation = Integer.class)
                    ),
                    @Parameter(
                            name = "size",
                            in = ParameterIn.QUERY,
                            schema = @Schema(implementation = Integer.class)
                    ),
                    @Parameter(
                            name = "sort",
                            in = ParameterIn.QUERY,
                            schema = @Schema(implementation = String.class)
                    )
            }
    )
    @GetMapping
    public ResponseEntity<Page<AccountConfigDTO>> getAllAccountConfigs(
            @RequestParam(name = "filter", required = false) final String filter,
            @Parameter(hidden = true) @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable) {
        return ResponseEntity.ok(accountConfigService.findAll(filter, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountConfigDTO> getAccountConfig(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(accountConfigService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createAccountConfig(
            @RequestBody @Valid final AccountConfigDTO accountConfigDTO) {
        final Long createdId = accountConfigService.create(accountConfigDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateAccountConfig(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final AccountConfigDTO accountConfigDTO) {
        accountConfigService.update(id, accountConfigDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteAccountConfig(@PathVariable(name = "id") final Long id) {
        accountConfigService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
