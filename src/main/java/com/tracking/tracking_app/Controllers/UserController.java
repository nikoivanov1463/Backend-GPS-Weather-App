package com.tracking.tracking_app.Controllers;

import com.tracking.tracking_app.DTOs.ChangePasswordRequestDTO;
import com.tracking.tracking_app.DTOs.UserRequestDTO;
import com.tracking.tracking_app.Services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody @Valid UserRequestDTO userRequestDTO) {
        ResponseEntity<String> userServiceResponse = userService.registerUser(userRequestDTO);

        return userServiceResponse;
    }

    @PostMapping("/login")
    @Transactional
    public ResponseEntity<String> loginUser(@RequestBody @Valid UserRequestDTO userRequestDTO) {
        ResponseEntity<String> userServiceResponse = userService.loginUser(userRequestDTO);

        return userServiceResponse;
    }

    @PostMapping("/change")
    public ResponseEntity<String> changePasswordUser(@RequestBody @Valid ChangePasswordRequestDTO changePasswordRequestDTO) {
        ResponseEntity<String> userServiceResponse = userService.sendResetPasswordUser(changePasswordRequestDTO);

        return userServiceResponse;
    }

    @DeleteMapping("/logout")
    public ResponseEntity<String> logoutUser(@RequestHeader(value = "Authorization") String authHeader) {
        ResponseEntity<String> userServiceResponse = userService.logoutUser(authHeader);

        return userServiceResponse;
    }

    @PostMapping("/session-check")
    public ResponseEntity<String> checkSession(@RequestHeader(value = "Refresh_Token") String refreshToken) {
        ResponseEntity<String> userServiceResponse = userService.sessionCheckUser(refreshToken);

        return userServiceResponse;
    }
}