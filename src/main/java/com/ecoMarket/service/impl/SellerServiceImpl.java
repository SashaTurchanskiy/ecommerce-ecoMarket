package com.ecoMarket.service.impl;

import com.ecoMarket.dtos.request.SellerRequest;
import com.ecoMarket.dtos.response.SellerResponse;
import com.ecoMarket.mapper.SellerMapper;
import com.ecoMarket.model.Seller;
import com.ecoMarket.model.VerificationCode;
import com.ecoMarket.model.enums.AccountStatus;
import com.ecoMarket.repository.SellerRepository;
import com.ecoMarket.repository.VerificationCodeRepository;
import com.ecoMarket.security.JwtProvider;
import com.ecoMarket.service.SellerService;
import com.ecoMarket.utils.OtpUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {

    private final SellerRepository sellerRepository;
    private final JwtProvider jwtProvider;
    private final SellerMapper sellerMapper;
    private final PasswordEncoder passwordEncoder;
    private final VerificationCodeRepository verificationCodeRepository;
    private final EmailService emailService;

    @Override
    public SellerResponse getSellerProfile(String jwt) throws Exception {
        String email = jwtProvider.getEmailFromJwtToken(jwt);
        return this.getSellerByEmail(email);
    }

    @Override
    public SellerResponse createSeller(SellerRequest request) throws Exception {
        if (request == null) {
            throw new IllegalArgumentException("Seller request cannot be null");
        }
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new IllegalArgumentException("Seller email is required");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new IllegalArgumentException("Seller password is required");
        }

        Seller sellerFind = sellerRepository.findByEmail(request.getEmail());
        if (sellerFind != null) {
            throw new Exception("Seller already exists");
        }

        Seller seller = sellerMapper.toEntity(request);
        seller.setAccountStatus(AccountStatus.PENDING_VERIFICATION);
        seller.setEmailVerified(false);
        seller.setPassword(passwordEncoder.encode(request.getPassword()));
        Seller savedSeller = sellerRepository.save(seller);

//        // Генерація OTP
//        String otp = OtpUtil.generateOtp();
//        VerificationCode verificationCode = new VerificationCode();
//        verificationCode.setEmail(savedSeller.getEmail());
//        verificationCode.setOtp(otp);
//        verificationCodeRepository.save(verificationCode);

        // Відправка листа
//        emailService.sendVerificationOtpEmail(
//                savedSeller.getEmail(),
//                otp,
//                "Seller Email Verification",
//                "Your seller verification OTP is " + otp
//        );

        return sellerMapper.toResponse(savedSeller);
    }

    @Override
    public SellerResponse getSellerById(Long id) throws Exception {
        Seller seller = sellerRepository.findById(id)
                .orElseThrow(()-> new Exception("cannot find seller with id"));
        return sellerMapper.toResponse(seller);
    }

    @Override
    public SellerResponse getSellerByEmail(String email) throws Exception {
        Seller seller = sellerRepository.findByEmail(email);
        if (seller == null){
            throw new Exception("cannot find seller with email");
        }
        return sellerMapper.toResponse(seller);
    }

    @Override
    public List<SellerResponse> getAllSellers() {
        return sellerRepository.findAll()
                .stream()
                .map(sellerMapper::toResponse)
                .toList();
    }

    @Override
    public SellerResponse updateSellerProfile(Long id, SellerRequest request) {
        Seller seller = sellerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Seller not found with id: " + id));

        // 2. Оновити поля (тільки ті, що прийшли в реквесті)
        seller.setSellerName(request.getSellerName());
        seller.setMobile(request.getMobile());
        seller.setEmail(request.getEmail());
        seller.setPassword(passwordEncoder.encode(request.getPassword())); // ⚠️ краще хешувати перед збереженням

        if (request.getBusinessDetails() != null) {
            seller.setBusinessDetails(request.getBusinessDetails());
        }
        if (request.getBankDetails() != null) {
            seller.setBankDetails(request.getBankDetails());
        }
        if (request.getPickupAddress() != null) {
            seller.setPickupAddress(request.getPickupAddress());
        }

        seller.setSGTIN(request.getSGTIN());

        // 3. Зберегти зміни
        Seller updatedSeller = sellerRepository.save(seller);

        // 4. Повернути DTO
        return sellerMapper.toResponse(updatedSeller);
    }

    @Override
    public void deleteSeller(Long id) throws Exception {
        Seller seller = sellerRepository.findById(id)
                .orElseThrow(()-> new Exception("cannot find seller with id"));
        sellerRepository.delete(seller);
    }

    @Override
    public SellerResponse verifyEmail(String email, String otp) throws Exception {
        Seller seller = sellerRepository.findByEmail(email);
        if (seller == null) {
            throw new Exception("Seller not found");
        }

        VerificationCode verificationCode = verificationCodeRepository.findByEmail(email);
        if (verificationCode == null || !verificationCode.getOtp().equals(otp.trim())) {
            throw new Exception("Invalid OTP");
        }

        seller.setEmailVerified(true);
        seller.setAccountStatus(AccountStatus.ACTIVE);
        Seller updatedSeller = sellerRepository.save(seller);

        // Видаляємо використаний OTP
        verificationCodeRepository.delete(verificationCode);

        return sellerMapper.toResponse(updatedSeller);
    }
}
