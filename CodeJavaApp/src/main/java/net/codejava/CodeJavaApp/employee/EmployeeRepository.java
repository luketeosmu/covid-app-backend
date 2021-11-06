package net.codejava.CodeJavaApp.employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.codejava.CodeJavaApp.Business.*;

import java.util.*;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByName(String employeeName);
    List<Employee> findByBusinessBusinessId(Long businessId);
    Optional<Employee> findByIdAndBusinessBusinessId(Long id, Long businessId);
}
