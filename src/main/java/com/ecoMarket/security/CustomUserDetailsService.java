package com.ecoMarket.security;

import com.ecoMarket.model.Seller;
import com.ecoMarket.model.User;
import com.ecoMarket.model.enums.Role;
import com.ecoMarket.repository.SellerRepository;
import com.ecoMarket.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private static final String SELLER_PREFIX = "seller_";
    private final SellerRepository sellerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username.startsWith(SELLER_PREFIX)) {
            String actualUsername = username.substring(SELLER_PREFIX.length());
            Seller seller = sellerRepository.findByEmail(actualUsername);

            if (seller != null) {
                return buildUserDetails(seller.getEmail(), seller.getPassword(), seller.getRole());
            }
        } else {
            User user = userRepository.findByEmail(username);
            if (user != null) {
                return buildUserDetails(user.getEmail(), user.getPassword(), user.getRoles());
            }
        }
        throw new UsernameNotFoundException("User or seller not found with email: " + username);
    }

    private UserDetails buildUserDetails(String email, String password, Role role) {
        if (role == null) {
            role = Role.ROLE_CUSTOMER;
        }

        // Якщо в майбутньому буде кілька ролей, можна легко розширити
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role.name()));

        return org.springframework.security.core.userdetails.User
                .withUsername(email)
                .password(password)
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}
