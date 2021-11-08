package net.codejava.CodeJavaApp.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.times;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import net.codejava.CodeJavaApp.restrictions.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;



@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository users;

    @InjectMocks
    private UserService userService;

    @Mock
    private BCryptPasswordEncoder encoder;

    @Test
    public void addUser_NewUsername_ReturnSavedUser(){
        // arrange ***
        User user = new User("test@gmail.com", "Test123", "tes", "tes", "ROLE_ADMIN");

        //mock the save operation
        when(users.save(any(User.class))).thenReturn(user);

        //act ***
        User savedUser = userService.addUser(user);

        //assert **
        assertNotNull(savedUser);

        verify(users).save(user);
    }

    @Test
    public void addUser_SameUsername_ReturnNull(){
        // arrange ***
        User user = new User("email@gmail.com","Strongpassword","john","Luke","ROLE_USER");
        User newUser =  new User("email@gmail.com","newPassword","john","Luke","ROLE_USER");

        // mock the "save" operation
        when(users.save(any(User.class))).thenReturn(user, newUser);
        when(users.findByUsername(any(String.class))).thenReturn(Optional.of(user));

        // act ***
        users.save(user);
        User savedUser = userService.addUser(newUser);

        // assert ***
        assertNull(savedUser);
        verify(users).save(user);
        verify(users).findByUsername(user.getUsername());
    }

    // @Test
    // void updateUser_NotFound_ReturnUserNotFoundException(){
    //     User updatedUser = null;
    //     try {
    //         User user = new User("test@gmail.com", "Test123", "tes", "tes", "ROLE_ADMIN");
    //         Long userID = 10L;
    //         when(users.findById(userID)).thenReturn(Optional.of(user));
    //         updatedUser = userService.updateUser(userID, user);
    //     } catch(UserNotFoundException e) {
    //         assertNull(updatedUser);
    //         verify(users).findById(10L);
    //     }
    // }

    // @Test
    // void updateUser_Success() {
    //     //arrange
    //     User user = new User("test@gmail.com", "Test123", "tes", "tes", "ROLE_ADMIN");
    //     User updatedUser = new User("test@gmail.com", "Test123", "NewName", "tes", "ROLE_ADMIN");
    //     when(users.save(any(User.class))).thenReturn(user);
    //     when(users.findById(user.getId())).thenReturn(Optional.of(user));

    //     //act
    //     userService.addUser(user);
    //     User newUser = userService.updateUser(user.getId(), updatedUser);

    //     //assert
    //     assertNotNull(newUser);
    //     verify(users, times(2)).save(user);
    //     verify(users).findById(user.getId());
    // }

    @Test
    void deleteUser_Success() {
        User user = new User("test@gmail.com", "Test123", "tes", "tes", "ROLE_ADMIN");

        //arrange
        when(users.save(any(User.class))).thenReturn(user);

        //act
        User savedUser = userService.addUser(user);
        users.delete(savedUser);

        //verify
        verify(users).delete(user);
    }

    @Test
    void deleteUser_NotFound_ReturnUserNotFoundException() {
        User user = new User("test@gmail.com", "Test123", "tes", "tes", "ROLE_ADMIN");
        try{
            users.delete(user);
        } catch(UserNotFoundException e) {
            verify(users).delete(user);
        }
    }
}
