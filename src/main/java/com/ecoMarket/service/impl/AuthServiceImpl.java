package com.ecoMarket.service.impl;

import com.ecoMarket.dtos.request.SignupRequest;
import com.ecoMarket.dtos.response.ApiResponse;
import com.ecoMarket.model.Cart;
import com.ecoMarket.model.User;
import com.ecoMarket.model.enums.Role;
import com.ecoMarket.repository.CartRepository;
import com.ecoMarket.repository.UserRepository;
import com.ecoMarket.security.JwtProvider;
import com.ecoMarket.service.AuthService;
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

    @Override
    public String createUser(SignupRequest request) {
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
