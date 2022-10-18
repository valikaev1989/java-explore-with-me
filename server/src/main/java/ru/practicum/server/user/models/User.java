package ru.practicum.server.user.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Entity
@Data
@Table(name = "users")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "users_name", length = 50)
    @NotBlank
    private String name;

    @Column(name = "users_email", unique = true, length = 50)
    @Email
    @NotBlank
    private String email;
}