package net.codejava.CodeJavaApp.employee;

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
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeIntegrationTest {

    @LocalServerPort
    private int port;

    private final String baseUrl = "http://localhost:";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository users;

    @Autowired
    private EmployeeRepository employees;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @AfterEach
    void tearDown() {
        users.deleteAll();
        employees.deleteAll();
    }

    @Test
    public void addEmployee_NewEmployeeId_Success() throws Exception {
        Employee employee = new Employee(1L, "luke", true);
        User user = new User("test@gmail.com", encoder.encode("Test12345"), "tes", "tes", "ROLE_ADMIN");
        Long userId = users.save(user).getId();
        employee.setUser(user);
        // employees.save(employee);
        URI uri = new URI(baseUrl + port + "/users/" + userId + "/employees");
        ResponseEntity<Employee> result = restTemplate.postForEntity(uri, employee, Employee.class);
        assertEquals(201, result.getStatusCode().value());
    }

    @Test
    public void addEmployee_SameEmployeeId_Failure() throws Exception {
        User user = new User("test@gmail.com", encoder.encode("Test12345"), "tes", "tes", "ROLE_ADMIN");
        Long userId = users.save(user).getId();
        Employee employee = new Employee(1L, "luke", true);
        employee.setUser(user);
        employees.save(employee);

        URI uri = new URI(baseUrl + port + "/users/" + userId + "/employees");
        ResponseEntity<Employee> result = restTemplate.postForEntity(uri, employee, Employee.class);
        assertEquals(409, result.getStatusCode().value());
    }

    @Test
    public void deleteEmployee_ValidEmployeeId_Success() throws Exception {
        Employee toDelete = new Employee(100L, "luke", true);
        User user = new User("test@gmail.com", encoder.encode("Test12345"), "tes", "tes", "ROLE_ADMIN");
        Long userId = users.save(user).getId();
        toDelete.setUser(user);
        employees.save(toDelete);
        URI uri = new URI(baseUrl + port + "/users/" + userId + "/employees/" + toDelete.getId());
        ResponseEntity<Void> result = restTemplate.withBasicAuth("test@gmail.com", "Test12345").exchange(uri,
                HttpMethod.DELETE, null, Void.class);
        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    public void deleteEmployee_InvalidEmployeeId_Failure() throws Exception {
        User user = new User("test@gmail.com", encoder.encode("Test12345"), "tes", "tes", "ROLE_ADMIN");
        Long userId = users.save(user).getId();
        URI uri = new URI(baseUrl + port + "/users/" + userId + "/employees/" + 100L);
        ResponseEntity<Void> result = restTemplate.withBasicAuth("test@gmail.com", "Test12345").exchange(uri,
                HttpMethod.DELETE, null, Void.class);
        assertEquals(404, result.getStatusCode().value());
    }

    @Test
    public void updateEmployee_ValidEmployeeId_Success() throws Exception {
        Employee toUpdate = new Employee(100L, "luke", true);
        Employee newEmployeeInfo = new Employee(100L, "John", false);
        User user = new User("test@gmail.com", encoder.encode("Test12345"), "tes", "tes", "ROLE_ADMIN");
        Long userId = users.save(user).getId();
        toUpdate.setUser(user);
        employees.save(toUpdate).getId();
        URI uri = new URI(baseUrl + port + "/users/" + userId + "/employees/" + toUpdate.getId());
        ResponseEntity<Employee> result = restTemplate.withBasicAuth("test@gmail.com", "Test12345").exchange(uri,
                HttpMethod.PUT, new HttpEntity<>(newEmployeeInfo), Employee.class);
        assertEquals(200, result.getStatusCode().value());
        assertEquals(result.getBody().getName(), newEmployeeInfo.getName());
    }

    @Test
    public void updateEmployee_InvalidEmployeeId_Failure() throws Exception {
        Employee newEmployeeInfo = new Employee(100L, "John", false);
        User user = new User("test@gmail.com", encoder.encode("Test12345"), "tes", "tes", "ROLE_ADMIN");
        Long userId = users.save(user).getId();
        URI uri = new URI(baseUrl + port + "/users/" + userId + "/employees/" + 100L);
        ResponseEntity<Employee> result = restTemplate.withBasicAuth("test@gmail.com", "Test12345").exchange(uri,
                HttpMethod.PUT, new HttpEntity<>(newEmployeeInfo), Employee.class);
        assertEquals(result.getStatusCode().value(), 404);
    }

    @Test
    public void getEmployees_Success() throws Exception {
        Employee employee = new Employee(1L, "luke", true);
        User user = new User("test@gmail.com", encoder.encode("Test12345"), "tes", "tes", "ROLE_ADMIN");
        Long userId = users.save(user).getId();
        employee.setUser(user);
        employees.save(employee);
        URI uri = new URI(baseUrl + port + "/users/" + userId + "/employees");
        ResponseEntity<Employee[]> result = restTemplate.getForEntity(uri, Employee[].class);
        Employee[] listEmployees = result.getBody();

        assertEquals(200, result.getStatusCode().value());
        assertEquals(1, listEmployees.length);
    }
    @Test
    public void getEmployee_ValidEmployeeId_Success() throws Exception {
        User user = new User("test@gmail.com", encoder.encode("Test12345"), "tes", "tes", "ROLE_ADMIN");
        Long userId = users.save(user).getId();
        Employee employee = new Employee(123L, "EmployeeName", true);
        employee.setUser(user);
        employees.save(employee);

        URI uri = new URI(baseUrl + port + "/users/" + userId + "/employees/" + employee.getId());

        ResponseEntity<Employee> result = restTemplate.getForEntity(uri, Employee.class);

        assertEquals(200, result.getStatusCode().value());
    }
    
    @Test
    public void getEmployee_InvalidEmployeeId_Failure() throws Exception {
        User user = new User("test@gmail.com", encoder.encode("Test12345"), "tes", "tes", "ROLE_ADMIN");
        Long userId = users.save(user).getId();
        Employee employee = new Employee(123L, "luke", true);

        URI uri = new URI(baseUrl + port + "/users/" + userId + "/employees" + employee.getId());
        ResponseEntity<Employee> result = restTemplate.getForEntity(uri, Employee.class);

        assertEquals(404, result.getStatusCode().value());
    }


    @Test
    public void getExpireFetEmployees_Success() throws Exception {
        User user = new User("test@gmail.com", encoder.encode("Test12345"), "tes", "tes", "ROLE_ADMIN");
        Long userId = users.save(user).getId();
        Employee employee = new Employee(123L, "EmployeeName", true);
        employee.setUser(user);
        employee.setFetDate(new Date(1635724800)); // November14 for testing
        employees.save(employee);

        URI uri = new URI(baseUrl + port + "/users/" + userId + "/employees/expired");

        ResponseEntity<Employee[]> result = restTemplate.getForEntity(uri, Employee[].class);
        Employee[] list = result.getBody();

        assertEquals(200, result.getStatusCode().value());
        assertEquals(1, list.length);
    }

    @Test
    public void getExpireFetEmployees_Failure() throws Exception {
        User user = new User("test@gmail.com", encoder.encode("Test12345"), "tes", "tes", "ROLE_ADMIN");
        Long userId = users.save(user).getId();
        Employee employee = new Employee(123L, "EmployeeName", true);
        employee.setUser(user);
        employee.setFetDate(new Date(1635724800)); // november14 for testing
        employees.save(employee);

        URI uri = new URI(baseUrl + port + "/users/" + 23L + "/employees/expired");

        ResponseEntity<Employee> result = restTemplate.getForEntity(uri, Employee.class);

        assertEquals(404, result.getStatusCode().value());
    }

    // Invalid UserId Tests
    @Test
    public void getEmployees_InvalidUserId_Failure() throws Exception {
        Employee employee = new Employee(1L, "luke", true);
        User user = new User("test@gmail.com", encoder.encode("Test12345"), "tes", "tes", "ROLE_ADMIN");
        Long userId = users.save(user).getId();
        employee.setUser(user);
        employees.save(employee);
        URI uri = new URI(baseUrl + port + "/users/" + 10L + "/employees");
        ResponseEntity<Employee> result = restTemplate.getForEntity(uri, Employee.class);

        assertEquals(404, result.getStatusCode().value());
    }
    
    @Test
    public void getEmployee_InvalidUserId_Failure() throws Exception {
        User user = new User("test@gmail.com", encoder.encode("Test12345"), "tes", "tes", "ROLE_ADMIN");
        Long userId = users.save(user).getId();
        Employee employee = new Employee(123L, "EmployeeName", true);
        employee.setUser(user);
        employees.save(employee);
        
        URI uri = new URI(baseUrl + port + "/users/" + 10L + "/employees/" + employee.getId());
        
        ResponseEntity<Employee> result = restTemplate.getForEntity(uri, Employee.class);
        
        assertEquals(404, result.getStatusCode().value());
    }
    
    @Test
    public void addEmployee_InvalidUserId_Failure() throws Exception {
        Employee employee = new Employee(1L, "luke", true);
        User user = new User("test@gmail.com", encoder.encode("Test12345"), "tes", "tes", "ROLE_ADMIN");
        users.save(user);
        employee.setUser(user);
        // employees.save(employee);
        URI uri = new URI(baseUrl + port + "/users/" + 10L + "/employees");
        ResponseEntity<Employee> result = restTemplate.postForEntity(uri, employee, Employee.class);
        assertEquals(404, result.getStatusCode().value());
    }
    
    @Test
    public void updateEmployee_InvalidUserId_Failure() throws Exception {
        Employee toUpdate = new Employee(100L, "luke", true);
        Employee newEmployeeInfo = new Employee(100L, "John", false);
        User user = users.save(new User("test@gmail.com", encoder.encode("Test12345"), "tes", "tes", "ROLE_ADMIN"));
        toUpdate.setUser(user);
        employees.save(toUpdate).getId();
        URI uri = new URI(baseUrl + port + "/users/" + 10L+ "/employees/" + toUpdate.getId());
        ResponseEntity<Employee> result = restTemplate.exchange(uri,
                HttpMethod.PUT, new HttpEntity<>(newEmployeeInfo), Employee.class);
        assertEquals(404, result.getStatusCode().value());
    }
    
    @Test
    public void deleteEmployee_InvalidUserId_Failure() throws Exception {
        Employee toDelete = new Employee(100L, "luke", true);
        User user = new User("test@gmail.com", encoder.encode("Test12345"), "tes", "tes", "ROLE_ADMIN");
        Long userId = users.save(user).getId();
        toDelete.setUser(user);
        employees.save(toDelete);
        URI uri = new URI(baseUrl + port + "/users/" + 10L + "/employees/" + toDelete.getId());
        ResponseEntity<Void> result = restTemplate.withBasicAuth("test@gmail.com", "Test12345").exchange(uri,
                HttpMethod.DELETE, null, Void.class);
        assertEquals(404, result.getStatusCode().value());
    }
}
