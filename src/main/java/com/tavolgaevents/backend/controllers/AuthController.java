package com.tavolgaevents.backend.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.tavolgaevents.backend.models.*;
import com.tavolgaevents.backend.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tavolgaevents.backend.exception.TokenRefreshException;
import com.tavolgaevents.backend.payload.request.LogOutRequest;
import com.tavolgaevents.backend.payload.request.LoginRequest;
import com.tavolgaevents.backend.payload.request.SignupRequest;
import com.tavolgaevents.backend.payload.request.TokenRefreshRequest;
import com.tavolgaevents.backend.payload.response.JwtResponse;
import com.tavolgaevents.backend.payload.response.MessageResponse;
import com.tavolgaevents.backend.payload.response.TokenRefreshResponse;
import com.tavolgaevents.backend.repository.RoleRepository;
import com.tavolgaevents.backend.repository.UserRepository;
import com.tavolgaevents.backend.security.jwt.JwtUtils;
import com.tavolgaevents.backend.security.services.TokenService;
import com.tavolgaevents.backend.security.services.UserDetailsImpl;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  @Autowired
  TokenService tokenService;

  @Autowired
  FileRepository fileRepository;

  @Autowired
  public JavaMailSender emailSender;

  private SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

    String jwt = jwtUtils.generateJwtToken(userDetails);

    List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
        .collect(Collectors.toList());

    RefreshToken refreshToken = tokenService.createRefreshToken(userDetails.getId());

    return ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getToken(), userDetails.getId(),
        userDetails.getUsername(), userDetails.getEmail(), roles));
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
    }

    User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),
        encoder.encode(signUpRequest.getPassword()));

    user.setFirstName(signUpRequest.getFirstName());
    user.setLastName(signUpRequest.getLastName());
    user.setMiddleName(signUpRequest.getMiddleName());
    String strRole = signUpRequest.getRole();
    Optional<Role> userRole = Optional.empty();

    if (strRole == null) {
      userRole = roleRepository.findByName(RoleConstants.ROLE_USER);
    } else {
        switch (strRole) {
        case RoleConstants.ROLE_ADMIN:
          userRole = roleRepository.findByName(RoleConstants.ROLE_ADMIN);
          break;
        case RoleConstants.ROLE_ASSESSOR:
          userRole = roleRepository.findByName(RoleConstants.ROLE_ASSESSOR);
          break;
          case RoleConstants.ROLE_JURY:
            userRole = roleRepository.findByName(RoleConstants.ROLE_JURY);
            break;
        default:
          userRole = roleRepository.findByName(RoleConstants.ROLE_USER);
        }
      }

    user.setRole(userRole.get());
    File file = new File();
    userRepository.save(user);

    simpleMailMessage.setTo(signUpRequest.getEmail());
    simpleMailMessage.setSubject("User Credentials");
    simpleMailMessage.setText("You have successfully registered on the ERK service! Your login: "
            + signUpRequest.getUsername() + "\n Your password: " + signUpRequest.getPassword());

    this.emailSender.send(simpleMailMessage);

    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  }

  @PostMapping("/refreshtoken")
  public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
    String requestRefreshToken = request.getRefreshToken();

    return tokenService.findByToken(requestRefreshToken)
        .map(tokenService::verifyExpiration)
        .map(RefreshToken::getUser)
        .map(user -> {
          String token = jwtUtils.generateTokenFromUsername(user.getUsername());
          return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
        })
        .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
            "Refresh token is not in database!"));
  }
  
  @PostMapping("/logout")
  public ResponseEntity<?> logoutUser(@Valid @RequestBody LogOutRequest logOutRequest) {
    tokenService.deleteByUserId(logOutRequest.getUserId());
    return ResponseEntity.ok(new MessageResponse("Log out successful!"));
  }

}
