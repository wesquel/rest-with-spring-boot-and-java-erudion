package br.com.wesquel.restwithspringbootandjavaerudion.integrationTests.controller.withXml;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import br.com.wesquel.restwithspringbootandjavaerudion.configs.TestConfigs;
import br.com.wesquel.restwithspringbootandjavaerudion.integrationTests.testcontainers.AbstractIntegrationTest;
import br.com.wesquel.restwithspringbootandjavaerudion.integrationTests.vo.security.AccountCredentialsVO;
import br.com.wesquel.restwithspringbootandjavaerudion.integrationTests.vo.security.TokenVO;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class AuthControllerXmlTest extends AbstractIntegrationTest {

    private static TokenVO tokenVO;

    @Test
	@Order(0)
	public void testSignin() throws JsonMappingException, JsonProcessingException {

		AccountCredentialsVO user = new AccountCredentialsVO("leandro", "admin123");
		
		tokenVO = given()
			.basePath("/auth/signin")
			.port(8080)
			.contentType(TestConfigs.CONTENT_TYPE_XML)
			.body(user)
			.when()
			.post()
			.then()
			.statusCode(200)
			.extract()
			.body()
			.as(TokenVO.class)
        ;

        assertNotNull(tokenVO.getAccessToken());
        assertNotNull(tokenVO.getRefreshToken());		
	}

    @Test
	@Order(1)
	public void testRefresh() throws JsonMappingException, JsonProcessingException {
		
		var newTokenVO = given()
			.basePath("/auth/refresh")
			.port(8080)
			.contentType(TestConfigs.CONTENT_TYPE_XML)
			.pathParam("username", tokenVO.getUsername())
            .header(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenVO.getRefreshToken())
			.when()
			.put("{username}")
			.then()
			.statusCode(200)
			.extract()
			.body()
			.as(TokenVO.class)
        ;
		
        assertNotNull(newTokenVO.getAccessToken());
        assertNotNull(newTokenVO.getRefreshToken());
	}

}
