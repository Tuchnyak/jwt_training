package net.tuchnyak.jwtsecuritydemo.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.tuchnyak.jwtsecuritydemo.constants.JwtConstant;
import net.tuchnyak.jwtsecuritydemo.service.JwtService;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    protected final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeaderValue = request.getHeader(JwtConstant.HEADER_AUTHORIZATION_NAME);

        if (authHeaderValue == null || !authHeaderValue.startsWith(JwtConstant.HEADER_AUTHORIZATION_BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwtToken = authHeaderValue.split(" ")[1];
        final String userEmail = jwtService.extractUsername(jwtToken);
    }

}
