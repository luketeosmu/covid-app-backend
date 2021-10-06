package net.codejava.CodeJavaApp.user;

import java.util.*;

import javax.validation.Valid;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    private UserRepository users;
    private BCryptPasswordEncoder encoder;

    public UserController(UserRepository users, BCryptPasswordEncoder encoder){
        this.users = users;
        this.encoder = encoder;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return users.findAll();
    }

    /**
    * Using BCrypt encoder to encrypt the password for storage 
    * @param user
     * @return
     */
    @PostMapping("/users")
    public User addUser(@Valid @RequestBody User user){
        // your code here
        User check = users.findByUsername(user.getUsername()).map(user2 -> {
            return user2;
        }).orElse(null);
        if(check != null){
            throw new UsernameExistsException(user.getUsername()); 
        }else{
            user.setPassword(encoder.encode(user.getPassword()));
            return users.save(user);
        }
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable Long id){
        // User user = users.findbyId(id);

        // // Need to handle "User not found" error using proper HTTP status code
        // // In this case it should be HTTP 404
        // if(user == null) throw new UserNotFoundException(id);
        // return user;

        return users.findById(id).orElse(null);

    }

   
}
