package net.codejava.CodeJavaApp.Business;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class BusinessExistsException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public BusinessExistsException(String businessName) {
        super("This business is already registered: " + businessName);
    }
    
}
