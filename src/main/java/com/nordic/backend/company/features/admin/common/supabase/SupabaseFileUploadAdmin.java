package com.nordic.backend.company.features.admin.common.supabase;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class SupabaseFileUploadAdmin {

    private final String supabaseUrl;
    private static final List<String> ALLOWED_DOMAINS = List.of(
            ".supabase.co",  // Only allow subdomains of supabase.co
            ".supabase.in"   // Only allow subdomains of supabase.in
    );
    
    // List of allowed protocols
    private static final List<String> ALLOWED_PROTOCOLS = List.of("https");

    public SupabaseFileUploadAdmin(@Value("${supabase.url}") String supabaseUrl) {
        validateSupabaseUrl(supabaseUrl);
        this.supabaseUrl = supabaseUrl.endsWith("/") 
            ? supabaseUrl.substring(0, supabaseUrl.length() - 1) 
            : supabaseUrl;
    }

    private void validateSupabaseUrl(String url) {
        if (!StringUtils.hasText(url)) {
            throw new IllegalArgumentException("Supabase URL cannot be empty");
        }

        try {
            // Parse the URL
            URI uri = new URI(url);
            String scheme = uri.getScheme();
            String host = uri.getHost();
            
            // Validate protocol
            if (scheme == null || !ALLOWED_PROTOCOLS.contains(scheme.toLowerCase())) {
                throw new IllegalArgumentException("Invalid protocol. Only HTTPS is allowed");
            }
            
            // Validate host
            if (host == null) {
                throw new IllegalArgumentException("Invalid URL: No host specified");
            }
            
            // Check against allowed domains
            if (!isAllowedDomain(host)) {
                throw new IllegalArgumentException("Invalid Supabase URL domain. Only *.supabase.co and *.supabase.in are allowed");
            }
            
            // Validate port (if specified)
            if (uri.getPort() != -1 && uri.getPort() != 443) {
                throw new IllegalArgumentException("Invalid port. Only port 443 is allowed for HTTPS");
            }

        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid Supabase URL format", e);
        }
    }

        private boolean isAllowedDomain(String host) {
        if (host == null) {
            return false;
        }
        // Convert to lowercase for case-insensitive comparison
        String lowerHost = host.toLowerCase();
        // Check if the host ends with any of the allowed domains
        return ALLOWED_DOMAINS.stream()
                .map(String::toLowerCase)
                .anyMatch(lowerHost::endsWith);
    }
    
    private String sanitizeExtension(String extension) {
        if (extension == null) return "";
        // Only allow alphanumeric characters in extension
        return extension.replaceAll("[^a-zA-Z0-9]", "");
    }
    
//    private String sanitizeFilename(String filename) {
//        if (filename == null) return "";
//        // Remove any path traversal characters and other potentially dangerous characters
//        return filename.replaceAll("[^a-zA-Z0-9.-]", "_");
//    }

    @Value("${supabase.key}")
    private String supabaseKey;

    private static final String ALLOWED_SUPABASE_DOMAIN = "supabase.co";

    public String uploadFile(MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();

            // Sanitize and validate supabase URL
            URI baseUri = new URI(supabaseUrl);
            String host = baseUri.getHost();
            if (host == null || !host.endsWith(ALLOWED_SUPABASE_DOMAIN)) {
                throw new SecurityException("Invalid Supabase URL. Possible SSRF attempt blocked.");
            }

            // Prepare headers
            HttpHeaders headers = new HttpHeaders();
            headers.set("apikey", supabaseKey);
            headers.set("Authorization", "Bearer " + supabaseKey);

            HttpEntity<byte[]> requestEntity = new HttpEntity<>(bytes, headers);

            // Generate filename safely
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = sanitizeExtension(originalFilename.substring(originalFilename.lastIndexOf('.') + 1));
            }
            String fileName = UUID.randomUUID() + (extension.isEmpty() ? "" : "." + extension);

            // Use UriComponentsBuilder for safe URL construction
            URI uploadUri;
            try {
                uploadUri = UriComponentsBuilder.fromHttpUrl(supabaseUrl)
                    .path("/storage/v1/object/AdminPhotos/"+fileName)
                    .queryParam("bucket", "AdminPhotos")
                    .queryParam("name", fileName)
                    .queryParam("bucketId", "AdminPhotos")
                    .build()
                    .toUri();
                
                // Validate the constructed URI
                validateSupabaseUrl(uploadUri.toString());
            } catch (Exception e) {
                throw new IllegalArgumentException("Failed to construct upload URL: " + e.getMessage(), e);
            }

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(
                    uploadUri,
                    HttpMethod.PUT,
                    requestEntity,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                return baseUri.resolve("/storage/v1/object/public/AdminPhotos/" + fileName).toString();
            } else {
                throw new RuntimeException("Failed to upload file. Status: " + response.getStatusCode());
            }

        } catch (URISyntaxException e) {
            throw new RuntimeException("Invalid Supabase base URL format.", e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Supabase upload error: check credentials, bucket, and file.", e);
        }
    }
}
