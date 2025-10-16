package com.cs79_1.interactive_dashboard.Service;


import com.cs79_1.interactive_dashboard.Entity.User;
import com.cs79_1.interactive_dashboard.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
public class AvatarService {
    @Value("${file.upload.path:/var/www/uploads/avatars/}")
    String UPLOAD_DIR;

    @Autowired
    UserRepository userRepository;

    @Transactional
    public String saveAvatar(MultipartFile file, Long userId) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("empty file");
        }


        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFileName = userId + "_" + System.currentTimeMillis() + extension;

        Path targetPath = Paths.get(UPLOAD_DIR + newFileName);

        Files.write(targetPath, file.getBytes());

        String newAvatarUrl = "/uploads/avatars/" + newFileName;



        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isEmpty()) {

            throw new RuntimeException("User not found with ID: " + userId);
        }

        User user = userOptional.get();


        user.setAvatarUrl(newAvatarUrl);

        userRepository.save(user);

        return newAvatarUrl;
    }
}
