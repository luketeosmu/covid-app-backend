package net.codejava.CodeJavaApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import net.codejava.CodeJavaApp.restrictions.RestrictionsRepository;
import net.codejava.CodeJavaApp.user.User;
import net.codejava.CodeJavaApp.user.UserRepository;
import net.codejava.CodeJavaApp.Business.Business;
import net.codejava.CodeJavaApp.Business.BusinessRepository;
import net.codejava.CodeJavaApp.restrictions.*;

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
		RestrictionsRepository restrictions = ctx.getBean(RestrictionsRepository.class);
		BusinessRepository businesses = ctx.getBean(BusinessRepository.class);
        System.out.println("[Add user]: " + users.save(
            new User("hihi@gmail.com", encoder.encode("Tester123"), "john","luke","ROLE_ADMIN")).getUsername());
        System.out.println("[Add user]: " + users.save(
            new User("hihi4@gmail.com", encoder.encode("Tester123"), "john","luke","ROLE_ADMIN")).getPassword());
        System.out.println("[Add RESTRICTIONS]: " + restrictions.save(new Restrictions("ind", "nim","woi")));

    }
}
