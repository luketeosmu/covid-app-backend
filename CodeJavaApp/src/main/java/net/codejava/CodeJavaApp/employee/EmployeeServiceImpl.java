package net.codejava.CodeJavaApp.employee;
import java.util.*;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import net.codejava.CodeJavaApp.Business.*;

import org.springframework.http.ResponseEntity;

@Service
public class EmployeeServiceImpl {
    @Autowired
    private BusinessRepository businesses;

    @Autowired
    private EmployeeRepository employees;

    public List<Employee> getAllEmployee(Long businessId){
        return employees.findByBusinessBusinessId(businessId);
    }

    public Employee getEmployeeByEmployeeIdAndBusinessId(Long employeeId, Long  businessId){
        return employees.findByIdAndBusinessBusinessId(employeeId, businessId).map(employee2 ->{
            return employee2;
        }).orElseThrow(()-> new EmployeeNotFoundException(employeeId));
    }

    
    public Employee addEmployee(Long businessId, Employee employee){
        return businesses.findById(businessId).map(business2-> {
            //if user exist then set user and check for duplicated business name 
            employee.setBusiness(business2);
            Employee check = employees.findById(employee.getId()).map(employee2 ->{
                return employee2;
            }).orElse(null);
            if(check != null){
                throw new EmployeeExistsException(employee.getName()); 
            }else{
                return employees.save(employee);
            }
        }).orElseThrow(()-> new BusinessNotFoundException(businessId));


    }


    public Employee updateEmployee(Long businessId, Long employeeId, Employee newEmployee){
        if(!businesses.existsById(businessId)){
            throw new BusinessNotFoundException(businessId);
        }
        return employees.findByIdAndBusinessBusinessId(employeeId, businessId).map(employee2 ->{
            employee2.setName(newEmployee.getName());
            employee2.setVaxStatus(newEmployee.isVaxStatus());
            employee2.setFetDate(newEmployee.getFetDate());
            return employees.save(employee2);
        }).orElseThrow(()->new EmployeeNotFoundException(employeeId));

    }

    public ResponseEntity<?> deleteEmployee(Long businessId, Long employeeId){
        if(!businesses.existsById(businessId)){
            throw new BusinessNotFoundException(businessId);
        }
        return employees.findByIdAndBusinessBusinessId(employeeId,businessId).map(employee2 ->{
            employees.delete(employee2);
            return ResponseEntity.ok().build();
        }).orElseThrow(()-> new EmployeeNotFoundException(employeeId));
    }

}
