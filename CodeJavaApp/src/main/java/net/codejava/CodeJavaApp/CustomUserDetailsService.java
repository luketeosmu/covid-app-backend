package net.codejava.CodeJavaApp;

import net.codejava.CodeJavaApp.CustomUserDetails;
import net.codejava.CodeJavaApp.User;
import net.codejava.CodeJavaApp.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new CustomUserDetails(user);
    }

//    public void createNewCustomerAfterOAuthLoginSuccess(String email, String name, AuthenticationProvider provider) {
//        User user = new User();
//        user.setEmail(email);
//        user.setFirstName(name);
//        user.setAuthProvider(provider);
//
//        userRepo.save(user);
//    }
}