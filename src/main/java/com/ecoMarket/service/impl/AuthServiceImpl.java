package com.ecoMarket.service.impl;

import com.ecoMarket.dtos.request.SignupRequest;
import com.ecoMarket.dtos.response.ApiResponse;
import com.ecoMarket.model.Cart;
import com.ecoMarket.model.Seller;
import com.ecoMarket.model.User;
import com.ecoMarket.model.VerificationCode;
import com.ecoMarket.model.enums.Role;
import com.ecoMarket.repository.CartRepository;
import com.ecoMarket.repository.SellerRepository;
import com.ecoMarket.repository.UserRepository;
import com.ecoMarket.repository.VerificationCodeRepository;
import com.ecoMarket.security.JwtProvider;
import com.ecoMarket.service.AuthService;
import com.ecoMarket.utils.OtpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
                User user = userRepository.findByEmail(email);
                if (user == null) {
                    throw new Exception("User not exist with provided email");

            }
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
    public String createUser(SignupRequest request) throws Exception {

        VerificationCode verificationCode = verificationCodeRepository.findByEmail(request.getEmail());
        if (verificationCode == null || !verificationCode.getOtp().equals(request.getOtp())){
            throw new Exception("Invalid OTP");
        }

        User user = userRepository.findByEmail(request.getEmail());

        if (user == null){
            User createdUser = User.builder()
                    .email(request.getEmail())
                    .fullName(request.getFullName())
                    .roles(Role.ROLE_CUSTOMER)
                    .password(passwordEncoder.encode(request.getPassword()))
                    .build();

            userRepository.save(createdUser);
            Cart cart = Cart.builder()
                    .user(createdUser)
                    .build();

            cartRepository.save(cart);
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(Role.ROLE_CUSTOMER.toString()));

        var authentication = new UsernamePasswordAuthenticationToken(request.getEmail(), null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtProvider.generateToken(authentication);
    }
}
