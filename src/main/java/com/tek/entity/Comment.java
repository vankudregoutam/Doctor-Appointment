package com.tek.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Inheritance
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "commentId")
    @SequenceGenerator(name = "commentId", initialValue = 501, allocationSize = 1)
    private Integer commentId;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
    private String comment;
}
