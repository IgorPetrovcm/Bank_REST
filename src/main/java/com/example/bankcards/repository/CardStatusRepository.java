package com.example.bankcards.repository;

import com.example.bankcards.entity.CardStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardStatusRepository extends JpaRepository<CardStatus, Long> {
    Optional<CardStatus> findByStatus(CardStatus.ECardStatus status);
}
