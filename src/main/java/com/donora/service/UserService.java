package com.donora.service;

import com.donora.dto.*;
import com.donora.entity.PasswordResetToken;
import com.donora.enums.Role;
import com.donora.entity.User;
import com.donora.jwt.JwtService;
import com.donora.repository.PasswordResetTokenRepository;
import com.donora.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JwtService jwtService;

    public RegisterResponse registerUser(RegisterRequest request) {
        // Check if email is already taken
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email is already in use.");
        }


        // Validate role
        Role role;
        try {
            role = request.getRole(); // Convert role to enum
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid role provided.");
        }

        // Create and set user entity

        // Save the basic user info
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // Encrypt the password
        user.setPhone(request.getPhone());
        user.setRole(role);
        user.setLocationLat(request.getLocationLat());
        user.setLocationLong(request.getLocationLong());
        // Save user to database
        userRepository.save(user);

        return new RegisterResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole()
        );
    }

        // Login
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        //Creating token to send jwt with login response to frontend
        String token = jwtService.generateToken(user.getEmail());
        return new LoginResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole(),
                token
        );
    }
    // ================================
    // 1. Forgot Password (Send Token)
    // ================================
    public void sendPasswordResetToken(String email) {
        // Step 1: Find the user by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));


        String generatedToken = generateToken();
        // Step 2: Create a token
        PasswordResetToken token = new PasswordResetToken();
        token.setOtp(generatedToken); // Random string like "3dfg-1r23-abc"
        token.setUser(user);
        token.setExpiryDate(LocalDateTime.now().plusMinutes(30)); // Valid for 30 mins
        token.setUsed(false);

        // Step 3: Save token in DB
        tokenRepository.save(token);

        // Step 4: (Optional) Send email // configuration should be in .properties or .yml
        // Create email content
        String subject = "Reset Your Password";

        // this comment i will unhide when need token or creating a big application
        /*String resetLink = "http://localhost:8181/api/auth/reset-password?token=" + generatedToken;
        String body = "Hello " + user.getName() + ",\n\n"
                + "Token: "+generatedToken+ " \nCopy token and paste into input of Reset Form\n or Click below to reset your password:\n" + resetLink
                + "\n\nThis link & token will expire in 30 minutes.\n\n"
                + "If you didn’t request this, ignore this email."; */

        String body="Hello "+ user.getName()+ ",\n\n"
                +"OTP : "+generatedToken+"\n\n"
                + "This is otp For Reset Your password and it will expire in 30 minutes \n\n"
                + "If you didn’t request this, ignore this email.";

        System.out.println("📧 Sending email to: " + user.getEmail());
        System.out.println("📧 Subject: " + subject);
        System.out.println("📧 Body: " + body);
        // You can print or log the token for now
        //System.out.println("Password reset token for " + user.getEmail() + ": " + token.getToken());
        // Send email
        emailService.sendEmail(user.getEmail(), subject, body);
    }

    private String generateToken() {
        int otp = 100000 + new Random().nextInt(900000); // Generates 6-digit number between 100000–999999
        return String.valueOf(otp); // ✅ Return as String
        //when i will need to create very secure app then i unhide it
        //return UUID.randomUUID().toString();
    }

    // ================================
    // 2. Reset Password (Using Token)
    // ================================
    public ResetPasswordResponse resetPassword(String token, String newPassword) {
        // Step 1: Get token from DB
        Optional<PasswordResetToken> tokenOpt = tokenRepository.findByOtp(token);
        if (!tokenOpt.isPresent()) {
            return new ResetPasswordResponse("Invalid or expired token");
        }

        PasswordResetToken resetToken = tokenOpt.get();

        // Step 2: Validate token
        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return new ResetPasswordResponse("Token has expired");
        }

        if (resetToken.isUsed()) {
            return new ResetPasswordResponse("Token has already been used");
        }

        // Step 3: Get user from token and update password
        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword)); // Encrypt new password
        userRepository.save(user);

        // Step 4: Mark token as used
        resetToken.setUsed(true);
        tokenRepository.save(resetToken);
        return new ResetPasswordResponse("Password successfully reset");
    }
}
