package net.codejava.CodeJavaApp.restrictions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.NOT_FOUND)
public class RestrictionNotFoundException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public RestrictionNotFoundException(Long id) {
        super("Could not find restriction " + id);
    }
    
}
