package com.cs79_1.interactive_dashboard.Controller;

import com.cs79_1.interactive_dashboard.Component.AdminAccountInitializer;
import com.cs79_1.interactive_dashboard.Service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/localbackend")
//@CrossOrigin("localhost")
public class LocalBackendController {
    @Autowired
    private AdminService adminService;

    private final static Logger logger = LoggerFactory.getLogger(LocalBackendController.class);

    @GetMapping("/admin")
    public ResponseEntity<String> getAdmin() {
        String username = adminService.getSuperadminUsername();
        String password = adminService.getSuperadminPassword();

        logger.info("Admin Fetched");
        return ResponseEntity.ok(username + "\n" + password);
    }
}
