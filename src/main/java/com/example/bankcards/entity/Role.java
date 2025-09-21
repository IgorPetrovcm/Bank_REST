package com.example.bankcards.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "roles")
@Getter
@Setter
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private ERole name;

    @Getter
    public enum ERole {
        USER("USER"), ADMIN("ADMIN");

        private String value;
        ERole(String value) {
            this.value = value;
        }
    }
}

