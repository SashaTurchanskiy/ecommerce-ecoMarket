package com.ecoMarket.controller;

import com.ecoMarket.dtos.request.LoginRequest;
import com.ecoMarket.dtos.request.SellerRequest;
import com.ecoMarket.dtos.response.AuthResponse;
import com.ecoMarket.dtos.response.SellerResponse;
import com.ecoMarket.model.VerificationCode;
import com.ecoMarket.repository.VerificationCodeRepository;
import com.ecoMarket.service.AuthService;
import com.ecoMarket.service.SellerService;
import com.ecoMarket.service.impl.EmailService;
import com.ecoMarket.utils.OtpUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sellers")
@RequiredArgsConstructor
public class SellerController {

    private final SellerService sellerService;
    private final AuthService authService;
    private final VerificationCodeRepository verificationCodeRepository;
    private final EmailService emailService;


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginSeller(@RequestBody LoginRequest req) {
        String otp = req.getOtp();
        String email = req.getEmail();

        req.setEmail("seller_" + email);
        AuthResponse authResponse = authService.signIn(req);
        return ResponseEntity.ok(authResponse);

    }
    @GetMapping("/profile")
    public ResponseEntity<SellerResponse> getByProfile(@RequestHeader("Authorization") String jwt) throws Exception {
        return ResponseEntity.ok(sellerService.getSellerProfile(jwt));
    }
    @PatchMapping("/verify/{otp}")
    public ResponseEntity<SellerResponse> verifySellerEmail(@PathVariable String otp)
            throws  Exception {

        VerificationCode verificationCode = verificationCodeRepository.findByOtp(otp);

        if (verificationCode == null || !verificationCode.getOtp().equals(otp)){
            throw new Exception("Invalid OTP");
        }

        SellerResponse seller = sellerService.verifyEmail(verificationCode.getEmail(), otp);

        return new ResponseEntity<>(seller, HttpStatus.OK);
    }

    @PostMapping("/create/seller")
    public ResponseEntity<SellerResponse> createSeller(@RequestBody SellerRequest req) throws Exception, MessagingException {
        SellerResponse savedSeller = sellerService.createSeller(req);

        String otp = OtpUtil.generateOtp();

        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setOtp(otp);
        verificationCode.setEmail(req.getEmail());
        verificationCodeRepository.save(verificationCode);

        String subject = "Email Verification Code";
        String text = "Welcome to our platform. Veify your email using this link: ";
        String frontEnd_url = "http://localhost:3000/verify-seller/";
        emailService.sendVerificationOtpEmail(req.getEmail(), verificationCode.getOtp(), subject, text + frontEnd_url);

        return new ResponseEntity<>(savedSeller, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SellerResponse> getSellerById(@PathVariable Long id) throws Exception {
        SellerResponse seller = sellerService.getSellerById(id);
        return new ResponseEntity<>(seller, HttpStatus.OK);
    }
//    @GetMapping("/report")
//    public ResponseEntity<SellerReport> getSellerReport(
//            @RequestHeader("Authorization") String jwt) throws Exception {
//        {
//            Seller seller = sellerService.getSellerProfile(jwt);
//            SellerReport report = sellerReportService.getSellerReport(seller);
//
//            return new ResponseEntity<>(report, HttpStatus.OK);
//        }

    @GetMapping("/all")
    public ResponseEntity<List<SellerResponse>> getAllSellers() {
        return ResponseEntity.ok(sellerService.getAllSellers());
    }

    @PatchMapping()
    public ResponseEntity<SellerResponse> updateSeller(
            @RequestHeader ("Authorization") String jwt,
            @RequestBody SellerRequest req) throws Exception {

        SellerResponse profile = sellerService.getSellerProfile(jwt);
        SellerResponse updatedSeller = sellerService.updateSellerProfile(profile.getId(), req);
        return ResponseEntity.ok(updatedSeller);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeller(@PathVariable Long id) throws Exception {

        sellerService.deleteSeller(id);
        return ResponseEntity.noContent().build();
    }

}
