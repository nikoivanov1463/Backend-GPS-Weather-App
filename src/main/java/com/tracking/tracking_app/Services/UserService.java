package com.tracking.tracking_app.Services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tracking.tracking_app.DTOs.ChangePasswordRequestDTO;
import com.tracking.tracking_app.DTOs.UserRequestDTO;
import com.tracking.tracking_app.EmailSenders.ResetPasswordEmailSender;
import com.tracking.tracking_app.Entities.RefreshJWToken;
import com.tracking.tracking_app.Entities.ResetPasswordToken;
import com.tracking.tracking_app.Entities.User;
import com.tracking.tracking_app.JWT.JWTProvider;
import com.tracking.tracking_app.Repositories.RefreshJWTokenRepository;
import com.tracking.tracking_app.Repositories.ResetPasswordRepository;
import com.tracking.tracking_app.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final RefreshJWTokenRepository refreshJWTokenRepository;

    private final JWTProvider jwtProvider;

    private final ResetPasswordEmailSender emailSender;

    @Autowired
    private ResetPasswordRepository resetPasswordRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, RefreshJWTokenRepository refreshJWTokenRepository, JWTProvider jwtProvider, ResetPasswordEmailSender emailSender) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.refreshJWTokenRepository = refreshJWTokenRepository;
        this.jwtProvider = jwtProvider;
        this.emailSender = emailSender;
    }

    public ResponseEntity<String> registerUser(UserRequestDTO userRequestDTO) {
        try {
            if (userRepository.findByEmail(userRequestDTO.getEmail()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already taken. Please try again");
            }

            User registeredUser = new User();
            registeredUser.setUsername(userRequestDTO.getUsername());
            registeredUser.setEmail(userRequestDTO.getEmail());
            registeredUser.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));

            userRepository.save(registeredUser);

            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully!");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    public ResponseEntity<String> loginUser(UserRequestDTO userRequestDTO) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userRequestDTO.getUsername(), userRequestDTO.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            User authenticatedUser = (User) authentication.getPrincipal();

            RefreshJWToken refreshJWToken = new RefreshJWToken();
            refreshJWToken.setUser(authenticatedUser);
            refreshJWToken.setExpiresAt(Instant.now().plus(15, ChronoUnit.DAYS));
            refreshJWTokenRepository.save(refreshJWToken);

            String accessToken = jwtProvider.createJWToken(authenticatedUser);
            String refreshTokenString = jwtProvider.createRefreshJWToken(authenticatedUser, refreshJWToken.getId());

            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("message", "Login successful!");
            responseBody.put("jwt", accessToken);
            responseBody.put("refresh_jwt_token", refreshTokenString);

            try {
                String json = mapper.writeValueAsString(responseBody);

                return ResponseEntity.status(HttpStatus.ACCEPTED).header(HttpHeaders.CONTENT_TYPE, "application/json").body(json);
            } catch (JsonProcessingException e) {
                System.err.println("Invalid JSON format " + e);
            }
        } catch (BadCredentialsException e) {
            try {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).header(HttpHeaders.CONTENT_TYPE, "application/json").body(mapper.writeValueAsString(e.getMessage()));
            } catch (JsonProcessingException ex) {
                System.err.println("Invalid JSON format " + ex);
            }
        }

        return null;
    }

    public ResponseEntity<String> sendResetPasswordUser(ChangePasswordRequestDTO changePasswordRequestDTO) {
        Optional<User> foundUser = userRepository.findByEmail(changePasswordRequestDTO.getEmail());

        if (foundUser.isEmpty()) {
            return ResponseEntity.ok("Email not found");
        }

        User user = foundUser.get();

        String token = UUID.randomUUID().toString();

        ResetPasswordToken resetPasswordToken = new ResetPasswordToken(token, user);

        resetPasswordRepository.save(resetPasswordToken);

        emailSender.resetPassword(changePasswordRequestDTO, token);

        return ResponseEntity.ok("Reset link sent to your email");
    }

    public ResponseEntity<String> resetPasswordUser(String token, String password) {
        Optional<ResetPasswordToken> resetToken = resetPasswordRepository.findByToken(token);

        if (resetToken.get().getToken().equals(token) && resetToken.get().getExpiryDate().isAfter(Instant.now())) {
            User user = userRepository.findById(resetToken.get().getUser().getId().toHexString()).orElseThrow();
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);

            resetPasswordRepository.delete(resetToken.get());

            return ResponseEntity.ok("Successfully reset your password");
        }

        resetPasswordRepository.delete(resetToken.get());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Couldn't reset password (maybe the link expired?)");
    }

    public ResponseEntity<String> logoutUser(String tokenHeader) {
        String token = tokenHeader.substring(7);

        if (jwtProvider.validateJWToken(token) && refreshJWTokenRepository.existsByUserId(jwtProvider.getUserIdFromJWT(token))) {
            refreshJWTokenRepository.deleteByUserId(jwtProvider.getUserIdFromJWT(token));

            return ResponseEntity.ok().body("Successful logout");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unsuccessful logout");
        }
    }

    public ResponseEntity<String> sessionCheckUser(String refreshHeader) {
        String refreshTokenString = refreshHeader.replaceAll("(?i)\\s*Bearer\\s*", " ").trim();

        if (jwtProvider.validateRefreshJWToken(refreshTokenString)) {
            String tokenId = jwtProvider.getTokenIdFromRefreshJWT(refreshTokenString);

            Optional<RefreshJWToken> refreshJWToken = refreshJWTokenRepository.findById(tokenId);

            if (refreshJWToken.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token expired");
            }

            if (refreshJWToken.get().getExpiresAt().isBefore(Instant.now())) {
                refreshJWTokenRepository.deleteById(tokenId);
            }

            Optional<User> authenticatedUser = userRepository.findById(jwtProvider.getUserIdFromRefreshJWT(refreshTokenString));

            if (authenticatedUser.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh Token not found");
            }

            refreshJWTokenRepository.deleteById(tokenId);

            RefreshJWToken newRefreshJWToken = new RefreshJWToken();
            newRefreshJWToken.setUser(authenticatedUser.get());
            newRefreshJWToken.setExpiresAt(Instant.now().plus(15, ChronoUnit.DAYS));
            refreshJWTokenRepository.save(newRefreshJWToken);

            String newAccessToken = jwtProvider.createJWToken(authenticatedUser.get());
            String newRefreshToken = jwtProvider.createRefreshJWToken(authenticatedUser.get(), newRefreshJWToken.getId());

            return ResponseEntity.ok().header("Authorization", "Bearer " + newAccessToken).header("Refresh_Token", "Bearer " + newRefreshToken).body("Generated new session token");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid session");
        }
    }
}
