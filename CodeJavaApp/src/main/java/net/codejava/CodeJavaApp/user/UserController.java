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

    public UserController(UserService users, BCryptPasswordEncoder encoder) {
        this.users = users;
        this.encoder = encoder;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return users.getUsers();
    }

    /**
     * @param user
     * @return user or throw UsernameExistException 
     */
    @CrossOrigin
    @PostMapping("/users")
    public User addUser(@Valid @RequestBody User user) {
        User user2 = users.addUser(user);
        if (user2 == null) {
            throw new UsernameExistsException(user.getUsername());
        } else {
            return user2;
        }
    }

    /**
     * Login Method for axios to call for frontend login
     * @param user
     * @return User or UserNotFoundException 404
     */
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/users/search")
    public User getUser(@Valid @RequestBody User user) {
        User user2 = users.getUser(user);
        if (user2 == null) {
            throw new UserNotFoundException(user.getUsername());
        } else {
            return user2;
        }
    }

    /**
     * Update user details 
     * @param userId
     * @param newUser
     * @return User or throw UserNotFound/UsernameExist 
     */
    @PutMapping("/users/{userId}")
    public User updateUser(@PathVariable(value = "userId") Long userId, @Valid @RequestBody User newUser) {
        return users.updateUser(userId, newUser);

    }

    /**
     * special method to make sure auth is implemented when changing password
     * @param userId
     * @param newUser
     * @return
     */
    @PutMapping("/users/{userId}/changePassword")
    public User updateUserPassword(@PathVariable(value = "userId") Long userId, @Valid @RequestBody User newUser) {
        User user = users.updateUserPassword(userId, newUser);
        if(user == null){
            throw new UserNotFoundException(userId);
        }
        return user;

    }

    /**
     * if there's no user to be deleted the exception will be caught and throws a
     * UserNotFoundException
     * 
     * @param id
     */
    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id) {
        try {
            users.deleteUser(id);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException(id);
        }
    }

}
