package net.codejava.CodeJavaApp.user;

import java.util.*;

import javax.validation.Valid;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.dao.EmptyResultDataAccessException;


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
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/users")
    public User addUser(@Valid @RequestBody User user){
        User user2 = users.addUser(user);
        if(user2 == null){
            throw new UsernameExistsException(user.getUsername());
        }else{
            return user2;
        }
    }
    
    // @GetMapping("/users/{id}")
    // public User getUser(@PathVariable Long id, @RequestBody String username){
        //     return users.findById(id).orElseThrow(()-> new UserNotFoundException(id));
        
        // }
        
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



    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id){
        try{
            users.deleteUser(id);
         }catch(EmptyResultDataAccessException e) {
            throw new UserNotFoundException(id);
         }
    }


}
