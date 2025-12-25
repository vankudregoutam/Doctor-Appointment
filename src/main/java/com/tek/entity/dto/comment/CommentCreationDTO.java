package com.tek.entity.dto.comment;

import com.tek.entity.User;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
public class CommentCreationDTO {
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
    private String comment;
}
