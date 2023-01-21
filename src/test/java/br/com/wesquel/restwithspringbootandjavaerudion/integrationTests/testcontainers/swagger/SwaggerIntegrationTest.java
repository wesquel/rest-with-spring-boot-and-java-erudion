package br.com.wesquel.restwithspringbootandjavaerudion.integrationTests.testcontainers.swagger;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.wesquel.restwithspringbootandjavaerudion.integrationTests.testcontainers.AbstractIntegrationTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SwaggerIntegrationTest extends AbstractIntegrationTest {
    
    @Test
    void shouldDisplaySwaggerUiPage(){
        var content = given()
            .basePath("/swagger-ui/index.html")
            .port(8080)
            .when()
            .get() 
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()
            ;
        assertTrue(content.contains("Swagger UI"));

    }
}
