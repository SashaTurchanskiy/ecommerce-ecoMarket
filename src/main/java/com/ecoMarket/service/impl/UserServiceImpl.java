package com.ecoMarket.service.impl;

import com.ecoMarket.dtos.response.UserResponse;
import com.ecoMarket.mapper.UserMapper;
import com.ecoMarket.model.User;
import com.ecoMarket.repository.UserRepository;
import com.ecoMarket.security.JwtProvider;
import com.ecoMarket.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final UserMapper userMapper;

    @Override
    public UserResponse findUserByJwtToken(String jwt) throws Exception {
        try {
            String email = jwtProvider.getEmailFromJwtToken(jwt);
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new Exception("User not found with email: " + email));
            return userMapper.toResponse(user);
        } catch (Exception e) {
            throw new Exception("Error finding user by JWT token: " + e.getMessage());
        }
    }

    @Override
    public UserResponse findUserByEmail(String email) throws Exception {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new Exception("User not found with email: " + email));
        return userMapper.toResponse(user);
    }
}
