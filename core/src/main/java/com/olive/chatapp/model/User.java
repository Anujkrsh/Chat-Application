package com.olive.chatapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users") // Optional: customize the table name
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(unique = true, nullable = false, length = 50) // Optional: customize column properties
    private String username;

    @NotNull
    @Column(nullable = false)
    private String password;

    @NotNull
    @Email
    @Column(nullable = false, unique = false, length = 100) // Email should typically be unique
    private String email;

    @NotNull
    @Column(unique = true, length = 15) // Optional: restrict phone number length
    private String phoneNumber;

    @NotNull
    @Column(nullable = false, length = 100,columnDefinition = "varchar(100) default 'Unknown'") // Optional: restrict name length
    private String name;
}
