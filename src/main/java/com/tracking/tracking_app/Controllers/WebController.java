package com.tracking.tracking_app.Controllers;

import com.tracking.tracking_app.DTOs.ChangePasswordWebRequestDTO;
import com.tracking.tracking_app.Entities.ResetPasswordToken;
import com.tracking.tracking_app.Repositories.ResetPasswordRepository;
import com.tracking.tracking_app.Services.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/web")
public class WebController {
    private final ResetPasswordRepository resetPasswordRepository;

    private final UserService userService;

    public WebController(ResetPasswordRepository resetPasswordRepository, UserService userService) {
        this.resetPasswordRepository = resetPasswordRepository;
        this.userService = userService;
    }

    @GetMapping("/reset")
    public String resetPassword(@RequestParam("token") @Size(min = 36, max = 36) String token, Model model) {
        Optional<ResetPasswordToken> resetToken = resetPasswordRepository.findByToken(token);

        System.out.println(resetToken);

        if (resetToken.isEmpty()) {
            model.addAttribute("errorCode", "403");
            model.addAttribute("error", "Invalid or expired token.");

            return "error";
        }

        model.addAttribute("resetPassword", new ChangePasswordWebRequestDTO());
        model.addAttribute("token", token);

        return "reset-password";
    }

    @PostMapping("/reset")
    public String handleResetPassword(@RequestParam("token") String token, @Valid @ModelAttribute("resetPassword") ChangePasswordWebRequestDTO changePasswordWebRequestDTO, BindingResult resultFromForm, RedirectAttributes redirectAttributes) {
        if(resultFromForm.hasErrors()){
            return "reset-password";
        }

        ResponseEntity<String> response = userService.resetPasswordUser(token, changePasswordWebRequestDTO.getPassword());

        redirectAttributes.addFlashAttribute("message", response.getBody());

        return "redirect:/web/result";
    }

    @GetMapping("/result")
    public String resetPassword() {
        return "result";
    }
}
