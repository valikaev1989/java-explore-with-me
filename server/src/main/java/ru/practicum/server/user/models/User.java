package ru.practicum.server.user.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "users")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "users_name", length = 50)
    @NotBlank
    private String name;

    @Column(name = "users_email", length = 50)
    @Email
    @NotBlank
    private String email;
    @Column(name = "permission_to_comment")
    private Boolean permissionToComment = true;
    private LocalDateTime blockComments;
}