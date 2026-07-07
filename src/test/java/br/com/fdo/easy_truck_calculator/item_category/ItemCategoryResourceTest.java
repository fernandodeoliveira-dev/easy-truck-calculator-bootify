package br.com.fdo.easy_truck_calculator.item_category;

import static org.junit.jupiter.api.Assertions.assertEquals;

import br.com.fdo.easy_truck_calculator.config.BaseIT;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;


public class ItemCategoryResourceTest extends BaseIT {

    @Test
    @Sql("/data/itemCategoryData.sql")
    void getAllItemCategories_success() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                .when()
                    .get("/api/itemCategories")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("page.totalElements", Matchers.equalTo(2))
                    .body("content.get(0).id", Matchers.equalTo(1200));
    }

    @Test
    @Sql("/data/itemCategoryData.sql")
    void getAllItemCategories_filtered() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                .when()
                    .get("/api/itemCategories?filter=1201")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("page.totalElements", Matchers.equalTo(1))
                    .body("content.get(0).id", Matchers.equalTo(1201));
    }

    @Test
    @Sql("/data/itemCategoryData.sql")
    void getItemCategory_success() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                .when()
                    .get("/api/itemCategories/1200")
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("name", Matchers.equalTo("Zed diam voluptua."));
    }

    @Test
    void getItemCategory_notFound() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                .when()
                    .get("/api/itemCategories/1866")
                .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body("code", Matchers.equalTo("NOT_FOUND"));
    }

    @Test
    void createItemCategory_success() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(readResource("/requests/itemCategoryDTORequest.json"))
                .when()
                    .post("/api/itemCategories")
                .then()
                    .statusCode(HttpStatus.CREATED.value());
        assertEquals(1, itemCategoryRepository.count());
    }

    @Test
    @Sql("/data/itemCategoryData.sql")
    void updateItemCategory_success() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .body(readResource("/requests/itemCategoryDTORequest.json"))
                .when()
                    .put("/api/itemCategories/1200")
                .then()
                    .statusCode(HttpStatus.OK.value());
        assertEquals("Duis autem vel.", itemCategoryRepository.findById(((long)1200)).orElseThrow().getName());
        assertEquals(2, itemCategoryRepository.count());
    }

    @Test
    @Sql("/data/itemCategoryData.sql")
    void deleteItemCategory_success() {
        RestAssured
                .given()
                    .accept(ContentType.JSON)
                .when()
                    .delete("/api/itemCategories/1200")
                .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());
        assertEquals(1, itemCategoryRepository.count());
    }

}
