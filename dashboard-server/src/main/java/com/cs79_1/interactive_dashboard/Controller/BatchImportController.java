package com.cs79_1.interactive_dashboard.Controller;

import com.cs79_1.interactive_dashboard.Service.BatchImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ops/import")
public class BatchImportController {

    @Autowired
    private BatchImportService batchImportService;
}
