package br.com.fdo.easy_truck_calculator.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import br.com.fdo.easy_truck_calculator.config.BaseIT;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.UUID;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;


public class UnitResourceTest extends BaseIT {

    @Test
    @Sql("/data/unitData.sql")
    void getAllUnits_success() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                .when()
                    .get("/api/units")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("page.totalElements", Matchers.equalTo(2))
                    .body("content.get(0).id", Matchers.equalTo("a9b7ba70-783b-317e-9998-dc4dd82eb3c5"));
    }

    @Test
    @Sql("/data/unitData.sql")
    void getAllUnits_filtered() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                .when()
                    .get("/api/units?filter=b8c37e33-defd-351c-b91e-1e03e51657da")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("page.totalElements", Matchers.equalTo(1))
                    .body("content.get(0).id", Matchers.equalTo("b8c37e33-defd-351c-b91e-1e03e51657da"));
    }

    @Test
    @Sql("/data/unitData.sql")
    void getUnit_success() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                .when()
                    .get("/api/units/a9b7ba70-783b-317e-9998-dc4dd82eb3c5")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("name", Matchers.equalTo("Zed diam voluptua."));
    }

    @Test
    void getUnit_notFound() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                .when()
                    .get("/api/units/23d7c8a0-8b4a-3a1b-87c5-99473f5dddda")
                .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body("code", Matchers.equalTo("NOT_FOUND"));
    }

    @Test
    void createUnit_success() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(readResource("/requests/unitDTORequest.json"))
                .when()
                    .post("/api/units")
                .then()
                    .statusCode(HttpStatus.CREATED.value());
        assertEquals(1, unitRepository.count());
    }

    @Test
    void createUnit_missingField() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(readResource("/requests/unitDTORequest_missingField.json"))
                .when()
                    .post("/api/units")
                .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("code", Matchers.equalTo("VALIDATION_FAILED"))
                    .body("fieldErrors.get(0).property", Matchers.equalTo("name"))
                    .body("fieldErrors.get(0).code", Matchers.equalTo("REQUIRED_NOT_NULL"));
    }

    @Test
    @Sql("/data/unitData.sql")
    void updateUnit_success() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(readResource("/requests/unitDTORequest.json"))
                .when()
                    .put("/api/units/a9b7ba70-783b-317e-9998-dc4dd82eb3c5")
                .then()
                    .statusCode(HttpStatus.OK.value());
        assertEquals("Duis autem vel.", unitRepository.findById(UUID.fromString("a9b7ba70-783b-317e-9998-dc4dd82eb3c5")).orElseThrow().getName());
        assertEquals(2, unitRepository.count());
    }

    @Test
    @Sql("/data/unitData.sql")
    void deleteUnit_success() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                .when()
                    .delete("/api/units/a9b7ba70-783b-317e-9998-dc4dd82eb3c5")
                .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());
        assertEquals(1, unitRepository.count());
    }

}
