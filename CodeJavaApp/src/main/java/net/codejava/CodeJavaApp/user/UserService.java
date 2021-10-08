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

    public List<User> getUsers() {
        return users.findAll();
    }

    public User addUser(User user) {
        User sameusers =  users.findByUsername(user.getUsername()).orElse(null);
        if(sameusers == null){
            user.setPassword(encoder.encode(user.getPassword()));
            return users.save(user);
        }
        else{
            return null;
        }
    }


    public User getUser(User user){
        return users.findByUsername(user.getUsername()).orElse(null);
    }

    public User updateUser(Long userId,User newUser) {
        return users.findById(userId).map(user-> {
            user.setPassword(newUser.getPassword());
            user.setFirstName(newUser.getFirstName());
            user.setLastName(newUser.getLastName());
            return users.save(user);
        }).orElseThrow(()-> new UserNotFoundException(userId));
       
    }

    public void deleteUser(Long userId){
        users.deleteById(userId);
    }
}
