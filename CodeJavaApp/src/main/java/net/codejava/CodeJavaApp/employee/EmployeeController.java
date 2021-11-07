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

    @GetMapping("/users/{userid}/employees")
    public List<Employee> getEmployeesByUserId(@PathVariable (value = "userid") Long userId){
        if(!users.existsById(userId)){
            throw new UserNotFoundException(userId);
        }
        return employeesvc.getAllEmployee(userId);
    }

    @GetMapping("/users/{userid}/employees/expired")
    public List<Employee> getByFetDate(@PathVariable (value = "userid") Long userId) {
        if(!users.existsById(userId)){
            throw new UserNotFoundException(userId);
        }
        return employeesvc.findbyExpireFetDate(userId);
    }

    @GetMapping("/users/{userid}/employees/today")
    public List<Employee> listTodayTests(@PathVariable (value = "userid") Long userId) {
        if(!users.existsById(userId)){
            throw new UserNotFoundException(userId);
        }
        return employeesvc.listTodayTests(userId);
    }

    @GetMapping("/users/{userid}/employees/{employeeId}")
    public Employee getEmployeeByEmployeeIdAndUserId(@PathVariable (value = "userId") Long userId,@PathVariable (value = "employeeId") Long employeeId){
        if(!users.existsById(userId)){
            throw new UserNotFoundException(userId);
        }
        return employeesvc.getEmployeeByEmployeeIdAndUserId( employeeId,userId);
    }

    

    @ResponseStatus(HttpStatus.CREATED)
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/users/{userid}/employees")
    public Employee addEmployee(@PathVariable (value = "userid") Long userId,@RequestBody @Valid Employee employee){
        return employeesvc.addEmployee(userId, employee);
    }

    @PutMapping("/users/{userid}/employees/{employeeId}")
    public Employee updateEmployee(@PathVariable (value = "userid") Long userId ,
            @PathVariable (value = "employeeId") Long employeeId, @RequestBody Employee newEmployee) {
        return employeesvc.updateEmployee(userId,employeeId ,newEmployee);
    }

    @DeleteMapping("/users/{userid}/employees/{employeeId}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long userid,@PathVariable Long employeeId ){
        return employeesvc.deleteEmployee(userid, employeeId);
    }


}
