package net.codejava.CodeJavaApp.user;

import java.util.*;

import javax.validation.Valid;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.dao.EmptyResultDataAccessException;

@CrossOrigin
@RestController
public class UserController {
    private UserService users; 
    private BCryptPasswordEncoder encoder;
    
    public UserController(UserService users, BCryptPasswordEncoder encoder){
        this.users = users;
        this.encoder = encoder;
    }
    
    @GetMapping("/users")
    public List<User> getUsers() {
        return users.getUsers();
    }
    
    /**
     * Using BCrypt encoder to encrypt the password for storage 
     * @param user
     * @return
     */
    @CrossOrigin
    @PostMapping("/users")
    public User addUser(@Valid @RequestBody User user){
        User user2 = users.addUser(user);
        if(user2 == null){
            throw new UsernameExistsException(user.getUsername());
        }else{
            return user2;
        }
    }
        
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/users/search")
    public User getUser(@Valid @RequestBody User user){
        User user2 = users.getUser(user);
        if(user2 == null){
            throw new UserNotFoundException(user.getUsername());
        }else{
            return user2;
        }
    }


    @PutMapping("/users/{userId}")
    public User updateUser(
                                 @PathVariable (value = "userId") Long userId,
                                 @Valid @RequestBody User newUser) {
        return users.updateUser(userId, newUser);
       
    }

    @PutMapping("/users/{userId}/changePassword")
    public User updateUserPassword(
                                 @PathVariable (value = "userId") Long userId,
                                 @Valid @RequestBody User newUser) {
        return users.updateUserPassword(userId, newUser);
       
    }


    /**
     * if there's no user to be deleted the exception will be caught 
     * and throws a UserNotFoundException
     * @param id
     */
    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id){
        try{
            users.deleteUser(id);
         }catch(EmptyResultDataAccessException e) {
            throw new UserNotFoundException(id);
         }
    }


}
