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

    }
}
