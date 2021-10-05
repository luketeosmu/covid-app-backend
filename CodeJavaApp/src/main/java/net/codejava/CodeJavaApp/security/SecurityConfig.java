package net.codejava.CodeJavaApp.security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    private UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userSvc){
        this.userDetailsService = userSvc;
    }
    
    /** 
     * Attach the user details and password encoder.
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth)
        throws Exception {
        auth
        .userDetailsService(userDetailsService)
        .passwordEncoder(encoder());
    }

    /**
     * User role: can add review.
     * Admin role: can add/delete/update books/reviews, and add users
     * Anyone can view book/review
     * 
     * Note: '*' matches zero or more characters, e.g., /books/* matches /books/20
             '**' matches zero or more 'directories' in a path, e.g., /books/** matches /books/1/reviews 
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        .httpBasic()
            .and() //  "and()"" method allows us to continue configuring the parent
        .authorizeRequests()
            .antMatchers(HttpMethod.POST, "/businesses").permitAll()
            .antMatchers(HttpMethod.PUT, "/businesses/*").permitAll()
            .antMatchers(HttpMethod.DELETE, "/businesses/*").permitAll()
            // your code here
            .antMatchers(HttpMethod.POST, "/restrictions").permitAll()
            .antMatchers(HttpMethod.PUT, "/restrictions/*").permitAll()
            .antMatchers(HttpMethod.DELETE, "/restrictions/*").permitAll()
            .antMatchers(HttpMethod.GET, "/users").permitAll()
            .antMatchers(HttpMethod.POST, "/users").permitAll()
            // .antMatchers(HttpMethod.POST, "/businesses").hasRole("ADMIN")
            // .antMatchers(HttpMethod.PUT, "/businesses/*").hasRole("ADMIN")
            // .antMatchers(HttpMethod.DELETE, "/businesses/*").hasRole("ADMIN")
            // // your code here
            // .antMatchers(HttpMethod.POST, "/restrictions").hasAnyRole("ADMIN", "USER")
            // .antMatchers(HttpMethod.PUT, "/restrictions/*").hasRole("ADMIN")
            // .antMatchers(HttpMethod.DELETE, "/restrictions/*").hasRole("ADMIN")
            // .antMatchers(HttpMethod.GET, "/users").hasRole("ADMIN")
            // .antMatchers(HttpMethod.POST, "/users").hasRole("ADMIN")
            .and()
        .csrf().disable() // CSRF protection is needed only for browser based attacks
        .formLogin().disable()
        .headers().disable()
        ;
    }

    /**
     * @Bean annotation is used to declare a PasswordEncoder bean in the Spring application context. 
     * Any calls to encoder() will then be intercepted to return the bean instance.
     */
    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}