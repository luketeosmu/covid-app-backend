package net.codejava.CodeJavaApp.user;

import net.codejava.CodeJavaApp.restrictions.Restrictions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserIntegrationTest {

    @LocalServerPort
    private int port;

    private final String baseUrl = "http://localhost:";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository users;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @AfterEach
    void tearDown() {
        users.deleteAll();
    }

    @Test
    public void addUser_InvalidPassword_Failure() throws Exception {
        URI uri = new URI(baseUrl + port + "/users");
        User user = new User("email@gmail.com", "1234567", "firstname", "lastname", "ROLE_ADMIN");

        ResponseEntity<User> result = restTemplate.postForEntity(uri, user, User.class);

        assertEquals(400, result.getStatusCode().value());
    }

    @Test
    public void addUser_InvalidEmail_Failure() throws Exception {
        URI uri = new URI(baseUrl + port + "/users");
        User user = new User("email", "12345678", "firstname", "lastname", "ROLE_ADMIN");

        ResponseEntity<User> result = restTemplate.postForEntity(uri, user, User.class);

        assertEquals(400, result.getStatusCode().value());
    }

    @Test
    public void addUser_LastNameNull_Failure() throws Exception {
        User user = new User("user@gmail.com", encoder.encode("password123"), "luke", null, "ROLE_USER");
        User admin = new User("admin@gmail.com", encoder.encode("password"), "luke", "handsome", "ROLE_ADMIN");
        users.save(admin);
        URI uri = new URI(baseUrl + port + "/users");
        ResponseEntity<User> result = restTemplate.withBasicAuth("admin@gmail.com", "password").postForEntity(uri, user,
                User.class);
        assertEquals(result.getStatusCode().value(), 400);
    }

    @Test
    public void addUser_FirstNameNull_Failure() throws Exception {
        User user = new User("user@gmail.com", encoder.encode("password123"), null, "handsome", "ROLE_USER");
        User admin = new User("admin@gmail.com", encoder.encode("password"), "luke", "handsome", "ROLE_ADMIN");
        users.save(admin);
        URI uri = new URI(baseUrl + port + "/users");
        ResponseEntity<User> result = restTemplate.withBasicAuth("admin@gmail.com", "password").postForEntity(uri, user,
                User.class);
        assertEquals(result.getStatusCode().value(), 400);
    }

    @Test
    public void deleteUser_ValidUserId_Success() throws Exception {
        User userToDelete = new User("user@gmail.com", encoder.encode("password"), "luke", "handsome", "ROLE_USER");
        User admin = new User("admin@gmail.com", encoder.encode("password"), "luke", "handsome", "ROLE_ADMIN");
        Long userId = users.save(userToDelete).getId();
        users.save(admin);

        URI uri = new URI(baseUrl + port + "/users/" + userId);
        ResponseEntity<Void> result = restTemplate.withBasicAuth("admin@gmail.com", "password").exchange(uri,
                HttpMethod.DELETE, null, Void.class);
        assertEquals(200, result.getStatusCode().value());
        Optional<User> emptyValue = Optional.empty();
        assertEquals(emptyValue, users.findById(userId));
    }

    @Test
    public void deleteUser_InvalidUserId_Failure() throws Exception {
        User admin = new User("admin@gmail.com", encoder.encode("password"), "luke", "handsome", "ROLE_ADMIN");
        users.save(admin);

        URI uri = new URI(baseUrl + port + "/users/" + 23L);
        ResponseEntity<Void> result = restTemplate.withBasicAuth("admin@gmail.com", "password").exchange(uri,
                HttpMethod.DELETE, null, Void.class);
        assertEquals(404, result.getStatusCode().value());
    }

}
