package com.example.bankcards.repository;

import com.example.bankcards.entity.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface CardRepository extends JpaRepository<Card, Long> {
    Page<Card> findAll(Pageable pageable);
    Page<Card> findAllByUserId(Long userId, Pageable pageable);

    Set<Card> findByIsBlockRequested(Boolean isBlockRequested);
}