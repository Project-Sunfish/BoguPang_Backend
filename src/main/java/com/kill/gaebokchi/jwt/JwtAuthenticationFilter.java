package com.kill.gaebokchi.jwt;

import com.kill.gaebokchi.entity.Member;
import com.kill.gaebokchi.repository.MemberRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtils jwtTokenProvider;
    private final MemberRepository memberRepository;
    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    private static final String LOGIN = "/login";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("jwtAuthenticationFilter");
        log.info("request URL : "+request.getRequestURL());
        log.info("request header : "+request.getHeader("Authorization"));
        log.info("extractAccessToken : "+jwtTokenProvider.extractAccessToken(request));
        if(request.getRequestURL().toString().equals(LOGIN)){
            log.info("/login이다");
            filterChain.doFilter(request, response); //filter x
            return;
        }

        //refresh token reissue 재설정 필요
        checkAccessTokenAndAuthentication(request, response, filterChain);
    }
    public void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException{
        log.info("checkAccessTokenAndAuthentication 호출");
        jwtTokenProvider.extractAccessToken(request)
                .filter(jwtTokenProvider::validateToken)
                .ifPresent(accessToken->jwtTokenProvider.extractEmail(accessToken)
                        .ifPresent(email->memberRepository.findByEmail(email)
                                .ifPresent(this::saveAuthentication)));

        filterChain.doFilter(request, response);
    }
    public void saveAuthentication(Member member) {
        UserDetails userDetailsUser = org.springframework.security.core.userdetails.User.builder()
                .username(member.getEmail())
                .password("")
                .roles(member.getRole().name())
                .build();

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDetailsUser, null, authoritiesMapper.mapAuthorities(userDetailsUser.getAuthorities()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}