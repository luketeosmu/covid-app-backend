package net.codejava.CodeJavaApp.Business;

import net.codejava.CodeJavaApp.Business.*;
import net.codejava.CodeJavaApp.user.User;
import net.codejava.CodeJavaApp.user.UserRepository;
import net.codejava.CodeJavaApp.user.UserService;
import org.apache.coyote.Response;
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

import com.nimbusds.openid.connect.sdk.claims.UserInfo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BusinessIntegrationTest {
    @LocalServerPort
    private int port;

    private final String baseUrl = "http://localhost:";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository users;

    @Autowired
    private BusinessRepository businesses;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @AfterEach
    void tearDown() {
        users.deleteAll();
        businesses.deleteAll();
    }

    @Test
    public void addBusiness_Success() throws Exception {
        User user = new User("test@gmail.com", encoder.encode("Test12345"), "tes", "tes", "ROLE_ADMIN");
        Long userId = users.save(user).getId();
        Business business = new Business(1L, "name", "category", 'O', 20L, user);
        // business.setUser(user);
        URI uri = new URI(baseUrl + port + "/users/" + userId + "/businesses");
        ResponseEntity<Business> result = restTemplate.postForEntity(uri, business, Business.class);
        assertEquals(201, result.getStatusCode().value());
        assertEquals(business.getBusinessName(), result.getBody().getBusinessName());
    }

    @Test
    public void addBusiness_Failure() throws Exception {
        User user = new User("test@gmail.com", encoder.encode("Test12345"), "tes", "tes", "ROLE_ADMIN");
        Long userId = users.save(user).getId();
        Business business = new Business(1L, "name", "category", 'O', 20L, user);
        businesses.save(business);
        // business.setUser(user);
        URI uri = new URI(baseUrl + port + "/users/" + userId + "/businesses");
        ResponseEntity<Business> result = restTemplate.postForEntity(uri, business, Business.class);
        assertEquals(409, result.getStatusCode().value());
        assertNull(result.getBody().getBusinessName());
    }

    @Test
    public void getBusinesses_Success() throws Exception {
        User user = new User("test@gmail.com", encoder.encode("Test12345"), "tes", "tes", "ROLE_ADMIN");
        Long userId = users.save(user).getId();
        URI uri = new URI(baseUrl + port + "/users/" + userId + "/businesses");

        businesses.save(new Business(1L, "name", "category", 'O', 20L, user));

        // Need to use array with a ReponseEntity here
        ResponseEntity<Business[]> result = restTemplate.getForEntity(uri, Business[].class);
        Business[] businessArr = result.getBody();

        assertEquals(200, result.getStatusCode().value());
        assertEquals(1, businessArr.length);
    }

    @Test
    public void getBusiness_InvalidBusinessId_Failure() throws Exception {
        User user = new User("test@gmail.com", encoder.encode("Test12345"), "tes", "tes", "ROLE_ADMIN");
        Long userId = users.save(user).getId();
        Business business = new Business("name", "category", 'O', 20L);
        URI uri = new URI(baseUrl + port + "/users/" + userId + "/businesses/" + 22L);

        ResponseEntity<Business> result = restTemplate.getForEntity(uri, Business.class);

        assertEquals(404, result.getStatusCode().value());
    }

    @Test
    public void getBusiness_ValidBusinessId_Success() throws Exception {
        User user = new User("test@gmail.com", encoder.encode("Test12345"), "tes", "tes", "ROLE_ADMIN");
        Long userId = users.save(user).getId();
        Business business = businesses.save(new Business(1L, "name", "category", 'O', 20L, user));
        URI uri = new URI(baseUrl + port + "/users/" + userId + "/businesses/" + business.getBusinessId());

        ResponseEntity<Business> result = restTemplate.getForEntity(uri, Business.class);

        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void deleteBusiness_ValidBusinessId_Success() throws Exception {
        User user = new User("test@gmail.com", encoder.encode("Test12345"), "tes", "tes", "ROLE_ADMIN");
        Long userId = users.save(user).getId();
        Business business = businesses.save(new Business(1L, "name", "category", 'O', 20L, user));
        URI uri = new URI(baseUrl + port + "/users/" + userId + "/businesses/" + business.getBusinessId());

        ResponseEntity<Void> result = restTemplate.exchange(uri, HttpMethod.DELETE, null, Void.class);
        assertEquals(200, result.getStatusCode().value());

        Optional<Business> emptyValue = Optional.empty();
        assertEquals(emptyValue, businesses.findById(business.getBusinessId()));
    }

    @Test
    public void deleteBusiness_InvalidBusinessId_Failure() throws Exception {
        User user = new User("test@gmail.com", encoder.encode("Test12345"), "tes", "tes", "ROLE_ADMIN");
        Long userId = users.save(user).getId();
        // Business business = businesses.save(new Business(1L,"name", "category",'O',
        // 20L,user));
        URI uri = new URI(baseUrl + port + "/users/" + userId + "/businesses/" + 1L);

        ResponseEntity<Void> result = restTemplate.exchange(uri, HttpMethod.DELETE, null, Void.class);
        assertEquals(404, result.getStatusCode().value());
    }

    @Test
    public void updateBusiness_ValidBusinessId_Success() throws Exception {
        User user = new User("test@gmail.com", encoder.encode("Test12345"), "tes", "tes", "ROLE_ADMIN");
        Long userId = users.save(user).getId();
        Business business = businesses.save(new Business(1L, "name", "category", 'O', 20L, user));
        URI uri = new URI(baseUrl + port + "/users/" + userId + "/businesses/" + business.getBusinessId());
        Business newBusiness = new Business("newname", "category", 'O', 20L);

        ResponseEntity<Business> result = restTemplate.exchange(uri, HttpMethod.PUT, new HttpEntity<>(newBusiness),
                Business.class);
        assertEquals(200, result.getStatusCode().value());
        assertEquals(newBusiness.getBusinessName(), result.getBody().getBusinessName());

    }

    @Test
    public void updateBusiness_InvalidBusinessId_Failure() throws Exception {
        User user = new User("test@gmail.com", encoder.encode("Test12345"), "tes", "tes", "ROLE_ADMIN");
        Long userId = users.save(user).getId();
        URI uri = new URI(baseUrl + port + "/users/" + userId + "/businesses/" + 20L);
        Business newBusiness = new Business("newname", "category", 'O', 20L);

        ResponseEntity<Business> result = restTemplate.exchange(uri, HttpMethod.PUT, new HttpEntity<>(newBusiness),
                Business.class);
        assertEquals(404, result.getStatusCode().value());

    }
}
