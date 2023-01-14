package br.com.wesquel.restwithspringbootandjavaerudion.integrationTests.testcontainers.swagger;

import static org.junit.Assert.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.wesquel.restwithspringbootandjavaerudion.configs.TestConfigs;
import br.com.wesquel.restwithspringbootandjavaerudion.integrationTests.testcontainers.AbstractIntegrationTest;
import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SwaggerIntegrationTest extends AbstractIntegrationTest {
    
    @Test
    void shouldDisplaySwaggerUiPage(){
        var content = given()
            .basePath("/swagger-ui/index.html")
            .port(TestConfigs.SERVER_PORT)
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
