package com.nordic.backend.company.features.partner.controller;

import com.nordic.backend.company.features.partner.service.PartnerService;
import com.nordic.backend.company.features.partner.model.PartnerModel;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/partners")
public class PartnerController {
    private final PartnerService partnerService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllPartners(
            @RequestHeader("Authorization") String token,
            @RequestParam String email) {
        return partnerService.getAllPartners(token, email);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPartnerById(
            @RequestHeader("Authorization") String token,
            @RequestParam String email,
            @PathVariable Long id) {
        return partnerService.getPartnerById(id, token, email);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updatePartnerById(
            @RequestHeader("Authorization") String token,
            @RequestParam String email,
            @PathVariable Long id,
            @RequestBody PartnerModel partnerModel) {
        return partnerService.updatePartner(partnerModel, id, token, email);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deletePartnerById(
            @RequestHeader("Authorization") String token,
            @RequestParam String email,
            @PathVariable Long id) {
        return partnerService.deletePartner(id, token, email);
    }

    @PostMapping("/new/add")
    public ResponseEntity<?> savePartner(
            @RequestHeader("Authorization") String token,
            @RequestParam String email,
            @RequestBody PartnerModel partnerModel) {
        return partnerService.savePartner(partnerModel, token, email);
    }

    @PostMapping("/upload-photo")
    public ResponseEntity<String> uploadPhoto(
            @RequestBody MultipartFile file,
            @RequestHeader("Authorization") String token,
            @RequestParam String email,
            @RequestParam Long partnerId) {

        String logourl = partnerService.uploadPartnerPhoto(file, partnerId, token, email);
        return ResponseEntity.ok(logourl);
    }

}
