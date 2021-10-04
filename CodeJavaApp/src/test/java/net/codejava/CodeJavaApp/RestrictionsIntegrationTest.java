package net.codejava.CodeJavaApp;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import net.codejava.CodeJavaApp.Business.*;
import net.codejava.CodeJavaApp.Restrictions.*;
//import org.junit.After;
//import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.net.URI;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestrictionsIntegrationTest {

    @LocalServerPort
    private int port;

    private final String baseUrl = "http://localhost:";

    @Autowired
    private RestrictionsService restrictionsService;

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
        restrictions.save(new Restrictions("Indoor", "Category", "Only 5 pax allowed for social gatherings"));

        ResponseEntity<Restrictions[]> result = restTemplate.getForEntity(uri, Restrictions[].class);
        Restrictions[] restrictions_array = result.getBody();

        assertEquals(200, result.getStatusCode().value());
        assert restrictions_array != null;
        assertEquals(1, restrictions_array.length);

    }

    @Test
    public void getRestriction_ValidRestrictionId_Success() throws Exception {
        Restrictions restriction = new Restrictions("Indoor", "Category", "Only 5 pax allowed for social gatherings");
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
        Restrictions restriction = new Restrictions("Indoor", "Category", "Only 5 pax allowed for social gatherings");
        users.save(new User("email", encoder.encode("goodpassword"), "firstName", "lastName"));

        ResponseEntity<Restrictions> result = restTemplate.withBasicAuth("admin", "goodpassword")
                .postForEntity(uri, restriction, Restrictions.class);

        assertEquals(201, result.getStatusCode().value());
        assertEquals(restriction.getDescription(), result.getBody().getDescription());
    }

    @Test
    public void deleteRestriction_ValidRestrictionId_Success() throws Exception {
        Restrictions restriction = restrictions.save(new Restrictions("Indoor", "Category", "Only 5 pax allowed for social gatherings"));
        URI uri = new URI(baseUrl + port + "/restrictions/" + restriction.getId().longValue());
        users.save(new User("email", encoder.encode("goodpassword"), "firstName", "lastName"));

        ResponseEntity<Void> result = restTemplate.withBasicAuth("admin", "goodpassword")
                                        .exchange(uri, HttpMethod.DELETE, null, Void.class);
        assertEquals(200, result.getStatusCode().value());
        Optional<Restrictions> emptyValue = Optional.empty();
        assertEquals(emptyValue, restrictions.findById(restriction.getId()));
    }

    @Test
    public void deleteRestriction_InvalidRestrictionId_Failure() throws Exception {
        URI uri = new URI(baseUrl + port + "/restrictions/1");
        users.save(new User("email", encoder.encode("goodpassword"), "firstName", "lastName"));

        ResponseEntity<Void> result = restTemplate.withBasicAuth("admin", "goodpassword")
                                        .exchange(uri, HttpMethod.DELETE, null, Void.class);

        assertEquals(404, result.getStatusCode().value());
    }

    @Test
    public void updateRestriction_ValidRestrictionId_Success() throws Exception {
        Restrictions restriction = restrictions.save(new Restrictions("Indoor", "Category", "Only 5 pax allowed for social gatherings"));
        URI uri = new URI(baseUrl + port + "/restrictions/" + restriction.getId().longValue());
        Restrictions newRestrictionInfo = new Restrictions("Indoor", "Category", "Only 2 pax allowed for social gatherings");
        users.save(new User("email", encoder.encode("goodpassword"), "firstName", "lastName"));

        ResponseEntity<Restrictions> result = restTemplate.withBasicAuth("admin", "goodpassword")
                                                .exchange(uri, HttpMethod.PUT, new HttpEntity<>(newRestrictionInfo), Restrictions.class);
        assertEquals(200, result.getStatusCode().value());
        assertEquals(newRestrictionInfo.getDescription(), result.getBody().getDescription());
    }

    @Test
    public void updateRestriction_InvalidRestrictionId_Failure() throws Exception {
        URI uri = new URI(baseUrl + port + "/restrictions/1");
        Restrictions newRestrictionInfo = new Restrictions("Indoor", "Category", "Only 2 pax allowed for social gatherings");
        users.save(new User("admin", encoder.encode("goodpassword"), "firstName", "lastName"));
        ResponseEntity<Restrictions> result = restTemplate.withBasicAuth("admin", "goodpassword")
                .exchange(uri, HttpMethod.PUT, new HttpEntity<>(newRestrictionInfo), Restrictions.class);
        assertEquals(404, result.getStatusCode().value());
    }
}
