package br.com.fdo.easy_truck_calculator.account_config;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;
import java.util.UUID;
import org.springframework.web.servlet.HandlerMapping;


/**
 * Validate that the tenant value isn't taken yet.
 */
@Target({ FIELD, METHOD, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = AccountConfigTenantUnique.AccountConfigTenantUniqueValidator.class
)
public @interface AccountConfigTenantUnique {

    String message() default "{Exists.accountConfig.tenant}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class AccountConfigTenantUniqueValidator implements ConstraintValidator<AccountConfigTenantUnique, UUID> {

        private final AccountConfigService accountConfigService;
        private final HttpServletRequest request;

        public AccountConfigTenantUniqueValidator(final AccountConfigService accountConfigService,
                final HttpServletRequest request) {
            this.accountConfigService = accountConfigService;
            this.request = request;
        }

        @Override
        public boolean isValid(final UUID value, final ConstraintValidatorContext cvContext) {
            if (value == null) {
                // no value present
                return true;
            }
            @SuppressWarnings("unchecked") final Map<String, String> pathVariables =
                    ((Map<String, String>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE));
            final String currentId = pathVariables.get("id");
            if (currentId != null && value.equals(accountConfigService.get(Long.parseLong(currentId)).getTenant())) {
                // value hasn't changed
                return true;
            }
            return !accountConfigService.tenantExists(value);
        }

    }

}
