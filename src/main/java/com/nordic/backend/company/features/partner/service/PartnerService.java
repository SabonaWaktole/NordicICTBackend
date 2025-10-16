package com.nordic.backend.company.features.partner.service;

import com.nordic.backend.company.features.partner.model.PartnerModel;
import com.nordic.backend.company.features.partner.repository.PartnerRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nordic.backend.company.Common.JWT.service.JwtService;
import com.nordic.backend.company.Common.excetions.UnauthorizedException;
import com.nordic.backend.company.Common.utils.CommonJWTChecker;
import com.nordic.backend.company.features.partner.common.supabase.SupabaseFileUploadPartners;

@Service
@AllArgsConstructor
public class PartnerService {
    private final PartnerRepository partnerRepository;
    private final SupabaseFileUploadPartners supabaseFileUploadPartners;
    private final CommonJWTChecker commonJWTChecker;
    private final JwtService jwtService;

    public ResponseEntity<?> savePartner(PartnerModel partnerModel, String token, String email) {
        try {
            commonJWTChecker.validateToken(jwtService.getRoleFromToken(token), token, partnerModel.getEmail());
            return ResponseEntity.ok(partnerRepository.save(partnerModel));
        } catch (UnauthorizedException e) {
            throw new UnauthorizedException(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    public ResponseEntity<?> deletePartner(Long id, String token, String email) {
        try {
            commonJWTChecker.validateToken(jwtService.getRoleFromToken(token), token, email);
            partnerRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    public ResponseEntity<?> updatePartner(PartnerModel partnerModel, Long id, String token, String email) {
        try {
            commonJWTChecker.validateToken(jwtService.getRoleFromToken(token), token, email);
            PartnerModel partner = partnerRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Partner not found"));
            partner.setName(partnerModel.getName());
            partner.setLogourl(partnerModel.getLogourl());
            partner.setWeblink(partnerModel.getWeblink());
            return ResponseEntity.ok(partnerRepository.save(partner));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    public ResponseEntity<?> getPartnerById(Long id, String token, String email) {
        return ResponseEntity
                .ok(partnerRepository.findById(id).orElseThrow(() -> new RuntimeException("Partner not found")));
    }

    public ResponseEntity<?> getAllPartners(String token, String email) {
        try {
            commonJWTChecker.validateToken(jwtService.getRoleFromToken(token), token, email);
            return ResponseEntity.ok(partnerRepository.findAll());
        } catch (Exception e) {
            throw new UnauthorizedException(e.getMessage());
        }
    }

    public String uploadPartnerPhoto(MultipartFile file, Long partnerId, String token, String email) {
        try {
            commonJWTChecker.validateToken(jwtService.getRoleFromToken(token), token, email);
            PartnerModel partnerModel = partnerRepository.findById(partnerId)
                    .orElseThrow(() -> new RuntimeException("Partner not found"));
            String logourl = supabaseFileUploadPartners.uploadFile(file);
            partnerModel.setLogourl(logourl);
            partnerRepository.save(partnerModel);
            return logourl;
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
