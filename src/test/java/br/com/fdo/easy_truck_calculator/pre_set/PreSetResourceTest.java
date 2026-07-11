package br.com.fdo.easy_truck_calculator.pre_set;

import static org.junit.jupiter.api.Assertions.assertEquals;

import br.com.fdo.easy_truck_calculator.config.BaseIT;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.UUID;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;


public class PreSetResourceTest extends BaseIT {

    @Test
    @Sql("/data/preSetData.sql")
    void getAllPreSets_success() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                .when()
                    .get("/api/preSets")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("page.totalElements", Matchers.equalTo(2))
                    .body("content.get(0).id", Matchers.equalTo("a9a53013-58b9-3cbe-baa4-5b1ceea088c6"));
    }

    @Test
    @Sql("/data/preSetData.sql")
    void getAllPreSets_filtered() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                .when()
                    .get("/api/preSets?filter=b8bdfd0d-fa22-33fc-a726-6376887f549b")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("page.totalElements", Matchers.equalTo(1))
                    .body("content.get(0).id", Matchers.equalTo("b8bdfd0d-fa22-33fc-a726-6376887f549b"));
    }

    @Test
    @Sql("/data/preSetData.sql")
    void getPreSet_success() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                .when()
                    .get("/api/preSets/a9a53013-58b9-3cbe-baa4-5b1ceea088c6")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("name", Matchers.equalTo("Zed diam voluptua."));
    }

    @Test
    void getPreSet_notFound() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                .when()
                    .get("/api/preSets/23e05616-c8ed-3594-a3f9-af00b142dd6f")
                .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body("code", Matchers.equalTo("NOT_FOUND"));
    }

    @Test
    void createPreSet_success() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(readResource("/requests/preSetDTORequest.json"))
                .when()
                    .post("/api/preSets")
                .then()
                    .statusCode(HttpStatus.CREATED.value());
        assertEquals(1, preSetRepository.count());
    }

    @Test
    @Sql("/data/preSetData.sql")
    void updatePreSet_success() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(readResource("/requests/preSetDTORequest.json"))
                .when()
                    .put("/api/preSets/a9a53013-58b9-3cbe-baa4-5b1ceea088c6")
                .then()
                    .statusCode(HttpStatus.OK.value());
        assertEquals("Duis autem vel.", preSetRepository.findById(UUID.fromString("a9a53013-58b9-3cbe-baa4-5b1ceea088c6")).orElseThrow().getName());
        assertEquals(2, preSetRepository.count());
    }

    @Test
    @Sql("/data/preSetData.sql")
    void deletePreSet_success() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                .when()
                    .delete("/api/preSets/a9a53013-58b9-3cbe-baa4-5b1ceea088c6")
                .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());
        assertEquals(1, preSetRepository.count());
    }

}
