package pw.react.tuesday_booklybackend.security.configs;

import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;
import pw.react.tuesday_booklybackend.security.services.JwtUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Profile({"jwt"})
public class WebJwtSecurityConfig {

    private final JwtUserDetailsService jwtUserDetailsService;
    private final AuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final OncePerRequestFilter jwtRequestFilter;

    public WebJwtSecurityConfig(JwtUserDetailsService jwtUserDetailsService,
                                AuthenticationEntryPoint jwtAuthenticationEntryPoint,
                                OncePerRequestFilter jwtRequestFilter
    ) {
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        // configure AuthenticationManager so that it knows from where to load user for matching credentials
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
         httpSecurity
                //.cors().disable()
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and()
                // make sure we use stateless session; session won't be used to store user's state.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                // dont authenticate this particular request
                 .authorizeRequests()
                 .requestMatchers("/authenticate").permitAll()
                 .requestMatchers(HttpMethod.POST, "/users").permitAll()
                 .requestMatchers(HttpMethod.GET, "/actuator/**").permitAll()
                 .requestMatchers( "/v3/api-docs/**", "/swagger-ui/**").permitAll()
                 // all other requests need to be authenticated
                 .anyRequest().authenticated();

        // Add a filter to validate the tokens with every request
        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}
