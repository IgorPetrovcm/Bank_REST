package com.example.bankcards.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "card_status")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CardStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private ECardStatus status;

    @Getter
    public enum ECardStatus {
        ACTIVE("ACTIVE"), BLOCKED("BLOCKED"), EXPIRED("EXPIRED");
        private final String value;
        ECardStatus(String value) {
            this.value = value;
        }
    }
}
