package br.com.fdo.easy_truck_calculator.tenant;

import static org.junit.jupiter.api.Assertions.assertEquals;

import br.com.fdo.easy_truck_calculator.config.BaseIT;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.UUID;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;


public class TenantResourceTest extends BaseIT {

    @Test
    @Sql("/data/tenantData.sql")
    void getAllTenants_success() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                .when()
                    .get("/api/tenants")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("page.totalElements", Matchers.equalTo(2))
                    .body("content.get(0).id", Matchers.equalTo("a9dd4a99-fba6-375a-9494-772b58f95280"));
    }

    @Test
    @Sql("/data/tenantData.sql")
    void getAllTenants_filtered() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                .when()
                    .get("/api/tenants?filter=b801a1c1-65dd-3420-816b-fec5edd6c2b1")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("page.totalElements", Matchers.equalTo(1))
                    .body("content.get(0).id", Matchers.equalTo("b801a1c1-65dd-3420-816b-fec5edd6c2b1"));
    }

    @Test
    @Sql("/data/tenantData.sql")
    void getTenant_success() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                .when()
                    .get("/api/tenants/a9dd4a99-fba6-375a-9494-772b58f95280")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("name", Matchers.equalTo("Zed diam voluptua."));
    }

    @Test
    void getTenant_notFound() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                .when()
                    .get("/api/tenants/234920ea-2540-3ec7-bbee-9efce43ea25e")
                .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body("code", Matchers.equalTo("NOT_FOUND"));
    }

    @Test
    void createTenant_success() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(readResource("/requests/tenantDTORequest.json"))
                .when()
                    .post("/api/tenants")
                .then()
                    .statusCode(HttpStatus.CREATED.value());
        assertEquals(1, tenantRepository.count());
    }

    @Test
    void createTenant_missingField() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(readResource("/requests/tenantDTORequest_missingField.json"))
                .when()
                    .post("/api/tenants")
                .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("code", Matchers.equalTo("VALIDATION_FAILED"))
                    .body("fieldErrors.get(0).property", Matchers.equalTo("name"))
                    .body("fieldErrors.get(0).code", Matchers.equalTo("REQUIRED_NOT_NULL"));
    }

    @Test
    @Sql("/data/tenantData.sql")
    void updateTenant_success() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(readResource("/requests/tenantDTORequest.json"))
                .when()
                    .put("/api/tenants/a9dd4a99-fba6-375a-9494-772b58f95280")
                .then()
                    .statusCode(HttpStatus.OK.value());
        assertEquals("Duis autem vel.", tenantRepository.findById(UUID.fromString("a9dd4a99-fba6-375a-9494-772b58f95280")).orElseThrow().getName());
        assertEquals(2, tenantRepository.count());
    }

    @Test
    @Sql("/data/tenantData.sql")
    void deleteTenant_success() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                .when()
                    .delete("/api/tenants/a9dd4a99-fba6-375a-9494-772b58f95280")
                .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());
        assertEquals(1, tenantRepository.count());
    }

}
