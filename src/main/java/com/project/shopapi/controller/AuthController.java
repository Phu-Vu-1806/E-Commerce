package com.project.shopapi.controller;

import com.project.shopapi.entity.enums.ERole;
import com.project.shopapi.entity.RefreshToken;
import com.project.shopapi.entity.Role;
import com.project.shopapi.entity.User;
import com.project.shopapi.payload.request.LoginRequest;
import com.project.shopapi.payload.request.RefreshTokenRequest;
import com.project.shopapi.payload.request.SignupRequest;
import com.project.shopapi.payload.response.JwtResponse;
import com.project.shopapi.payload.response.TokenRefreshResponse;
import com.project.shopapi.repository.RoleRepository;
import com.project.shopapi.repository.UserRepository;
import com.project.shopapi.security.jwt.JwtUtils;
import com.project.shopapi.security.service.UserDetailsImpl;
import com.project.shopapi.service.impl.RefreshTokenServiceImpl;
import com.project.shopapi.utils.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RefreshTokenServiceImpl refreshTokenService;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;


    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        System.out.println(userDetails);

        String jwt = jwtUtils.generateJwtToken(userDetails.getUsername());
        User user = userRepository.findByUsername(userDetails.getUsername()).get();
        if (user.getRefreshToken() != null) {
            refreshTokenService.refreshToken(user);
        } else {
            refreshTokenService.createRefreshToken(user);
        }

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(), encoder.encode(signUpRequest.getPassword()));
        String role = signUpRequest.getRole();

        if (role == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER).get();
            user.setRole(userRole);
        } else {

            switch (role) {
                case "admin":
                    System.out.println(ERole.ROLE_ADMIN);
                    Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN).get();

                    user.setRole(adminRole);

                    break;
                case "mod":
                    Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR).get();

                    user.setRole(modRole);
                    break;
                default:
                    Role userRole = roleRepository.findByName(ERole.ROLE_USER).get();

                    user.setRole(userRole);
            }

        }

        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {

        Optional<RefreshToken> refreshToken = refreshTokenService.findByToken(request.getRefreshToken());
        if (refreshToken.isPresent()) {
            if (refreshTokenService.verifyExpiration(refreshToken.get())) {
                User user = refreshToken.get().getUser();
                String token = jwtUtils.generateJwtToken(user.getUsername());
                return ResponseEntity.ok(new TokenRefreshResponse(token, request.getRefreshToken()));
            }

        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Not found"));
        }
        return null;
    }
}
