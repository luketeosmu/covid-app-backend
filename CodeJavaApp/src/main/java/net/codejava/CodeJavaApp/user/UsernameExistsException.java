package net.codejava.CodeJavaApp.user;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UsernameExistsException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public UsernameExistsException(String username) {
        super("This username is in use: " + username);
    }
    
}