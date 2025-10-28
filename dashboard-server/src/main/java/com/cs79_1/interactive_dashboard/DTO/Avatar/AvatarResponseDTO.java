package com.cs79_1.interactive_dashboard.DTO.Avatar;

import lombok.Data;

@Data
public class AvatarResponseDTO {
    private Long id;
    private String avatarUrl;

    public AvatarResponseDTO(Long id, String avatarUrl) {
        this.id = id;
        this.avatarUrl = avatarUrl;
    }
}
