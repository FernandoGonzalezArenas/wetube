package com.wetube.admin.config;

import com.wetube.admin.security.GatewayHeaderFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
    return http
            .csrf(csrf -> csrf.disable())
            .addFilterBefore(new GatewayHeaderFilter(), UsernamePasswordAuthenticationFilter.class)
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/admin/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/webjars/**").permitAll()
                    .requestMatchers(HttpMethod.POST, "/admin/reports/**").authenticated()
                    .requestMatchers("/admin/**").hasRole("ADMIN")
                    .requestMatchers("/actuator/**").permitAll()
                    .anyRequest().authenticated())
            .sessionManagement(sesion -> sesion.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).build();
}

@Bean
    public UserDetailsService userDetailsService(){
    return new InMemoryUserDetailsManager();
}

}
