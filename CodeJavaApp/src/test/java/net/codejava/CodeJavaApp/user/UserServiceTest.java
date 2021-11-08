package net.codejava.CodeJavaApp.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import net.codejava.CodeJavaApp.restrictions.*;

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
    private UserService usersvc; 

    @Test
    public void addUser_SameUsername_ReturnNull(){
        // arrange ***
        User user = new User("email@gmail.com","Strongpassword","john","Luke","ROLE_USER");

        // mock the "save" operation
        when(users.save(any(User.class))).thenReturn(user);
        when(users.findByUsername(any(String.class))).thenReturn(Optional.of(user)); 

        // act ***
        users.save(user);
        User savedUser= usersvc.addUser(user);

        // assert ***
        assertNull(savedUser);
        verify(users).save(user);
    }
}
