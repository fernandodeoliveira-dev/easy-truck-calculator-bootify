package br.com.fdo.easy_truck_calculator.item;

import static org.junit.jupiter.api.Assertions.assertEquals;

import br.com.fdo.easy_truck_calculator.config.BaseIT;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.UUID;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;


public class ItemResourceTest extends BaseIT {

    @Test
    @Sql("/data/itemData.sql")
    void getAllItems_success() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                .when()
                    .get("/api/items")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("page.totalElements", Matchers.equalTo(2))
                    .body("content.get(0).id", Matchers.equalTo("a96e0a04-d20f-3096-bc64-dac2d639a577"));
    }

    @Test
    @Sql("/data/itemData.sql")
    void getAllItems_filtered() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                .when()
                    .get("/api/items?filter=b8bff625-bdb0-3939-92c9-d4db0c6bbe45")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("page.totalElements", Matchers.equalTo(1))
                    .body("content.get(0).id", Matchers.equalTo("b8bff625-bdb0-3939-92c9-d4db0c6bbe45"));
    }

    @Test
    @Sql("/data/itemData.sql")
    void getItem_success() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                .when()
                    .get("/api/items/a96e0a04-d20f-3096-bc64-dac2d639a577")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("name", Matchers.equalTo("Zed diam voluptua."));
    }

    @Test
    void getItem_notFound() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                .when()
                    .get("/api/items/23a93ba8-9a5b-3c6c-a26e-49b88973f46e")
                .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body("code", Matchers.equalTo("NOT_FOUND"));
    }

    @Test
    void createItem_success() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(readResource("/requests/itemDTORequest.json"))
                .when()
                    .post("/api/items")
                .then()
                    .statusCode(HttpStatus.CREATED.value());
        assertEquals(1, itemRepository.count());
    }

    @Test
    void createItem_missingField() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(readResource("/requests/itemDTORequest_missingField.json"))
                .when()
                    .post("/api/items")
                .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("code", Matchers.equalTo("VALIDATION_FAILED"))
                    .body("fieldErrors.get(0).property", Matchers.equalTo("name"))
                    .body("fieldErrors.get(0).code", Matchers.equalTo("REQUIRED_NOT_NULL"));
    }

    @Test
    @Sql("/data/itemData.sql")
    void updateItem_success() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(readResource("/requests/itemDTORequest.json"))
                .when()
                    .put("/api/items/a96e0a04-d20f-3096-bc64-dac2d639a577")
                .then()
                    .statusCode(HttpStatus.OK.value());
        assertEquals("Duis autem vel.", itemRepository.findById(UUID.fromString("a96e0a04-d20f-3096-bc64-dac2d639a577")).orElseThrow().getName());
        assertEquals(2, itemRepository.count());
    }

    @Test
    @Sql("/data/itemData.sql")
    void deleteItem_success() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                .when()
                    .delete("/api/items/a96e0a04-d20f-3096-bc64-dac2d639a577")
                .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());
        assertEquals(1, itemRepository.count());
    }

}
