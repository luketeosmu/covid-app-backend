package net.codejava.CodeJavaApp.restrictions;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import static org.hamcrest.Matchers.equalTo;
import net.codejava.CodeJavaApp.user.*;
import static io.restassured.RestAssured.*;
import java.awt.print.Book;
import java.net.URI;
import java.util.Optional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.BDDMockito.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RestrictionsIntegrationTest {

    @LocalServerPort
    private int port;

    private final String baseUrl = "http://localhost:";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private RestrictionsRepository restrictions;

    @Autowired
    private UserRepository users;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @AfterEach
    void tearDown() {
        restrictions.deleteAll();
        users.deleteAll();
    }

    // @Test
    // public void getAllRestrictions_Success() throws Exception {
    //     URI uri = new URI(baseUrl + port + "/restrictions");
    //     restrictions.save(new Restrictions("Indoor", "category", "description"));

    //     ResponseEntity<Restrictions[]> result = restTemplate.getForEntity(uri, Restrictions[].class);
    //     Restrictions[] restrictions = result.getBody();

    //     assertEquals(200, result.getStatusCode().value());
    //     assertEquals(1, restrictions.length);
    // }

//    @Test
//    public void getRestrictions_Success() throws Exception {
//        URI uri = new URI(baseUrl + port + "/restrictions");
//        restrictions.save(new Restrictions("Indoor", "category", "description"));
//
//        given().get(uri).
//                then().
//                statusCode(200).
//                body("size()", equalTo(1));
//    }

    @Test
    public void getRestriction_ValidRestrictionId_Success() throws Exception {
        Restrictions restriction = new Restrictions("Indoor", "category", "description");
        Long id = restrictions.save(restriction).getId();

        URI uri = new URI(baseUrl + port + "/restrictions/" + id);

        ResponseEntity<Restrictions> result = restTemplate.getForEntity(uri, Restrictions.class);

        assertEquals(200, result.getStatusCode().value());
        assertEquals(restriction.getDescription(), result.getBody().getDescription());
    }

    @Test
    public void getRestriction_InvalidRestrictionId_Failure() throws Exception {
        URI uri = new URI(baseUrl + port + "/restrictions/1");

        ResponseEntity<Restrictions> result = restTemplate.getForEntity(uri, Restrictions.class);

        assertEquals(404, result.getStatusCode().value());
    }

    @Test
    public void addRestriction_Success() throws Exception {
        URI uri = new URI(baseUrl + port + "/restrictions");
        Restrictions restriction = new Restrictions("Indoor", "category", "description");
        users.save(new User("hihi@gmail.com", encoder.encode("Tester123") , " firstname", "lastname", "ROLE_ADMIN"));

        ResponseEntity<Restrictions> result = restTemplate.withBasicAuth("hihi@gmail.com", "Tester123")
                .postForEntity(uri, restriction, Restrictions.class);

        assertEquals(201, result.getStatusCode().value());
        assertEquals(restriction.getDescription(), result.getBody().getDescription());
    }

    @Test
    public void deleteRestriction_ValidRestrictionId_Success() throws Exception {
        Restrictions restriction = restrictions.save(new Restrictions("Indoor", "category", "description"));
        URI uri = new URI(baseUrl + port + "/restrictions/" + restriction.getId().longValue());
        users.save(new User("hihi@gmail.com", encoder.encode("Tester123") , " firstname", "lastname", "ROLE_ADMIN"));
        ResponseEntity<Void> result = restTemplate.withBasicAuth("hihi@gmail.com", "Tester123")
                .exchange(uri, HttpMethod.DELETE, null, Void.class);
        assertEquals(200, result.getStatusCode().value());
        Optional<Restrictions> emptyValue = Optional.empty();
        assertEquals(emptyValue, restrictions.findById(restriction.getId()));
    }

    @Test
    public void deleteRestriction_InvalidRestrictionId_Failure() throws Exception {
        URI uri = new URI(baseUrl + port + "/restrictions/1");
        users.save(new User("hihi@gmail.com", encoder.encode("Tester123") , " firstname", "lastname", "ROLE_ADMIN"));
        ResponseEntity<Void> result = restTemplate.withBasicAuth("hihi@gmail.com", "Tester123")
                .exchange(uri, HttpMethod.DELETE, null, Void.class);
        assertEquals(404, result.getStatusCode().value());
    }

    @Test
    public void updateRestriction_ValidRestrictionId_Success() throws Exception {
        Restrictions restriction = restrictions.save(new Restrictions("Indoor", "category", "description"));
        URI uri = new URI(baseUrl + port + "/restrictions/" + restriction.getId().longValue());
        users.save(new User("hihi@gmail.com", encoder.encode("Tester123") , " firstname", "lastname", "ROLE_ADMIN"));
        Restrictions newRestrictionInfo = new Restrictions("Indoor", "category", "new description");
        ResponseEntity<Restrictions> result = restTemplate.withBasicAuth("hihi@gmail.com", "Tester123")
                .exchange(uri, HttpMethod.PUT, new HttpEntity<>(newRestrictionInfo), Restrictions.class);

        assertEquals(200, result.getStatusCode().value());
        assertEquals(newRestrictionInfo.getDescription(), result.getBody().getDescription());
    }

    @Test
    public void updateRestriction_InvalidRestrictionId_Failure() throws Exception {
        URI uri = new URI(baseUrl + port + "/restrictions/2");
        Restrictions newRestrictionInfo = new Restrictions("Indoor", "category", "description");
        users.save(new User("hihi@gmail.com", encoder.encode("Tester123") , " firstname", "lastname", "ROLE_ADMIN"));
        ResponseEntity<Restrictions> result = restTemplate.withBasicAuth("hihi@gmail.com", "Tester123")
                .exchange(uri, HttpMethod.PUT, new HttpEntity<>(newRestrictionInfo), Restrictions.class);
        assertEquals(404, result.getStatusCode().value());
    }
}