package com.example.demo.service.impl;

import com.example.demo.domain.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.WebSecurityEnablerConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.JsonbHttpMessageConverter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Configuration
@EnableWebMvcSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserServiceImpl userService;

    public static User currenUser;

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    };

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().and().cors().and().authorizeRequests()
                .antMatchers("/*").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                    .loginProcessingUrl("/login")
                    .permitAll()
                    .passwordParameter("password")
                    .usernameParameter("username")
                    .successHandler(new AuthSuccessHandler())
                .and()
                .logout().permitAll().logoutSuccessUrl("/login")
                .and()
                .csrf().disable();
    }

    class AuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler{
        @Override
        public void onAuthenticationSuccess(HttpServletRequest request,
                                            HttpServletResponse response, Authentication authentication) throws IOException {
            User user = userService.getByName(authentication.getName()).get();
            currenUser = user;
            ObjectMapper mapper = new ObjectMapper();
            //response.setHeader("user", mapper.writeValueAsString(user));
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.write(mapper.writeValueAsString(user));
            out.flush();
        }
    }
}
