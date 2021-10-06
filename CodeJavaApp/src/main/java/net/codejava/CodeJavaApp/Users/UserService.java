package net.codejava.CodeJavaApp.Users;

import net.codejava.CodeJavaApp.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository repo;
}
