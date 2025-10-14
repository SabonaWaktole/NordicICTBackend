package com.nordic.backend.company.features.partner.controller;


import com.nordic.backend.company.features.partner.service.PartnerService;
import com.nordic.backend.company.features.partner.model.PartnerModel;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "http://127.0.0.1:5500") // or "*" for all
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/partners")
public class PartnerController {
    private final PartnerService partnerService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllPartners() {
        return partnerService.getAllPartners();
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getPartnerById(@PathVariable Long id) {
        return partnerService.getPartnerById(id);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updatePartnerById(@PathVariable Long id, @RequestBody PartnerModel partnerModel) {
        return partnerService.updatePartner(partnerModel,id);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deletePartnerById(@PathVariable Long id) {
        return partnerService.deletePartner(id);
    }

    @PostMapping("/new/add")
    public ResponseEntity<?> savePartner(@RequestBody PartnerModel partnerModel) {
        return partnerService.savePartner(partnerModel);
    }

    @PostMapping("/upload-photo")
    public ResponseEntity<String> uploadPhoto(
            @RequestParam("file") MultipartFile file,
            @RequestParam("partnerId") Long partnerId) {

        String logourl = partnerService.uploadPartnerPhoto(file, partnerId);
        return ResponseEntity.ok(logourl);
    }

}
