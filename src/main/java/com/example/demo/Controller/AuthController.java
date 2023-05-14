package com.example.demo.Controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.demo.Exception.JwtTokenInvalidException;
import com.example.demo.Model.*;
import com.example.demo.Repo.RoleRepository;
import com.example.demo.Repo.UserJwtRepository;
import com.example.demo.Repo.UserProfileRepository;
import com.example.demo.Repo.UserRepository;
import com.example.demo.Security.JWT.JwtUtils;
import com.example.demo.Security.Payload.Request.LoginRequest;
import com.example.demo.Security.Payload.Request.SignupRequest;
import com.example.demo.Security.Payload.Response.MessageResponse;
import com.example.demo.Security.Payload.Response.UserInfoResponse;
import com.example.demo.Security.Services.UserDetailsImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@CrossOrigin(allowCredentials = "true", origins = {"http://localhost:4200", "https://main--superb-cupcake-f2ff38.netlify.app"})
@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {
    AuthenticationManager authenticationManager;

    UserRepository userRepository;

    UserProfileRepository userProfileRepository;

    UserJwtRepository userJwtRepository;

    RoleRepository roleRepository;

    PasswordEncoder encoder;

    JwtUtils jwtUtils;

    public AuthController(AuthenticationManager authenticationManager,
                          UserRepository userRepository,
                          UserProfileRepository userProfileRepository,
                          UserJwtRepository userJwtRepository,
                          RoleRepository roleRepository,
                          PasswordEncoder encoder,
                          JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.userJwtRepository = userJwtRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    @Transactional
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userJwtRepository.existsByUsername(signUpRequest.getUsername())) {
            userJwtRepository.deleteByUsername(signUpRequest.getUsername());
        }

        String jwtToken = jwtUtils.generateTokenFromUsernameRegister(signUpRequest.getUsername()).getValue();

        UserJwt userJwtPair = new UserJwt();
        userJwtPair.setUsername(signUpRequest.getUsername());
        userJwtPair.setPassword(encoder.encode(signUpRequest.getPassword()));
        userJwtPair.setJwtToken(jwtToken);

        userJwtRepository.save(userJwtPair);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new RegisterDTO(userJwtPair.getUsername(), userJwtPair.getJwtToken()));
    }

    @PostMapping("register/confirm/{jwtToken}")
    public ResponseEntity<?> confirmUser(@PathVariable String jwtToken) {

        if (!userJwtRepository.existsByJwtToken(jwtToken)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Token is invalid!"));
        }

        UserJwt userJwt = userJwtRepository.findByJwtToken(jwtToken);
        if (! jwtUtils.validateJwtToken(userJwt.getJwtToken()))
            throw new JwtTokenInvalidException(userJwt.getJwtToken());

        User user = new User();
        UserProfile userProfile = new UserProfile();
        userProfileRepository.save(userProfile);

        user.setUsername(userJwt.getUsername());
        user.setPassword(userJwt.getPassword());

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: User role not found."));
        roles.add(userRole);
        user.setRoles(roles);
        user.setUserProfile(userProfile);

        userJwtRepository.delete(userJwt);

        userRepository.save(user);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new MessageResponse("Successfully confirmed the registration code!"));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwtCookie = jwtUtils.generateTokenFromUsernameSignIn(userDetails.getUsername()).getValue();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        System.out.println(userDetails.getId());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new UserInfoResponse(userDetails.getId(),
                        userDetails.getUsername(),
                        roles, jwtCookie));
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new MessageResponse("You've been signed out!"));
    }
}
