package com.tek.controller;

import com.tek.constants.CommentConstants;
import com.tek.entity.dto.ResponseDTO;
import com.tek.entity.dto.comment.CommentResponseDTO;
import com.tek.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {
    private final UserService userService;

    @GetMapping("/fetchAllByAdmin")
    public ResponseEntity<ResponseDTO> fetchAllComments() {
        List<CommentResponseDTO> commentResponseDTO = userService.fetchAllCommentsAdmin();

        return ResponseEntity.status(HttpStatus.FOUND)
                .body(new ResponseDTO(CommentConstants.STATUS_200, CommentConstants.MESSAGE_200_FOUND, commentResponseDTO));
    }

    @GetMapping("/fetchAll/{userId}")
    public ResponseEntity<ResponseDTO> fetchAllComment(@Valid @PathVariable Integer userId) {
        List<CommentResponseDTO> comments = userService.findByUserIdAdmin(userId);

        return ResponseEntity.status(HttpStatus.FOUND)
                .body(new ResponseDTO(CommentConstants.STATUS_200, CommentConstants.MESSAGE_200_FOUND, comments));
    }
}
