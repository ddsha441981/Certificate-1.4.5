package com.cwc.certificate.controller.versions;


import com.cwc.certificate.config.ConstantValue;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/versions")
@Tag(name = "Version ", description = "Check Versions List API")
public class VersionController {

    @GetMapping("/check-version-info")
    public ResponseEntity<String> getVersionInfo() {
        try {
            ClassPathResource resource = new ClassPathResource(ConstantValue.DEFAULT_VERSION_FILE_NAME);
            String content = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));
            return new ResponseEntity<>(content, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Error reading version file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}


