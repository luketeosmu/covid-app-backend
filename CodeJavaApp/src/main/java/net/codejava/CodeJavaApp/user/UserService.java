package net.codejava.CodeJavaApp.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.*;

@Service
public class UserService {
    @Autowired
    private UserRepository users;
    @Autowired
    private BCryptPasswordEncoder encoder;

    /**
     * Get a list of all users (ADMIN rights)
     * 
     * @return a List<User> of users 
     */
    public List<User> getUsers() {
        return users.findAll();
    }

    /**
     * Takes in a user and check if the username already exists
     * if exist return null else save the user with encoded password and return
     * 
     * @param user
     * @return the added user / null 
     */
    public User addUser(User user) {
        User sameuser = users.findByUsername(user.getUsername()).orElse(null);
        if (sameuser == null) {
            user.setPassword(encoder.encode(user.getPassword()));
            return users.save(user);
        } else {
            return null;
        }
    }
    
    /**
     * This is a special post method for axios to call for the user 
     * The body will consist of the username and password 
     * check if username and password matches an existing user then return 
     * @param user
     * @return user found / null 
     */
    public User getUser(User user) {
        return users.findByUsername(user.getUsername()).map(user2-> {
            if (encoder.matches(user.getPassword(), user2.getPassword())){
                return user2;
            }
            return null;
        }).orElse(null);
    }
    /**
     * if user is found then updates password firstname and lastname 
     * then save and return the user -> if user not found then throw exception 
     * @param userId
     * @param newUser
     * @return User/ null / thorw usernotfoundexception 
     */
    public User updateUser(Long userId, User newUser) {
        return users.findById(userId).map(user -> {
            if(!newUser.getUsername().equals("dummy@gmail.com")){
                User findUser = users.findByUsername(newUser.getUsername()).orElse(null);
                if(findUser != null){
                    throw new UsernameExistsException(newUser.getUsername());
                }
                user.setUsername(newUser.getUsername());
            }
            else{

                user.setPassword(encoder.encode(newUser.getPassword()));
                user.setFirstName(newUser.getFirstName());
                user.setLastName(newUser.getLastName());
            }
            return users.save(user);
        }).orElseThrow(() -> new UserNotFoundException(userId));
    }
    

    /**
     * deleteUser with id 
     * @param userId
     */
    public void deleteUser(Long userId) {
        users.deleteById(userId);
    }
}
