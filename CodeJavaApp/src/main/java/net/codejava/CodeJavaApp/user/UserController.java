package net.codejava.CodeJavaApp.user;

import java.util.*;

import javax.validation.Valid;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.dao.EmptyResultDataAccessException;

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

    // @GetMapping("/users/{id}")
    // public User getUser(@PathVariable Long id, @RequestBody String username){
    //     return users.findById(id).orElseThrow(()-> new UserNotFoundException(id));

    // }

    @GetMapping("/users/search")
    public User getUser(@Valid @RequestBody User user){
        return users.findByUsername(user.getUsername()).orElseThrow(()-> new UserNotFoundException(1L));

    }


    @PutMapping("/users/{userId}")
    public User updateUser(
                                 @PathVariable (value = "userId") Long userId,
                                 @Valid @RequestBody User newUser) {
        return users.findById(userId).map(user-> {
            user.setPassword(newUser.getPassword());
            user.setFirstName(newUser.getFirstName());
            user.setLastName(newUser.getLastName());
            return users.save(user);
        }).orElseThrow(()-> new UserNotFoundException(userId));
       
    }

    @DeleteMapping("/users/{id}")
    public void deleteRestriction(@PathVariable Long id){
        try{
            users.deleteById(id);
         }catch(EmptyResultDataAccessException e) {
            throw new UserNotFoundException(id);
         }
    }


}
