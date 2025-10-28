package com.cs79_1.interactive_dashboard.Controller;

import com.cs79_1.interactive_dashboard.Component.AdminAccountInitializer;
import com.cs79_1.interactive_dashboard.Service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;

@RestController
@RequestMapping("/localbackend")
@Slf4j
//@CrossOrigin("localhost")
public class LocalBackendController {
    @Autowired
    private AdminService adminService;

    @Value("${file.upload.path:/var/www/uploads/avatars/}")
    private String uploadPath;
    
    @GetMapping("/admin")
    public ResponseEntity<String> getAdmin() {
        String username = adminService.getSuperadminUsername();
        String password = adminService.getSuperadminPassword();

        log.info("Admin Fetched");
        return ResponseEntity.ok(username + "\n" + password);
    }

    @GetMapping("/check-file/{filename}")
    public ResponseEntity<?> checkFile(@PathVariable String filename) {
        File file = new File(uploadPath + filename);

        return ResponseEntity.ok(java.util.Map.of(
                "uploadPath", uploadPath,
                "fullPath", file.getAbsolutePath(),
                "exists", file.exists(),
                "canRead", file.canRead(),
                "isFile", file.isFile()
        ));
    }

    @GetMapping("/download/{filename}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        try {
            File file = new File(uploadPath + filename);
            if (!file.exists()) {
                return ResponseEntity.notFound().build();
            }

            Resource resource = new FileSystemResource(file);
            return ResponseEntity.ok()
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
