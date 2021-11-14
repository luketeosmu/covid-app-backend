package net.codejava.CodeJavaApp.employee;

import net.codejava.CodeJavaApp.employee.*;
import net.codejava.CodeJavaApp.user.User;
import net.codejava.CodeJavaApp.user.UserRepository;
import net.codejava.CodeJavaApp.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmployeeUnitTest {
    @Mock
    private EmployeeRepository employees;

    @Mock
    private UserRepository users;

    @InjectMocks
    private EmployeeServiceImpl employeesvc;

    @InjectMocks
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Test
    public void addEmployee_NewEmployee_ReturnEmployee() throws Exception {
        // Arrange
        User user = new User(1L, "test@gmail.com", "Test1234", "tes", "tes");
        Employee employee = new Employee(1L, "Ename", true);

        // Mock the operation
        when(users.findById(user.getId())).thenReturn(Optional.of(user));
        when(employees.findById(any(Long.class))).thenReturn(Optional.empty());
        when(employees.save(any(Employee.class))).thenReturn(employee);

        // Act
        Employee savedEmployee = employeesvc.addEmployee(user.getId(), employee);

        // Assert
        assertEquals(savedEmployee.getName(), employee.getName());
        assertNotNull(savedEmployee);

        // verify
        verify(users).findById(user.getId());
        verify(employees).save(employee);
        verify(employees).findById(employee.getId());
    }

    @Test
    public void addEmployee_SameEmployeeId_ReturnNull() {
        // Arrange
        User user = new User(1L, "test@gmail.com", "Test1234", "tes", "tes");
        Employee employee = new Employee(1L, "Ename", true);

        // Mock the operation
        when(users.findById(user.getId())).thenReturn(Optional.of(user));
        when(employees.findById(any(Long.class))).thenReturn(Optional.of(employee));

        // Act
        Employee savedEmployee = employeesvc.addEmployee(user.getId(), employee);

        // Assert
        // assertEquals(savedEmployee.getName(), employee.getName());
        assertNull(savedEmployee);

        // verify
        verify(users).findById(user.getId());
        verify(employees).findById(employee.getId());
    }

    @Test
    public void getEmployee_InvalidEmployeeId_ReturnNull() {
        User user = new User(1L, "test@gmail.com", "Test1234", "tes", "tes");
        Employee employee = new Employee(1L, "Ename", true);
        when(employees.findByIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.empty());
        // act
        Employee result = employeesvc.getEmployeeByEmployeeIdAndUserId(employee.getId(), user.getId());
        // assert
        assertNull(result);
        verify(employees).findByIdAndUserId(employee.getId(), user.getId());
    }

    @Test
    public void getEmployee_ValidEmployeeId_ReturnEmployee() {
        // arrange
        User user = new User(1L, "test@gmail.com", "Test1234", "tes", "tes");
        Employee employee = new Employee(1L, "Ename", true);
        when(employees.findByIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.of(employee));
        // act
        Employee result = employeesvc.getEmployeeByEmployeeIdAndUserId(employee.getId(), user.getId());
        // assert
        assertNotNull(result);
        verify(employees).findByIdAndUserId(employee.getId(), user.getId());
    }

    @Test
    public void getAllEmployee_ValidEmployeeId_ReturnList() {
        // arrange
        User user = new User(1L, "test@gmail.com", "Test1234", "tes", "tes");
        Employee employee = new Employee(1L, "Ename", true);
        ArrayList<Employee> list = new ArrayList<>();
        list.add(employee);
        // act
        when(employees.findByUserId(anyLong())).thenReturn(list);
        List<Employee> returnEmployees = employeesvc.getAllEmployee(user.getId());
        // assert
        assert (returnEmployees.size() == 1);
        verify(employees).findByUserId(user.getId());
    }

    @Test
    public void getAllEmployee_InvalidEmployeeId_ReturnNull() {
        // arrange
        User user = new User(1L, "test@gmail.com", "Test1234", "tes", "tes");
        Employee employee = new Employee(1L, "Ename", true);
        ArrayList<Employee> list = new ArrayList<>();
        list.add(employee);
        // act
        when(employees.findByUserId(anyLong())).thenReturn(null);
        List<Employee> returnEmployees = employeesvc.getAllEmployee(user.getId());
        // assert
        assertNull(returnEmployees);
        verify(employees).findByUserId(user.getId());
    }

    @Test
    public void updateEmployee_EmployeeIdValid_ReturnUpdatedEmployeee() {
        // Arrange
        User user = new User(1L, "test@gmail.com", "Test1234", "tes", "tes");
        Employee employee = new Employee(1L, "Ename", true);
        Employee newEmployee = new Employee(1L, "newName", false, new Date(), user);
        when(employees.findByIdAndUserId(user.getId(), employee.getId())).thenReturn(Optional.of(employee));
        when(employees.save(employee)).thenReturn(employee);
        // Act
        Employee savedEmployee = employeesvc.updateEmployee(user.getId(), employee.getId(), newEmployee);
        // Assert
        assertNotNull(savedEmployee);
        assertEquals(savedEmployee.getName(), newEmployee.getName());
        assertEquals(savedEmployee.isVaxStatus(), newEmployee.isVaxStatus());
        assertEquals(savedEmployee.getFetDate(), newEmployee.getFetDate());
        verify(employees).findByIdAndUserId(employee.getId(), user.getId());
        verify(employees).save(employee);

    }

    // // //
    @Test
    public void updateEmployee_EmployeeInvalid_ReturnNull() {
        // Arrange
        User user = new User(1L, "test@gmail.com", "Test1234", "tes", "tes");
        Employee employee = new Employee(1L, "Ename", true);
        Employee newEmployee = new Employee(1L, "newName", false);
        when(employees.findByIdAndUserId(user.getId(), employee.getId())).thenReturn(Optional.empty());
        // Act
        Employee savedEmployee = employeesvc.updateEmployee(user.getId(), employee.getId(), newEmployee);
        // Assert
        assertNull(savedEmployee);
        verify(employees).findByIdAndUserId(employee.getId(), user.getId());

    }

    @Test
    public void getExpiredFetEmployees_ValidUserId_ReturnList() {
        // Arrange
        User user = new User(1L, "test@gmail.com", "Test1234", "tes", "tes");
        Employee employee = new Employee(1L, "Ename", true);
        ArrayList<Employee> list = new ArrayList<>();
        list.add(employee);
        when(employees.listExpiredTestEmployees(user.getId())).thenReturn(list);
        // Act
        List<Employee> retList = employeesvc.getExpiredFetEmployees(user.getId());

        // Assert
        assertNotNull(retList);
        assertEquals(1, retList.size());
        verify(employees).listExpiredTestEmployees(user.getId());

    }

    @Test
    public void getExpiredFetEmployees_InvalidUserId_ReturnNull() {
        // Arrange
        User user = new User(1L, "test@gmail.com", "Test1234", "tes", "tes");
        // Employee employee = new Employee(1L, "Ename", true);
        // ArrayList<Employee> list = new ArrayList<>();
        // list.add(employee);
        when(employees.listExpiredTestEmployees(user.getId())).thenReturn(null);
        // Act
        List<Employee> retList = employeesvc.getExpiredFetEmployees(user.getId());

        // Assert
        assertNull(retList);
        verify(employees).listExpiredTestEmployees(user.getId());

    }
}
