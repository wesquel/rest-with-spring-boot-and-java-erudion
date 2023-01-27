package br.com.wesquel.restwithspringbootandjavaerudion.integrationTests.controller.withXml;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import br.com.wesquel.restwithspringbootandjavaerudion.integrationTests.vo.security.AccountCredentialsVO;
import br.com.wesquel.restwithspringbootandjavaerudion.integrationTests.vo.wrappers.WrapperPersonVO;
import br.com.wesquel.restwithspringbootandjavaerudion.configs.TestConfigs;
import br.com.wesquel.restwithspringbootandjavaerudion.data.vo.v1.security.TokenVO;
// import br.com.wesquel.restwithspringbootandjavaerudion.integrationTests.vo.security.TokenVO;
import br.com.wesquel.restwithspringbootandjavaerudion.integrationTests.testcontainers.AbstractIntegrationTest;
import br.com.wesquel.restwithspringbootandjavaerudion.integrationTests.vo.PersonVO;
import br.com.wesquel.restwithspringbootandjavaerudion.integrationTests.vo.pagedmodels.PagedModelPerson;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class PersonControllerXmlTest extends AbstractIntegrationTest {
    
    private static RequestSpecification specification;
    private static XmlMapper objectMapper;
    private static PersonVO person;

    @BeforeAll
    public static void setup(){
        objectMapper = new XmlMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        person = new PersonVO();
    }

    @Test
	@Order(0)
	public void authorization() throws JsonMappingException, JsonProcessingException {

		AccountCredentialsVO user = new AccountCredentialsVO("leandro", "admin123");

		var accessToken = given()
			.basePath("/auth/signin")
			.port(8080)
			.contentType(TestConfigs.CONTENT_TYPE_XML)
            .accept(TestConfigs.CONTENT_TYPE_XML)
			.body(user)
			.when()
			.post()
			.then()
			.statusCode(200)
			.extract()
			.body()
			.as(TokenVO.class)
			.getAccessToken()
        ;
        		
		specification = new RequestSpecBuilder()
			.addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken)
			.setBasePath("/api/person/v1")
			.setPort(TestConfigs.SERVER_PORT)
			.addFilter(new RequestLoggingFilter(LogDetail.ALL))
			.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
			.build()
        ;
	}

    @Test
    @Order(1)
    void testCreate() throws JsonMappingException, JsonProcessingException{
        mockPerson();

        var content = given()
			.spec(specification)
			.contentType(TestConfigs.CONTENT_TYPE_XML)
			.accept(TestConfigs.CONTENT_TYPE_XML)
            .port(8080)
			.body(person)
			.when()
			.post()
			.then()
			.statusCode(200)
			.extract()
			.body()
			.asString()
        ;
        PersonVO createdPerson = objectMapper.readValue(content, PersonVO.class);
        person = createdPerson;
        assertNotNull(createdPerson);
        assertNotNull(createdPerson.getFirstName());
        assertNotNull(createdPerson.getId());
        assertNotNull(createdPerson.getLastName());
        assertNotNull(createdPerson.getGender());
        assertNotNull(createdPerson.getAddress());
        assertTrue(createdPerson.getId() >= 0);

        assertEquals("Richard", createdPerson.getFirstName());
        assertEquals("Stallman", createdPerson.getLastName());
        assertEquals("New York City, New York, US", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());


    }

    @Test
	@Order(2)
	public void testCreateWithWrongOrigin() throws JsonMappingException, JsonProcessingException {
		mockPerson();
	
		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
                .accept(TestConfigs.CONTENT_TYPE_XML)
                .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_SEMERU)
                .port(8080)
                .body(person)
				.when()
                .post()
				.then()
                .statusCode(403)
                .extract()
                .body()
                .asString()
            ;
		
		assertNotNull(content);
		assertEquals("Invalid CORS request", content);
	}

	@Test
	@Order(3)
	public void testFindById() throws JsonMappingException, JsonProcessingException {
		mockPerson();
			
		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
                .accept(TestConfigs.CONTENT_TYPE_XML)
                .header(TestConfigs.HEADER_PARAM_ORIGIN, "http://localhost:8080")
                .port(8080)
                .pathParam("id", person.getId())
                .when()
                .get("{id}")
				.then()
                .statusCode(200)
                .extract()
                .body()
                .asString()
            ;
		
		PersonVO persistedPerson = objectMapper.readValue(content, PersonVO.class);
		person = persistedPerson;
		
		assertNotNull(persistedPerson);
		
		assertNotNull(persistedPerson.getId());
		assertNotNull(persistedPerson.getFirstName());
		assertNotNull(persistedPerson.getLastName());
		assertNotNull(persistedPerson.getAddress());
		assertNotNull(persistedPerson.getGender());
		
		assertTrue(persistedPerson.getId() > 0);
		
		assertEquals("Richard", persistedPerson.getFirstName());
		assertEquals("Stallman", persistedPerson.getLastName());
		assertEquals("New York City, New York, US", persistedPerson.getAddress());
		assertEquals("Male", persistedPerson.getGender());
	}

    @Test
	@Order(4)
	public void testDelete() throws JsonMappingException, JsonProcessingException {
			
		given().spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_XML)
            .accept(TestConfigs.CONTENT_TYPE_XML)
            .port(8080)
            .pathParam("id", person.getId())
            .when()
            .delete("{id}")
            .then()
            .statusCode(204)
        ;		
	}
	

	@Test
	@Order(5)
	public void testFindByIdWithWrongOrigin() throws JsonMappingException, JsonProcessingException {
		mockPerson();
		
		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
                .accept(TestConfigs.CONTENT_TYPE_XML)
                .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_SEMERU)
                .port(8080)
                .pathParam("id", person.getId())
                .when()
                .get("{id}")
				.then()
                .statusCode(403)
                .extract()
                .body()
                .asString()
            ;

		
		assertNotNull(content);
		assertEquals("Invalid CORS request", content);
	}

    @Test
    @Order(6)
    void testFindAll() throws JsonMappingException, JsonProcessingException{
        
        var content = given()
                .spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
                .accept(TestConfigs.CONTENT_TYPE_XML)
                .queryParams("page", 1, "size", 10, "direction", "asc")
                .when()
                .port(8080)
                .get()
				.then()
                .statusCode(200)
                .extract()
                .body()
                .asString()
            ;
		
        PagedModelPerson wrapper = objectMapper.readValue(content, PagedModelPerson.class);
    
        var people = wrapper.getContent();
        
        PersonVO foundPersonOne = people.get(1);
		
		assertNotNull(foundPersonOne.getId());
		assertNotNull(foundPersonOne.getFirstName());
		assertNotNull(foundPersonOne.getLastName());
		assertNotNull(foundPersonOne.getAddress());
		assertNotNull(foundPersonOne.getGender());
		
		assertEquals(589, foundPersonOne.getId());
		
		assertEquals("Adrea", foundPersonOne.getFirstName());
		assertEquals("De Lorenzo", foundPersonOne.getLastName());
		assertEquals("2274 Ohio Terrace", foundPersonOne.getAddress());
		assertEquals("Female", foundPersonOne.getGender());

    }

    private void mockPerson(){
        person.setFirstName("Richard");
        person.setLastName("Stallman");
        person.setAddress("New York City, New York, US");
        person.setGender("Male");
		person.setEnabled(true);
    }
}
