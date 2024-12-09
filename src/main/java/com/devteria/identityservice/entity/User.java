package com.devteria.identityservice.entity;

import java.time.LocalDate;
import java.util.Set;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // RANDOM ID
    String id;

    String username;
    String password;
    String firstName;
    String lastName;
    LocalDate dob;

    @ManyToMany
    Set<Role> roles; // Set # List ở chỗ các phần tử trong set là unique.
}
