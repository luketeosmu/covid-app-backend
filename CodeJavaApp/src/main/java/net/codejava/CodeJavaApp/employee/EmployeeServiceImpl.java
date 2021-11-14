package net.codejava.CodeJavaApp.employee;
import java.util.*;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import net.codejava.CodeJavaApp.user.*;

import org.springframework.http.ResponseEntity;

@Service
public class EmployeeServiceImpl {
    @Autowired
    private UserRepository users;

    @Autowired
    private EmployeeRepository employees;

    public List<Employee> getAllEmployee(Long userId){
        return employees.findByUserId(userId);
    }

    public Employee getEmployeeByEmployeeIdAndUserId(Long employeeId, Long userId){
        return employees.findByIdAndUserId(employeeId, userId).map(employee2 ->{
            return employee2;
        }).orElse(null);
    }

    public List<Employee> getExpiredFetEmployees(long userid){
        return employees.listExpiredTestEmployees(userid); 
    }
    
    // public List<Employee> listTodayTests(long userid){
    //     return employees.listTodayTests(userid); 
    // }

    
    public Employee addEmployee(Long userId, Employee employee){
        return users.findById(userId).map(user2-> {
            //if user exist then set user and check for duplicated user name 
            Employee check = employees.findById(employee.getId()).map(employee2 ->{
                return employee2;
            }).orElse(null);
            
            if(check != null){
                return null;  
            }else{
                employee.setUser(user2);
                return employees.save(employee);
            }
        }).orElse(null);


    }


    public Employee updateEmployee(Long userId, Long employeeId, Employee newEmployee){
        return employees.findByIdAndUserId(employeeId, userId).map(employee2 ->{
            setDetails(newEmployee, employee2);
            return employees.save(employee2);
        }).orElse(null); 

    }

    private void setDetails(Employee newEmployee, Employee employee2) {
        employee2.setName(newEmployee.getName());
        employee2.setVaxStatus(newEmployee.isVaxStatus());
        employee2.setFetDate(newEmployee.getFetDate());
    }

    public ResponseEntity<?> deleteEmployee(Long userId, Long employeeId){
        
        return employees.findByIdAndUserId(employeeId,userId).map(employee2 ->{
            employees.delete(employee2);
            return ResponseEntity.ok().build();
        }).orElseThrow(()-> new EmployeeNotFoundException(employeeId));
    }

}
