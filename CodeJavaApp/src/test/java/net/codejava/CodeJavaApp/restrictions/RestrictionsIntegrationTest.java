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

import net.codejava.CodeJavaApp.user.*;

import java.awt.print.Book;
import java.net.URI;
import java.util.Optional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @Test
    public void getRestrictions_Success() throws Exception {
        URI uri = new URI(baseUrl + port + "/restrictions");
        restrictions.save(new Restrictions("Indoor", "category", "description"));

        ResponseEntity<Restrictions[]> result = restTemplate.getForEntity(uri, Restrictions[].class);
        Restrictions[] restrictions = result.getBody();

        assertEquals(200, result.getStatusCode().value());
        assertEquals(1, restrictions.length);
    }

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
//        users.save(new User("admin", encoder.encode("goodpassword"), "ROLE_ADMIN"));

        ResponseEntity<Restrictions> result = restTemplate.postForEntity(uri, restriction, Restrictions.class);

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
        Restrictions newRestrictionInfo = new Restrictions("Indoor", "category", "new description");
        ResponseEntity<Restrictions> result = restTemplate.exchange(uri, HttpMethod.PUT, new HttpEntity<>(newRestrictionInfo), Restrictions.class);

        assertEquals(200, result.getStatusCode().value());
        assertEquals(newRestrictionInfo.getDescription(), result.getBody().getDescription());
    }

    @Test
    public void updateRestriction_InvalidRestrictionId_Failure() throws Exception {
        URI uri = new URI(baseUrl + port + "/restrictions/1");
        Restrictions newRestrictionInfo = new Restrictions("Indoor", "category", "description");
        ResponseEntity<Restrictions> result = restTemplate.exchange(uri, HttpMethod.PUT, new HttpEntity<>(newRestrictionInfo), Restrictions.class);
        assertEquals(404, result.getStatusCode().value());
    }
}
/*
users.save(new User("admin", encoder.encode("goodpassword"), "ROLE_ADMIN"));

		ResponseEntity<Book> result = restTemplate.withBasicAuth("admin", "goodpassword")
										.postForEntity(uri, book, Book.class);
			
*/