package com.api.service;

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

            String htmlContent = "<!DOCTYPE html>"
                    + "<html lang=\"en\">"
                    + "<head>"
                    + "<meta charset=\"UTF-8\" />"
                    + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />"
                    + "<title>Verify Account</title>"
                    + "<link href=\"https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css\" rel=\"stylesheet\" />"
                    + "</head>"
                    + "<body class=\"flex items-center justify-center min-h-screen min-h-screen\">"
                    + "<div class=\"w-1/3\">"
                    + "<div class=\"flex items-center justify-between mb-4\">"
                    + "<img width=\"32\" src=\"https://ci3.googleusercontent.com/meips/ADKq_NbWe7fsWlEs_rpn43XN33-ZFxOxQFsnVpaxTJ8s5l52pgt1QxMyaQle_ZsYwPq4tLPsGTgbVPocYESX4Qt2gXoBLek90hz8X-yoBKvjwTJEtb0=s0-d-e1-ft#https://static.xx.fbcdn.net/rsrc.php/v4/yS/r/ZirYDPWh0YD.png\" height=\"32\" style=\"border: 0\" class=\"CToWUd\" data-bit=\"iit\" />"
                    + "<div class=\"font-semibold flex items-center gap-2\">"
                    + "<span>" + to + "</span>"
                    + "<img width=\"32\" src=\"https://ci3.googleusercontent.com/meips/ADKq_NY8CsqjtlrfnBSny6yaH7QrWA-vuo4ZRZ4ovmjyae4WTecWsPPKwv7jMsERbRwqswyeBDAh7VO3zKDsL0xzRhgc-FTClRdU5GJrtA9SvX-f08vmWw-pjgaaKsWVOTGNsXl6EYM0yM_50WmZ_Q2S_FuvvWabk6hBLkS_l20bmmwxGTmsz7iMWUr75QUyk2FZk0QjBWe7313W5Rh2VjK14h5CxBfFyvQ7sv9R968dA0Z9Er0z147Bg0gch5bSmssnAymbSCmpnn4_9X7azt_q4GAxpD18rk3PHNw1q89kRLxxDDc1JknhnXUjPGKlUl6h9BaktKjqDjhxNItuvolXPzBm2myFH7UuxafKHgf4503eyPy4Fj_ZrMIshJhthYjKKJQTdW1FDe79nAZ0BnLQvZue8UGgCh-TilB2JbNZI2JQBWR-1qeUKAQf3LwAaYTS-WKu4caumK3qo27tSghy832cB2mF27Vaj4HgtRe08IIUa55nMDVk2CP4p1dsbTa0VCZ7Ity7Jp5wMRLPsSAs2qD0bf7YUhNZRc02as4O6TO94o1V6uEE71Sv9LJUaeccnw=s0-d-e1-ft#https://scontent.xx.fbcdn.net/v/t1.30497-1/453178253_471506465671661_2781666950760530985_n.png?stp=cp0_dst-png_mk1-ffffff-0.00_s64x64&amp;_nc_cat=1&amp;ccb=1-7&amp;_nc_sid=08a855&amp;_nc_ohc=LKYJxVlGs68Q7kNvwGaVFXu&amp;_nc_oc=AdmfjCudF_Kk0U1LVOBXqR8YR9F66189oDVxIT0CgQCBtOms_xd4YWqQTGwryakZc58qpPOmk6XT5sqelbeW4IyY&amp;_nc_ad=z-m&amp;_nc_cid=0&amp;_nc_zt=24&amp;_nc_ht=scontent.xx&amp;oh=00_AfOxR3RubB2F5Jia1o5w8LTrOf9dFztzdZZ-AUTBxP1ctw&amp;oe=6885083A\" height=\"32\" style=\"border: 0\" class=\"CToWUd\" data-bit=\"iit\" />"
                    + "</div>"
                    + "</div>"
                    + "<div class=\"border-t border-b border-black\">"
                    + "<h2 class=\"text-3xl font-semibold text-left my-4\">One more step to register account</h2>"
                    + "<div class=\"flex flex-col gap-4\">"
                    + "<p>Hi,</p>"
                    + "<p>We got your request to verify your email. Enter this code in Alignify:</p>"
                    + "<div class=\"bg-blue-100 border border-blue-500 w-full p-4 rounded-lg\">"
                    + "<p class=\"text-xl font-semibold text-center\">" + otp + "</p>"
                    + "</div>"
                    + "<p class=\"text-sm text-center\">Don't share this code with anyone.</p>"
                    + "<div>"
                    + "<p class=\"font-semibold\">If someone asks for this code</p>"
                    + "<p>Don't share this code with anyone, especially if they tell you they work for <b class=\"text-blue-600\">Alignify</b>. They may be trying to hack your account.</p>"
                    + "</div>"
                    + "<div>"
                    + "<p class=\"font-semibold\">Didn't request this?</p>"
                    + "<p>If you got this email but aren't trying to verify, let us know. You don't need to take any further steps, as long as you don't share this code with anyone. If you like to make your account more secure, visit Security Checkup.</p>"
                    + "</div>"
                    + "<div class=\"font-semibold mb-4\">"
                    + "<p>Thanks,</p>"
                    + "<p>Alignify Security</p>"
                    + "</div>"
                    + "</div>"
                    + "</div>"
                    + "</div>"
                    + "</body>"
                    + "</html>";

            helper.setText(htmlContent, true);
            javaMailSender.send(message);
        } catch (MessagingException | IllegalStateException e) {
            throw new RuntimeException("Failed to send OTP email: " + e.getMessage(), e);
        }
    }

    public void sendResetPasswordEmail(String to, String name, String avatar, String resetUrl) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
            helper.setTo(to);
            helper.setFrom(fromEmail);
            helper.setSubject("Alignify: Verify Your Email");
            String htmlContent = "<!DOCTYPE html>"
                    + "<html lang=\"en\">"
                    + "<head>"
                    + "<meta charset=\"UTF-8\" />"
                    + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />"
                    + "<title>Reset Account</title>"
                    + "<link href=\"https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css\" rel=\"stylesheet\" />"
                    + "</head>"
                    + "<body class=\"flex items-center justify-center min-h-screen min-h-screen\">"
                    + "<div class=\"w-1/3\">"
                    + "<div class=\"flex items-center justify-between mb-4\">"
                    + "<img width=\"32\" src=\"https://ci3.googleusercontent.com/meips/ADKq_NbWe7fsWlEs_rpn43XN33-ZFxOxQFsnVpaxTJ8s5l52pgt1QxMyaQle_ZsYwPq4tLPsGTgbVPocYESX4Qt2gXoBLek90hz8X-yoBKvjwTJEtb0=s0-d-e1-ft#https://static.xx.fbcdn.net/rsrc.php/v4/yS/r/ZirYDPWh0YD.png\" height=\"32\" style=\"border: 0\" class=\"CToWUd\" data-bit=\"iit\" />"
                    + "<div class=\"font-semibold flex items-center gap-2\">"
                    + "<span>" + name + "</span>"
                    + "<img width=\"32\" src=\""
                    + (avatar == null || avatar.isEmpty() || avatar.isBlank()
                            ? "https://ci3.googleusercontent.com/meips/ADKq_NY8CsqjtlrfnBSny6yaH7QrWA-vuo4ZRZ4ovmjyae4WTecWsPPKwv7jMsERbRwqswyeBDAh7VO3zKDsL0xzRhgc-FTClRdU5GJrtA9SvX-f08vmWw-pjgaaKsWVOTGNsXl6EYM0yM_50WmZ_Q2S_FuvvWabk6hBLkS_l20bmmwxGTmsz7iMWUr75QUyk2FZk0QjBWe7313W5Rh2VjK14h5CxBfFyvQ7sv9R968dA0Z9Er0z147Bg0gch5bSmssnAymbSCmpnn4_9X7azt_q4GAxpD18rk3PHNw1q89kRLxxDDc1JknhnXUjPGKlUl6h9BaktKjqDjhxNItuvolXPzBm2myFH7UuxafKHgf4503eyPy4Fj_ZrMIshJhthYjKKJQTdW1FDe79nAZ0BnLQvZue8UGgCh-TilB2JbNZI2JQBWR-1qeUKAQf3LwAaYTS-WKu4caumK3qo27tSghy832cB2mF27Vaj4HgtRe08IIUa55nMDVk2CP4p1dsbTa0VCZ7Ity7Jp5wMRLPsSAs2qD0bf7YUhNZRc02as4O6TO94o1V6uEE71Sv9LJUaeccnw=s0-d-e1-ft#https://scontent.xx.fbcdn.net/v/t1.30497-1/453178253_471506465671661_2781666950760530985_n.png?stp=cp0_dst-png_mk1-ffffff-0.00_s64x64&amp;_nc_cat=1&amp;ccb=1-7&amp;_nc_sid=08a855&amp;_nc_ohc=LKYJxVlGs68Q7kNvwGaVFXu&amp;_nc_oc=AdmfjCudF_Kk0U1LVOBXqR8YR9F66189oDVxIT0CgQCBtOms_xd4YWqQTGwryakZc58qpPOmk6XT5sqelbeW4IyY&amp;_nc_ad=z-m&amp;_nc_cid=0&amp;_nc_zt=24&amp;_nc_ht=scontent.xx&amp;oh=00_AfOxR3RubB2F5Jia1o5w8LTrOf9dFztzdZZ-AUTBxP1ctw&amp;oe=6885083A"
                            : avatar)
                    + "\" height=\"32\" style=\"border: 0\" class=\"CToWUd\" data-bit=\"iit\" />"
                    + "</div>"
                    + "</div>"
                    + "<div class=\"border-t border-b border-black\">"
                    + "<h2 class=\"text-3xl font-semibold text-left my-4\">One more step to reset your account</h2>"
                    + "<div class=\"flex flex-col gap-4\">"
                    + "<p>Hi,</p>"
                    + "<p>We got your request to reset your account. Access this token url to redirect to change new password:</p>"
                    + "<div class=\"bg-blue-100 border border-blue-500 w-full p-4 rounded-lg\">"
                    + "<p class=\"text-xl font-semibold text-center\">" + resetUrl + "</p>"
                    + "</div>"
                    + "<p class=\"text-sm text-center\">Don't share this code with anyone.</p>"
                    + "<div>"
                    + "<p class=\"font-semibold\">If someone asks for this code</p>"
                    + "<p>Don't share this token with anyone, especially if they tell you they work for <b class=\"text-blue-600\">Alignify</b>. They may be trying to hack your account.</p>"
                    + "</div>"
                    + "<div>"
                    + "<p class=\"font-semibold\">Didn't request this?</p>"
                    + "<p>If you got this email but aren't trying to reset account, let us know. You don't need to take any further steps, as long as you don't share this code with anyone. If you like to make your account more secure, visit Security Checkup.</p>"
                    + "</div>"
                    + "<div class=\"font-semibold mb-4\">"
                    + "<p>Thanks,</p>"
                    + "<p>Alignify Security</p>"
                    + "</div>"
                    + "</div>"
                    + "</div>"
                    + "</div>"
                    + "</body>"
                    + "</html>";
            helper.setText(htmlContent, true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send password reset email", e);
        }
    }
}
