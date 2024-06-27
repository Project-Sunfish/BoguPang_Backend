package com.kill.gaebokchi.global.config;

import com.kill.gaebokchi.domain.account.jwt.JWTFilter;
import com.kill.gaebokchi.domain.account.jwt.JWTUtil;
import com.kill.gaebokchi.global.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    private final JWTUtil jwtUtil;
    private final RedisService redisService;
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .csrf((auth)->auth.disable())
                .formLogin((auth)->auth.disable())
                .httpBasic((auth)->auth.disable())
                .authorizeHttpRequests((auth)->auth
                        .requestMatchers("/","/admin/login", "/admin/reissue").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement((session)->session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http
                .addFilterBefore(new JWTFilter(jwtUtil, redisService), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
