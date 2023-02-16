package net.tuchnyak.jwtsecuritydemo.service;

import lombok.RequiredArgsConstructor;
import net.tuchnyak.jwtsecuritydemo.model.auth.AuthenticateRequest;
import net.tuchnyak.jwtsecuritydemo.model.auth.AuthenticationResponse;
import net.tuchnyak.jwtsecuritydemo.model.auth.RegisterRequest;
import net.tuchnyak.jwtsecuritydemo.user.Role;
import net.tuchnyak.jwtsecuritydemo.user.User;
import net.tuchnyak.jwtsecuritydemo.user.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticateService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);

        return new AuthenticationResponse(jwtToken);
    }

    public AuthenticationResponse authenticate(AuthenticateRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(
                        () -> new UsernameNotFoundException("User '" + request.getEmail() + "' not found!")
                );
        var jwtToken = jwtService.generateToken(user);

        return new AuthenticationResponse(jwtToken);
    }

}
