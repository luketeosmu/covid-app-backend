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

    @Test
    public void getUsers_Success() throws Exception {
        users.save(new User("hihi4@gmail.com", encoder.encode("Tester123"), "john", "luke", "ROLE_ADMIN"));
        URI uri = new URI(baseUrl + port + "/users");
        ResponseEntity<User[]> result = restTemplate.withBasicAuth("email", "password").getForEntity(uri, User[].class);
        User[] users = result.getBody();

        assertEquals(1, users.length);
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void addUser_Success() throws Exception {
        URI uri = new URI(baseUrl + port + "/users");
        User user = new User("email", encoder.encode("password"), "luke", "handsome","ROLE_ADMIN");
        // User user = users.save(new User("hihi@gmail.com", "Tester123", " firstname",
        // "lastname", "ROLE_ADMIN"));
        ResponseEntity<User> result = restTemplate.postForEntity(uri,user,User.class);

        // assertEquals(201, result.getStatusCode());
        assertEquals(user.getUsername(), result.getBody().getUsername());
    }

    // @Test
    // public void updateUser_ValidUserId_Success() throws Exception {
    // User userToUpdate = new User("user@gmail.com", encoder.encode("password"),
    // "luke", "handsome", "ROLE_USER");
    // User admin = new User("admin@gmail.com", encoder.encode("password"), "luke",
    // "handsome", "ROLE_ADMIN");
    // User newUser = new User("newuser@gmail.com", encoder.encode("newPassword"),
    // "JOHN", "ugly", "ROLE_USER");
    // Long userId = users.save(userToUpdate).getId().longValue();
    // users.save(admin);
    //
    // URI uri = new URI(baseUrl + port + "/users/" + userId);
    // ResponseEntity<User> result = restTemplate.withBasicAuth("admin@gmail.com",
    // "password")
    // .exchange(uri, HttpMethod.PUT, new HttpEntity<>(newUser), User.class);
    // assertEquals(200, result.getStatusCode().value());
    // assertEquals(newUser.getFirstName(), result.getBody().getFirstName());
    // }
    //
    // @Test
    // public void updateUser_InvalidUserId_Failure() throws Exception {
    // User admin = new User("admin@gmail.com", encoder.encode("password"), "luke",
    // "handsome", "ROLE_ADMIN");
    // User newUser = new User("user@gmail.com", encoder.encode("password"), "JOHN",
    // "handsome", "ROLE_USER");
    // users.save(admin);
    //
    // URI uri = new URI(baseUrl + port + "/users/" + 10L);
    // HttpHeaders headers = new HttpHeaders();
    //// headers.setContentType(MediaType.APPLICATION_JSON);
    // HttpEntity<User> httpEntity = new HttpEntity<User>(newUser);
    // ResponseEntity<User> result = restTemplate.withBasicAuth("admin@gmail.com",
    // "password")
    // .exchange(uri, HttpMethod.PUT, httpEntity, User.class);
    // assertEquals(404, result.getStatusCode().value());
    // }
}
