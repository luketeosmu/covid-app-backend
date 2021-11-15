package net.codejava.CodeJavaApp.employee;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import net.codejava.CodeJavaApp.user.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class EmployeeController {

    @Autowired 
    private EmployeeServiceImpl employeesvc;

    @Autowired
    private UserRepository users;

    /**
     * List all employees under this user 
     * @param userId
     * @return list of all employees
     */
    @GetMapping("/users/{userid}/employees")
    public List<Employee> getEmployeesByUserId(@PathVariable (value = "userid") Long userId){
        if(!users.existsById(userId)){
            throw new UserNotFoundException(userId);
        }
        return employeesvc.getAllEmployee(userId);
    }

    /**
     * List all employees with FET expiring today
     * @param userId
     * @return list of employees who needs to do fet today
     */
    @GetMapping("/users/{userid}/employees/expired")
    public List<Employee> getByFetDate(@PathVariable (value = "userid") Long userId) {
        if(!users.existsById(userId)){
            throw new UserNotFoundException(userId);
        }
        return employeesvc.getExpiredFetEmployees(userId);
    }

    /**
     * search for employee with given id under the user
     * throw UserNotFoundException if the userid does not exist
     * throw EmployeeNotFoundException if employee does not exist 
     * @param userId
     * @param employeeId
     * @return employee
     */
    @GetMapping("/users/{userid}/employees/{employeeId}")
    public Employee getEmployeeByEmployeeIdAndUserId(@PathVariable (value = "userid") Long userId,
                                                    @PathVariable (value = "employeeId") Long employeeId){
        if(!users.existsById(userId)){
            throw new UserNotFoundException(userId);
        }
        Employee employee = employeesvc.getEmployeeByEmployeeIdAndUserId( employeeId , userId);
        if(employee == null){
            throw new EmployeeNotFoundException(employeeId);
        }
        return employee;
    }

    
    /**
     * add a new employee with POST request to "/users/{userid}/employees"
     * throw UserNotFoundException if the userid does not exist
     * check if ID already taken :throw EmployeeExistsException if yes
     * else employee set user then save 
     * @param userId
     * @param employee
     * @return added employee
     */

    @ResponseStatus(HttpStatus.CREATED)
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/users/{userid}/employees")
    public Employee addEmployee(@PathVariable (value = "userid") Long userId,@RequestBody @Valid Employee employee){
        if(!users.existsById(userId)){
            throw new UserNotFoundException(userId);
        }
        Employee employee2 = employeesvc.addEmployee(userId, employee);
        if(employee2 == null ){
            throw new EmployeeExistsException(employee.getId());
        }
        return employee2;
    }

    /**
     * Updates employee with PUT request to "/users/{userid}/employees/*"
     * @param userId
     * @param employeeId
     * @param newEmployee
     * @return updated employee
     */
    @PutMapping("/users/{userid}/employees/{employeeId}")
    public Employee updateEmployee(@PathVariable (value = "userid") Long userId ,
            @PathVariable (value = "employeeId") Long employeeId, @RequestBody Employee newEmployee) {
        if(!users.existsById(userId)){
            throw new UserNotFoundException(userId); 
        }
        Employee employee = employeesvc.updateEmployee(userId,employeeId ,newEmployee);
        if(employee == null ){
            throw new EmployeeNotFoundException(employeeId);
        }
        return employee;
    }


    /**
     * Delete employee with DELETE request to "/users/{userid}/employees/*"
     * @param userid
     * @param employeeId
     * @return ResponeEntity with status code 
     */
    @DeleteMapping("/users/{userid}/employees/{employeeId}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long userid,@PathVariable Long employeeId ){
        if(!users.existsById(userid)){
            throw new UserNotFoundException(userid);
        }
        return employeesvc.deleteEmployee(userid, employeeId);
    }


}
