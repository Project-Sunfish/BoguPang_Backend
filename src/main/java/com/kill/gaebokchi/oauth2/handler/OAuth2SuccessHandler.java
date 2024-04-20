package com.kill.gaebokchi.oauth2.handler;

import com.kill.gaebokchi.entity.Role;
import com.kill.gaebokchi.jwt.JwtUtils;
import com.kill.gaebokchi.oauth2.CustomOAuth2User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtils jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("Handler : onAuthenticationSuccess");
        log.info("request Body"+ request.getInputStream());
        try{
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

            if(oAuth2User.getRole()== Role.GUEST){
                String accessToken = jwtTokenProvider.createAccessToken(oAuth2User.getEmail());
                response.setHeader("Authorization", "Bearer "+accessToken);
                response.sendRedirect("/api/sign-up");
            }else{
                loginSuccess(response, oAuth2User);
            }
        }catch(Exception e){
            throw e;
        }
    }
    //refresh token section 수정필요
    private void loginSuccess(HttpServletResponse response, CustomOAuth2User oAuth2User) throws IOException {
        String accessToken = jwtTokenProvider.createAccessToken(oAuth2User.getEmail());
        response.addHeader("Authorization", "Bearer " + accessToken);

        jwtTokenProvider.sendAccessToken(response, accessToken);
    }
}

