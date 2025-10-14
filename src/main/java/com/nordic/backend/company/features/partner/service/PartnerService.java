package com.nordic.backend.company.features.partner.service;

import com.nordic.backend.company.features.partner.model.PartnerModel;
import com.nordic.backend.company.features.partner.repository.PartnerRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.nordic.backend.company.features.partner.common.supabase.SupabaseFileUploadPartners;

@Service
@AllArgsConstructor
public class PartnerService {
    private final PartnerRepository partnerRepository;
    private final SupabaseFileUploadPartners supabaseFileUploadPartners;

    public ResponseEntity<?> savePartner(PartnerModel partnerModel) {
        return ResponseEntity.ok(partnerRepository.save(partnerModel));
    }

    public  ResponseEntity<?> deletePartner(Long id) {
        try {
            partnerRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    public ResponseEntity<?> updatePartner(PartnerModel partnerModel, Long id) {
        try {
            PartnerModel partner = partnerRepository.findById(id).orElseThrow(() -> new RuntimeException("Partner not found"));
            partner.setName(partnerModel.getName());
            partner.setLogourl(partnerModel.getLogourl());
            partner.setWeblink(partnerModel.getWeblink());
            return ResponseEntity.ok(partnerRepository.save(partner));
        }catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    public ResponseEntity<?> getPartnerById(Long id) {
        return ResponseEntity.ok(partnerRepository.findById(id).orElseThrow(() -> new RuntimeException("Partner not found")));
    }

    public ResponseEntity<?> getAllPartners() {
        return ResponseEntity.ok(partnerRepository.findAll());
    }

    public String uploadPartnerPhoto(MultipartFile file, Long partnerId){
        PartnerModel partnerModel = partnerRepository.findById(partnerId).orElseThrow(() -> new RuntimeException("Partner not found"));
        String logourl = supabaseFileUploadPartners.uploadFile(file);
        partnerModel.setLogourl(logourl);
        partnerRepository.save(partnerModel);
        return logourl;
    }
}

