package com.api.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendSimpleEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        javaMailSender.send(message);
    }

    public void sendOtpEmail(String to, String otp) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
            helper.setTo(to);
            helper.setFrom(fromEmail);
            helper.setSubject("Alignify: Verify Your Email");

            // Sanitize inputs to prevent unexpected characters
            String safeDate = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("dd MMMM yyyy | hh:mm a"))
                    .replaceAll("[^a-zA-Z0-9\\s|:]", ""); // Safe date format

            // Construct HTML content using + concatenation
            String htmlContent = "<!DOCTYPE html>"
                    + "<html lang=\"en\">"
                    + "<head>"
                    + "<meta charset=\"UTF-8\">"
                    + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
                    + "<title>Alignify: Verify Your Email</title>"
                    + "<style>"
                    + "body { margin: 0; padding: 0; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif; background-color: #f5e8df; color: #333 }"
                    + ".container { max-width: 600px; margin: 20px auto; background-color: #fff3e6; border-radius: 8px; overflow: hidden; box-shadow: 0 2px 4px rgba(0,0,0,0.1) }"
                    + ".header { display: flex; justify-content: space-between; align-items: center; padding: 20px; background-color: #fff3e6 }"
                    + ".header img { max-width: 150px }"
                    + ".header .date { font-size: 14px; color: #666 }"
                    + ".content { padding: 30px; text-align: center; display: flex; flex-direction: column; align-items: center; gap: 20px }"
                    + ".otp-code { font-size: 32px; font-weight: bold; color: #d35400; letter-spacing: 4px; padding: 10px 20px; background-color: #fff; border-radius: 4px; border: 1px solid #e0e0e0 }"
                    + ".instructions { font-size: 16px; line-height: 1.6; color: #666 }"
                    + ".button { display: inline-block; padding: 12px 30px; background-color: #f4a261; color: #fff; text-decoration: none; border-radius: 5px; font-size: 16px; font-weight: bold }"
                    + ".cancel-link { color: #e74c3c; text-decoration: underline }"
                    + ".footer { padding: 20px; text-align: center; background-color: #fff3e6; font-size: 12px; color: #666 }"
                    + ".footer .social-icons img { width: 24px; margin: 0 5px }"
                    + "@media only screen and (max-width: 600px) { .container { margin: 10px } .header { flex-direction: column; text-align: center } .content { padding: 20px } .otp-code { font-size: 24px } .button { width: 100%; box-sizing: border-box } }"
                    + "</style>"
                    + "</head>"
                    + "<body>"
                    + "<div class=\"container\">"
                    + "<div class=\"header\">"
                    + "<img src=\"https://img.icons8.com/?size=100&id=Qr3ggQTcZ4PM&format=png&color=000000\" alt=\"Alignify Logo\">"
                    + "<div class=\"date\">Alignify | " + safeDate + "</div>"
                    + "</div>"
                    + "<div class=\"content\">"
                    + "<p class=\"instructions\">Please use the OTP code below to verify your email address.</p>"
                    + "<div class=\"otp-code\">" + otp + "</div>"
                    + "<p class=\"instructions\">If you did not make this request, <a href=\"#\" class=\"cancel-link\">click here to cancel</a>.</p>"
                    + "<a href=\"https://alignify.com/verify\" class=\"button\">Verify</a>"
                    + "</div>"
                    + "<div class=\"footer\">"
                    + "<div class=\"social-icons\">"
                    + "<img src=\"https://img.icons8.com/?size=100&id=uLWV5A9vXIPu&format=png&color=000000\" alt=\"Facebook\">"
                    + "<img src=\"https://img.icons8.com/?size=100&id=Xy10Jcu1L2Su&format=png&color=000000\" alt=\"Instagram\">"
                    + "</div>"
                    + "<p>Terms and Conditions | Privacy Policy | Support</p>"
                    + "<p>This email was sent to you by Alignify.<br>You are receiving this email because you registered for \"Alignify\".<br>If you wish to unsubscribe from all future emails, please <a href=\"#\">click here</a>.</p>"
                    + "</div>"
                    + "</div>"
                    + "</body>"
                    + "</html>";

            // Validate no unexpected semicolons outside CSS
            if (htmlContent.contains(";") && !htmlContent.contains("style>")) {
                throw new IllegalStateException("Unexpected semicolon detected in HTML content: " + htmlContent);
            }
            helper.setText(htmlContent, true);
            javaMailSender.send(message);
        } catch (MessagingException | IllegalStateException e) {
            throw new RuntimeException("Failed to send OTP email: " + e.getMessage(), e);
        }
    }

    public void sendResetPasswordEmail(String to, String resetUrl) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
        try {
            helper.setTo(to);
            helper.setFrom(fromEmail);
            helper.setSubject("Alignify: Reset Your Password");
            String htmlContent = """
                    <!DOCTYPE html>
                    <html lang="en">
                    <head>
                        <meta charset="UTF-8">
                        <meta name="viewport" content="width=device-width, initial-scale=1.0">
                        <style>
                            body {
                                margin: 0;
                                padding: 0;
                                font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif;
                                background-color: #f4f4f4;
                                color: #333;
                            }
                            .container {
                                max-width: 600px;
                                margin: 20px auto;
                                background-color: #ffffff;
                                border-radius: 8px;
                                overflow: hidden;
                                box-shadow: 0 2px 4px rgba(0,0,0,0.1);
                            }
                            .header {
                                background-color: #4a90e2;
                                padding: 20px;
                                text-align: center;
                            }
                            .header img {
                                max-width: 150px;
                            }
                            .content {
                                padding: 30px;
                                display: flex;
                                flex-direction: column;
                                align-items: center;
                                gap: 20px;
                            }
                            .instructions {
                                font-size: 16px;
                                line-height: 1.6;
                                color: #555;
                                text-align: center;
                            }
                            .button {
                                display: inline-block;
                                padding: 12px 30px;
                                background-color: #4a90e2;
                                color: #ffffff;
                                text-decoration: none;
                                border-radius: 6px;
                                font-size: 16px;
                                font-weight: bold;
                                transition: background-color 0.3s;
                            }
                            .button:hover {
                                background-color: #357abd;
                            }
                            .footer {
                                background-color: #f4f4f4;
                                padding: 20px;
                                text-align: center;
                                font-size: 14px;
                                color: #777;
                            }
                            @media only screen and (max-width: 600px) {
                                .content {
                                    padding: 20px;
                                }
                                .button {
                                    width: 100%;
                                    box-sizing: border-box;
                                }
                            }
                        </style>
                    </head>
                    <body>
                        <div class="container">
                            <div class="header">
                                <img src="https://via.placeholder.com/150x50?text=Alignify+Logo" alt="Alignify Logo">
                            </div>
                            <div class="content">
                                <h1>Reset Your Alignify Password</h1>
                                <p class="instructions">Click the button below to reset your password. This link is valid for 1 hour.</p>
                                <a href="%s" class="button">Reset Password</a>
                                <p class="instructions">If you didn’t request a password reset, please secure your account or contact our support team.</p>
                            </div>
                            <div class="footer">
                                <p>Contact support at <a href="mailto:support@alignify.com">support@alignify.com</a> if you need assistance.</p>
                                <p>© 2025 Alignify. All rights reserved.</p>
                            </div>
                        </div>
                    </body>
                    </html>
                    """
                    .formatted(resetUrl);
            helper.setText(htmlContent, true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send password reset email", e);
        }
    }
}
