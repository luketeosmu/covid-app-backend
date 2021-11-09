package net.codejava.CodeJavaApp.user;


import net.codejava.CodeJavaApp.restrictions.Restrictions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.net.URI;
import java.util.List;

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

    //  @Test
    //  public void getUsers_Success() throws Exception {
    //      URI uri = new URI(baseUrl + port + "/users");
    //      users.save(new User("hihi4@gmail.com", encoder.encode("Tester123"), "john","luke","ROLE_ADMIN"));
    //      ResponseEntity<User[]> result = restTemplate.withBasicAuth("email", "password")
    //                                      .getForEntity(uri, User[].class);
    //      User[] users = result.getBody();

    //      assertEquals(200, result.getStatusCode().value());
    //      assertEquals(1, users.length);
    //  }
    // @Test
    // public void addUser_Success() throws Exception {
    //     URI uri = new URI(baseUrl + port + "/users");
    // //    User user = new User("email", encoder.encode("password"), "luke", "handsome", "ROLE_ADMIN");
    //     User user = users.save(new User("hihi@gmail.com", "Tester123" 
    //     , " firstname", "lastname", "ROLE_ADMIN"));

    //     ResponseEntity<User> result = restTemplate.postForEntity(uri, user, User.class);

    //     assertEquals(201, result.getStatusCode().value());
    //     assertEquals(user.getUsername(), result.getBody().getUsername());
    // }
}

