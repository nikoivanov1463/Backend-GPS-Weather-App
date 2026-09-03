package com.tracking.tracking_app.EmailSenders;

import com.tracking.tracking_app.DTOs.ChangePasswordRequestDTO;
import io.mailtrap.client.MailtrapClient;
import io.mailtrap.config.MailtrapConfig;
import io.mailtrap.model.request.emails.Address;
import io.mailtrap.model.request.emails.MailtrapMail;
import io.mailtrap.factory.MailtrapClientFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ResetPasswordEmailSender implements ResetPasswordInterface {
    @Value("${mailtrap.api-key}")
    private String apiKey;

    @Value("${mailtrap.sender-email}")
    private String senderEmail;

    @Override
    public void resetPassword(ChangePasswordRequestDTO changePasswordRequestDTO, String token) {
        final String name = changePasswordRequestDTO.getEmail().split("@")[0];

        final MailtrapConfig config = new MailtrapConfig.Builder()
                .token(apiKey)
                .build();

        final MailtrapClient client = MailtrapClientFactory.createMailtrapClient(config);

        MailtrapMail mail = setupEmail(name, token);

        try {
            System.out.println(client.send(mail));
        } catch (Exception e) {
            System.out.println("Caught mailtrap exception : " + e);
        }
    }

    private MailtrapMail setupEmail(String name, String token) {
        String link = "https://localhost:8080/web/reset?token=" + token;

        return MailtrapMail.builder()
                .from(new Address(senderEmail, "Tracker App"))
                .to(List.of(new Address("neznam54@mail.bg")))
                .subject("Request for resetting your password")
                .text("Dear " + name + ",\n\nWe received a request to reset your password.\n")
                .html("<p>Dear " + name + ",</p>"
                        + "<p>We received a request to reset your password.</p>"
                        + "<p><strong>Link: </strong><a href=\"" + link + "\">Click here to reset your password</a></p>"
                        + "<p>If you didn’t request this, ignore this email.</p>")
                .category("Password Reset")
                .build();
    }
}
