package pw.react.tuesday_booklybackend.security.configs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.filter.OncePerRequestFilter;
import pw.react.tuesday_booklybackend.dao.UserRepository;
import pw.react.tuesday_booklybackend.security.filters.JwtAuthenticationEntryPoint;
import pw.react.tuesday_booklybackend.security.filters.JwtRequestFilter;
import pw.react.tuesday_booklybackend.security.services.JwtTokenService;
import pw.react.tuesday_booklybackend.security.services.JwtUserDetailsService;

import javax.annotation.PostConstruct;

@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {

    private static final Logger log = LoggerFactory.getLogger(JwtConfig.class);

    private String secret;
    private long expirationMs;

    @PostConstruct
    private void init() {
        log.debug("************** JWT properties **************");
        log.debug("JWT secret: {}", secret);
        log.debug("JWT expirationMs: {}", expirationMs);
    }


    @Bean
    public JwtUserDetailsService jwtUserDetailsService(UserRepository userRepository) {
        return new JwtUserDetailsService(userRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtTokenService jwtTokenService() {
        return new JwtTokenService(secret, expirationMs);
    }

    @Bean
    public OncePerRequestFilter jwtRequestFilter(UserRepository userRepository) {
        return new JwtRequestFilter(jwtUserDetailsService(userRepository), jwtTokenService());
    }

    @Bean
    public AuthenticationEntryPoint jwtAuthenticationEntryPoint() {
        return new JwtAuthenticationEntryPoint();
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public long getExpirationMs() {
        return expirationMs;
    }

    public void setExpirationMs(long expirationMs) {
        this.expirationMs = expirationMs;
    }
}

