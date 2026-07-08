package br.com.fdo.easy_truck_calculator.account_config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import br.com.fdo.easy_truck_calculator.config.BaseIT;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;


public class AccountConfigResourceTest extends BaseIT {

    @Test
    @Sql({"/data/tenantData.sql", "/data/accountConfigData.sql"})
    void getAllAccountConfigs_success() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                .when()
                    .get("/api/accountConfigs")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("page.totalElements", Matchers.equalTo(2))
                    .body("content.get(0).id", Matchers.equalTo(1300));
    }

    @Test
    @Sql({"/data/tenantData.sql", "/data/accountConfigData.sql"})
    void getAllAccountConfigs_filtered() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                .when()
                    .get("/api/accountConfigs?filter=1301")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("page.totalElements", Matchers.equalTo(1))
                    .body("content.get(0).id", Matchers.equalTo(1301));
    }

    @Test
    @Sql({"/data/tenantData.sql", "/data/accountConfigData.sql"})
    void getAccountConfig_success() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                .when()
                    .get("/api/accountConfigs/1300")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("maxUnits", Matchers.equalTo(63));
    }

    @Test
    void getAccountConfig_notFound() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                .when()
                    .get("/api/accountConfigs/1966")
                .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body("code", Matchers.equalTo("NOT_FOUND"));
    }

    @Test
    @Sql("/data/tenantData.sql")
    void createAccountConfig_success() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(readResource("/requests/accountConfigDTORequest.json"))
                .when()
                    .post("/api/accountConfigs")
                .then()
                    .statusCode(HttpStatus.CREATED.value());
        assertEquals(1, accountConfigRepository.count());
    }

    @Test
    void createAccountConfig_missingField() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(readResource("/requests/accountConfigDTORequest_missingField.json"))
                .when()
                    .post("/api/accountConfigs")
                .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("code", Matchers.equalTo("VALIDATION_FAILED"))
                    .body("fieldErrors.get(0).property", Matchers.equalTo("maxUnits"))
                    .body("fieldErrors.get(0).code", Matchers.equalTo("REQUIRED_NOT_NULL"));
    }

    @Test
    @Sql({"/data/tenantData.sql", "/data/accountConfigData.sql"})
    void updateAccountConfig_success() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(readResource("/requests/accountConfigDTORequest.json"))
                .when()
                    .put("/api/accountConfigs/1300")
                .then()
                    .statusCode(HttpStatus.OK.value());
        assertEquals(78, accountConfigRepository.findById(((long)1300)).orElseThrow().getMaxUnits());
        assertEquals(2, accountConfigRepository.count());
    }

    @Test
    @Sql({"/data/tenantData.sql", "/data/accountConfigData.sql"})
    void deleteAccountConfig_success() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                .when()
                    .delete("/api/accountConfigs/1300")
                .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());
        assertEquals(1, accountConfigRepository.count());
    }

}
