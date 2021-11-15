package net.codejava.CodeJavaApp.employee;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class EmployeeExistsException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    
    public EmployeeExistsException(Long id) {
        super("This employee is already registered: id = " + id);
    }
    
}
