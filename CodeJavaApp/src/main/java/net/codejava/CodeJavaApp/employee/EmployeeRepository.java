package net.codejava.CodeJavaApp.employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.codejava.CodeJavaApp.user.*;

import java.util.*;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByName(String employeeName);
    List<Employee> findByUserId(Long userId);
    Optional<Employee> findByIdAndUserId(Long id, Long userId);
}
