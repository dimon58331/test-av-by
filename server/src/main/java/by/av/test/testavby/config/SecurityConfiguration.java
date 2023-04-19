package by.av.test.testavby.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true, securedEnabled = true, proxyTargetClass = true)
public class SecurityConfiguration {
//    private final JWTFilter jwtFilter;
//    private final PersonDetailsService personDetailsService;
//    private final JWTAuthenticationEntryPoint authenticationEntryPoint;
//
//    @Autowired
//    public SecurityConfig(JWTFilter jwtFilter, PersonDetailsService personDetailsService, JWTAuthenticationEntryPoint authenticationEntryPoint) {
//        this.jwtFilter = jwtFilter;
//        this.personDetailsService = personDetailsService;
//        this.authenticationEntryPoint = authenticationEntryPoint;
//    }
    @Bean
    public SecurityFilterChain httpSecurity(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(null)
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/api/auth/**", "/error").permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(null, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
//    @Bean
//    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {
//        AuthenticationManagerBuilder managerBuilder =
//                httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
//        managerBuilder.userDetailsService(personDetailsService).passwordEncoder(passwordEncoder());
//
//        return managerBuilder.build();
//    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
