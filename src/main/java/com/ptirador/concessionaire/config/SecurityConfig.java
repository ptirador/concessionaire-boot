package com.ptirador.concessionaire.config;

import com.ptirador.concessionaire.repository.UserRepository;
import com.ptirador.concessionaire.security.handler.MyAuthenticationFailureHandler;
import com.ptirador.concessionaire.security.service.MyUserDetailsService;
import com.ptirador.concessionaire.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserRepository userRepository;

    public SecurityConfig(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public UserDetailsService userDetailsServiceBean() {
        return new MyUserDetailsService(userRepository);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
       auth
                .eraseCredentials(false)
                .userDetailsService(userDetailsServiceBean())
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/css/**", "/img/**", "/js/**", "/login", "/signup").permitAll()
                    .antMatchers("/administration/**").hasAuthority(Constants.ROLE_ADMIN)
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage("/login")
                    .defaultSuccessUrl("/home")
                    .failureHandler(myFailureHandler())
                    .and()
                .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                    .and()
                .exceptionHandling()
                    .accessDeniedPage("/accessDenied")
                    .and()
                .rememberMe()
                    .tokenValiditySeconds(2592000)
                .userDetailsService(userDetailsServiceBean());
    }

    @Bean
    public AuthenticationFailureHandler myFailureHandler() {
        return new MyAuthenticationFailureHandler();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
