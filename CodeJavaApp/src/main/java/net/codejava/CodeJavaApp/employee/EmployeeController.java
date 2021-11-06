package net.codejava.CodeJavaApp.employee;

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

import net.codejava.CodeJavaApp.Business.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class EmployeeController {

    @Autowired 
    private EmployeeServiceImpl employeesvc;

    @Autowired
    private BusinessRepository businesses;

    @GetMapping("/users/{userid}/businesses/{businessId}/employees")
    public List<Employee> getEmployeesByBusinessId(@PathVariable (value = "businessId") Long businessId){
        if(!businesses.existsById(businessId)){
            throw new BusinessNotFoundException(businessId);
        }
        return employeesvc.getAllEmployee(businessId);
    }

    @GetMapping("/users/{userid}/businesses/{businessid}/employees/{employeeId}")
    public Employee getEmployeeBybusinessId(@PathVariable (value = "businessId") Long userId,@PathVariable (value = "businessId") Long businessId, @PathVariable (value = "employeeId") Long employeeId){
        if(!businesses.existsById(businessId)){
            throw new BusinessNotFoundException(businessId);
        }
        return employeesvc.getEmployeeByEmployeeIdAndBusinessId( employeeId,businessId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/users/{userid}/businesses/{businessId}/employees")
    public Employee addEmployee(@PathVariable (value = "businessId") Long businessId,@RequestBody @Valid Employee employee){
        return employeesvc.addEmployee(businessId, employee);
    }

    @PutMapping("/users/{userid}/businesses/{businessId}/employees/{employeeId}")
    public Employee updateEmployee(@PathVariable (value = "businessId") Long businessId ,
            @PathVariable (value = "employeeId") Long employeeId, @RequestBody Employee newEmployee) {
        return employeesvc.updateEmployee(businessId,employeeId ,newEmployee);
    }

    @DeleteMapping("/users/{userid}/businesses/{businessId}/employees/{employeeId}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long businessId,@PathVariable Long employeeId ){
        return employeesvc.deleteEmployee(businessId, employeeId);
    }


}
