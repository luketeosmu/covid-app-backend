package net.codejava.CodeJavaApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// import net.codejava.CodeJavaApp.client.RestTemplateClient;
import net.codejava.CodeJavaApp.user.User;
import net.codejava.CodeJavaApp.user.UserRepository;
@SpringBootApplication
public class CodeJavaAppApplication {

	// public static void main(String[] args) {
	// 	SpringApplication.run(CodeJavaAppApplication.class, args);
	// }


	public static void main(String[] args) {
		
		ApplicationContext ctx = SpringApplication.run(CodeJavaAppApplication.class, args);
		BCryptPasswordEncoder encoder = ctx.getBean(BCryptPasswordEncoder.class);
        // JPA user repository init
        UserRepository users = ctx.getBean(UserRepository.class);
        System.out.println("[Add user]: " + users.save(
            new User(1L,"hihi@gmail.com", encoder.encode("Tester123"), "john","luke","ROLE_ADMIN")).getUsername());
        
    }
}
//this.id = id;
// this.email = email;
// this.password = password;
// this.firstName = firstName;
// this.lastName = lastName;
// this.authorities = authorities;
