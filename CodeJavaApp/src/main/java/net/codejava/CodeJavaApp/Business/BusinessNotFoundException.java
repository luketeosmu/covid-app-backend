package net.codejava.CodeJavaApp.Business;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.NOT_FOUND)
public class BusinessNotFoundException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public BusinessNotFoundException(Long id) {
        super("Could not find business " + id);
    }

}
