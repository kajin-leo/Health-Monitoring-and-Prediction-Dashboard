package com.cs79_1.interactive_dashboard.DTO.BatchImport;

import lombok.Data;

import java.nio.file.Path;

@Data
public class FileInfo {
    private final Path tempPath;
    private final String originalName;

    public FileInfo(Path tempPath, String originalName) {
        this.tempPath = tempPath;
        this.originalName = originalName;
    }
}
