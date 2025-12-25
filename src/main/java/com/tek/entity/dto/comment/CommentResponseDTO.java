package com.tek.entity.dto.comment;

import com.tek.entity.User;
import com.tek.entity.dto.user.UserResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class CommentResponseDTO {
    private Integer commentId;
    private UserResponseDTO user;
    private String comment;
}
