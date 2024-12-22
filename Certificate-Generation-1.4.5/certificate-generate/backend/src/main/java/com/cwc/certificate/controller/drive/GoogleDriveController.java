package com.cwc.certificate.controller.drive;


import com.cwc.certificate.model.drive.Folder;
import com.cwc.certificate.service.drive.GoogleDriveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;




/**
 * @author  Deendayal KUmawat
 * @version 1.4.3
 * @since   2024/05/02
 */


@RestController
@RequestMapping("/api/v4/drive")
@Slf4j
@CrossOrigin("*")
@Tag(name = "Google Drive", description = "Download Documents from Google Drive API")
public class GoogleDriveController {
    private final GoogleDriveService googleDriveService;

    @Autowired
    public GoogleDriveController(GoogleDriveService googleDriveService) {
        this.googleDriveService = googleDriveService;
    }

    @Operation(summary = "Retrieve all Documents list by Company wise")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Folder[].class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "500", content = {
                    @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
            })
    })

    @GetMapping("/folder")
    public ResponseEntity<Folder> findFolderByName(@RequestParam String folderName) {
        try {
            Folder rootFolder = googleDriveService.getFolderStructure();
            log.info("Fetched Folder Structure: {}", rootFolder);

            Folder foundFolder = googleDriveService.findFolderByName(rootFolder, folderName)
                    .orElseThrow(() -> new RuntimeException("Folder not found!"));

            return ResponseEntity.ok(foundFolder);
        } catch (Exception e) {
            log.error("Error finding folder by name: {}", folderName, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }


}
