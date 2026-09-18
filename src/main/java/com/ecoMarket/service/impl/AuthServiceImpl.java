package com.ecoMarket.service.impl;

import com.ecoMarket.dtos.request.LoginRequest;
import com.ecoMarket.dtos.request.SignupRequest;
import com.ecoMarket.dtos.response.ApiResponse;
import com.ecoMarket.dtos.response.AuthResponse;
import com.ecoMarket.dtos.response.UserResponse;
import com.ecoMarket.mapper.UserMapper;
import com.ecoMarket.model.Cart;
import com.ecoMarket.model.Seller;
import com.ecoMarket.model.User;
import com.ecoMarket.model.VerificationCode;
import com.ecoMarket.model.enums.Role;
import com.ecoMarket.repository.CartRepository;
import com.ecoMarket.repository.SellerRepository;
import com.ecoMarket.repository.UserRepository;
import com.ecoMarket.repository.VerificationCodeRepository;
import com.ecoMarket.security.CustomUserDetailsService;
import com.ecoMarket.security.JwtProvider;
import com.ecoMarket.service.AuthService;
import com.ecoMarket.utils.OtpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;
    private final JwtProvider jwtProvider;
    private final VerificationCodeRepository verificationCodeRepository;
    private final SellerRepository sellerRepository;
    private final EmailService emailService;
    private final CustomUserDetailsService customUserDetailsService;
    private final UserMapper userMapper;


    @Override
    public void sendLoginOpt(String email) throws Exception {
        String SIGNING_PREFIX = "signing_";


        if (email.startsWith(SIGNING_PREFIX)) {
            email = email.substring(SIGNING_PREFIX.length());

//            if (role.equals(Role.ROLE_SELLER)) {
//                Seller seller = sellerRepository.findByEmail(email);
//                if (seller == null) {
//                    throw new Exception("Seller not found with provided email");
//                }
//            } else {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new Exception("User not found with provided email"));

        }
        VerificationCode isExist = verificationCodeRepository.findByEmail(email);
        if (isExist != null) {
            verificationCodeRepository.delete(isExist);
        }
        String otp = OtpUtil.generateOtp();
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setOtp(otp);
        verificationCode.setEmail(email);
        verificationCodeRepository.save(verificationCode);

        String subject = "Login OTP";
        String text = "Your login/signup OTP is " + otp;

        emailService.sendVerificationOtpEmail(email, otp, subject, text);


    }

    @Override
    public AuthResponse createUser(SignupRequest request) throws Exception {
        if (request == null) {
            throw new IllegalArgumentException("SignupRequest cannot be null");
        }

        String email = request.getEmail();
        String password = request.getPassword();
        String otp = request.getOtp();

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }
        if (otp == null || otp.isBlank()) {
            throw new IllegalArgumentException("OTP is required");
        }

        VerificationCode verificationCode = verificationCodeRepository.findByEmail(email.trim());
        if (verificationCode == null || !verificationCode.getOtp().equals(otp.trim())) {
            throw new Exception("Invalid OTP");
        }

        if (userRepository.findByEmail(email.trim()).isPresent()) {
            throw new Exception("User already exists");
        }

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User savedUser = userRepository.save(user);

        verificationCodeRepository.delete(verificationCode);

        Cart cart = Cart.builder()
                .user(savedUser)
                .build();
        cartRepository.save(cart);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                savedUser.getEmail(),
                null,
                buildAuthority(savedUser.getRoles()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setMessage("User created successfully");
        authResponse.setRole(savedUser.getRoles());

        return authResponse;
    }

    @Override
    public AuthResponse signIn(LoginRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("LoginRequest cannot be null");
        }

        String email = request.getEmail();
        String password = request.getPassword();

        if (email == null || email.isBlank()) {
            throw new BadCredentialsException("Email is required");
        }
        if (password == null || password.isBlank()) {
            throw new BadCredentialsException("Password is required");
        }

        User user = userRepository.findByEmail(email.trim())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getEmail(),
                null,
                buildAuthority(user.getRoles())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setMessage("User signed in successfully");
        authResponse.setJwt(jwt);
        authResponse.setRole(user.getRoles());

        return authResponse;
    }
    private List<GrantedAuthority> buildAuthority(Role role){
        return List.of(new SimpleGrantedAuthority(role.toString()));
    }
}
