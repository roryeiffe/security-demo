package com.revature.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


// this annotation tells Spring that this is a security configuration class
@EnableWebSecurity
// tells Spring that this class has bean definition methods:
@Configuration
public class SecurityConfig{
    // Encoder lets us encrpyt our passwords
    @Autowired
    EncoderConfig encoderConfig;

    // JWT Stuff:
    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private UserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private AuthenticationConfiguration authConfiguration;



    // Spring will look for a bean of type SecurityFilterChain
    // create our own been Security Filter Chain, therefore overriding the default security filter chain
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
//        http
//            .authorizeHttpRequests()
//            // we want any request to be authenticated
//            .anyRequest()
//            .authenticated();
//        // basic authentication is not recommended due to vulnurabilities
    // http.formLogin();
//        http.httpBasic();
//        return http.build();
//    }

    // This bean is responsible for configuring the security of our application:
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.csrf().disable()
                // allow anyone to access the authenticate request, how else would we be able to authenticate ourselves?
                .authorizeHttpRequests().requestMatchers("/authenticate").permitAll()
                // all other requests need to be authenticated
                .anyRequest().authenticated().and()
                // configure the exception handling using the jwt entry point class
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                // make sure we use stateless session; session won't be used to
                // store user's state.
                .and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Add a filter to validate the tokens with every request
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }




    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authConfiguration.getAuthenticationManager();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        // configure our authentication manager to use BCyrpt, which is a password encoder:
        auth.userDetailsService(jwtUserDetailsService).passwordEncoder(encoderConfig.passwordEncoder());
    }


}
