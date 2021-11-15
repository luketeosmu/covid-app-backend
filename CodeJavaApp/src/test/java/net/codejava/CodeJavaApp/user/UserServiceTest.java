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
    public void addUser_NewUsername_ReturnSavedUser() {
        // arrange ***
        User user = new User("test@gmail.com", "Test123", "tes", "tes", "ROLE_ADMIN");

        // mock the save operation
        when(users.save(any(User.class))).thenReturn(user);

        // act ***
        User savedUser = userService.addUser(user);

        // assert **
        assertNotNull(savedUser);

        // verify
        verify(users).save(user);
    }

    @Test
    public void getUsers_Success_Return() {
        // arrange ***
        User user = new User("test@gmail.com", "Test123", "tes", "tes", "ROLE_ADMIN");
        ArrayList<User> list = new ArrayList<>();
        list.add(user);
        // mock the save operation
        when(users.findAll()).thenReturn(list);

        // act ***
        List<User> savedUser = userService.getUsers();

        // assert **
        assertNotNull(savedUser);
        assertEquals(1, savedUser.size());
        // verify
        verify(users).findAll();
    }

    @Test
    public void addUser_SameUsername_ReturnNull() {
        // arrange ***
        User user = new User("email@gmail.com", "Strongpassword", "john", "Luke", "ROLE_USER");

        // mock
        when(users.findByUsername(any(String.class))).thenReturn(Optional.of(user));

        // act ***
        User savedUser = userService.addUser(user);

        // assert ***
        assertNull(savedUser);

        // verify
        verify(users).findByUsername(user.getUsername());
    }

    @Test
    void updateUserPassword_UserNotFound_ReturnNull() {
        User updatedUser = null;
        User user = new User("test@gmail.com", "Test123", "tes", "tes", "ROLE_ADMIN");
        // mock
        when(users.findById(anyLong())).thenReturn(Optional.empty());

        // act
        updatedUser = userService.updateUserPassword(10L, user);

        assertNull(updatedUser);

        // verify
        verify(users).findById(10L);
    }

    @Test
    void updateUserPasword_Success_ReturnUser() {
        // arrange
        User user = new User("test@gmail.com", "Test123", "tes", "tes", "ROLE_ADMIN");
        User updatedUser = new User("test@gmail.com", "Test123", "NewName", "tes", "ROLE_ADMIN");

        // mock
        when(users.save(any(User.class))).thenReturn(user);
        when(users.findById(user.getId())).thenReturn(Optional.of(user));

        // act
        User newUser = userService.updateUser(user.getId(), updatedUser);

        // assert
        assertNotNull(newUser);
        verify(users).save(user);
        verify(users).findById(user.getId());
    }

    @Test
    void getUser_UsernamePasswordValid_ReturnUser() {
        User user = new User("test@gmail.com", encoder.encode("Test123"), "tes", "tes", "ROLE_ADMIN");
        User reqUser = new User("test@gmail.com", "Test123", "tes", "tes", "ROLE_ADMIN");
        when(users.findByUsername(any(String.class))).thenReturn(Optional.of(user));
        when(encoder.matches(reqUser.getPassword(), user.getPassword())).thenReturn(true);

        User returnUser = userService.getUser(reqUser);

        assertNotNull(returnUser);
        verify(users).findByUsername(user.getUsername());
        verify(encoder).matches(reqUser.getPassword() , user.getPassword());
    }

    @Test
    void getUser_UsernamePasswordInvalid_ReturnNull(){
        User reqUser = new User("test@gmail.com", "Test123", "tes", "tes", "ROLE_ADMIN");
        User user = new User("test@gmail.com", encoder.encode("Test123"), "tes", "tes", "ROLE_ADMIN");
        when(users.findByUsername(any(String.class))).thenReturn(Optional.of(user));
        when(encoder.matches(reqUser.getPassword(), user.getPassword())).thenReturn(false);

        User returnUser = userService.getUser(reqUser);

        assertNull(returnUser);
        verify(users).findByUsername(user.getUsername());
        verify(encoder).matches(reqUser.getPassword() , user.getPassword());
    }

}
