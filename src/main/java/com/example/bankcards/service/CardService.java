package com.example.bankcards.service;

import com.example.bankcards.dto.request.CardRequest;
import com.example.bankcards.dto.response.CardBalanceResponse;
import com.example.bankcards.dto.response.CardResponse;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.exception.entity.CardNotFoundException;
import com.example.bankcards.exception.entity.UserNotFoundException;
import com.example.bankcards.mapper.CardMapper;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.CardStatusRepository;
import com.example.bankcards.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardService {
    private final UserService userService;

    private final CardStatusRepository cardStatusRepository;
    private final CardRepository cardRepository;
    private final UserRepository userRepository;

    private final CardMapper cardMapper;

    public CardResponse createCard(CardRequest cardRequest) {
        var user = userRepository.findById(cardRequest.userId())
                .orElseThrow(UserNotFoundException::new);

        var card = cardMapper.toEntity(cardRequest);
        card.setUser(user);
        return cardMapper.toDTO(cardRepository.save(card), CardResponse.class);
    }

    public Set<CardResponse> getAllCards(Pageable pageable) {
        var cards = cardRepository.findAll(pageable);
        return cards.stream()
                .map(x -> cardMapper.toDTO(x, CardResponse.class))
                .collect(Collectors.toSet());
//        return cardRepository.findAll().stream()
//                .map(x -> cardMapper.toDTO(x, CardResponse.class))
//                .collect(Collectors.toSet());
    }

    public CardResponse cardBlock(Long cardId) {
        var card = cardRepository.findById(cardId).orElseThrow(CardNotFoundException::new);
        var blockedStatus = new CardStatus();
        blockedStatus.setStatus(CardStatus.ECardStatus.BLOCKED);
        card.setStatus(Set.of(blockedStatus));
        if (card.getIsBlockRequested()) card.setIsBlockRequested(false);
        return cardMapper.toDTO(cardRepository.save(card), CardResponse.class);
    }

    public CardResponse cardActivate(Long cardId) {
        var card = cardRepository.findById(cardId).orElseThrow(CardNotFoundException::new);
        var activeStatus = new CardStatus();
        activeStatus.setStatus(CardStatus.ECardStatus.ACTIVE);
        card.setStatus(Set.of(activeStatus));
        return cardMapper.toDTO(cardRepository.save(card), CardResponse.class);
    }

    public void deleteCard(Long cardId) {
        cardRepository.deleteById(cardId);
    }

    public CardBalanceResponse getBalance(Long cardId) {
        var user = userService.getCurrentAuthenticatedUser();

        var card = user.getCards().stream()
                .filter(x -> x.getId().equals(cardId))
                .findFirst()
                .orElseThrow(CardNotFoundException::new);
        return new CardBalanceResponse(card.getId(), card.getBalance());
    }

    public Set<CardResponse> getAllCardsForCurrentUser(Pageable pageable) {
        var user = userService.getCurrentAuthenticatedUser();
        return cardRepository.findAllByUserId(user.getId(), pageable).stream()
                .map(x -> cardMapper.toDTO(x, CardResponse.class))
                .collect(Collectors.toSet());
    }

    public CardResponse requestToBlockCard(Long cardId) {
        var user = userService.getCurrentAuthenticatedUser();
        var card = user.getCards().stream()
                .filter(x -> x.getId().equals(cardId))
                .findFirst()
                .orElseThrow(CardNotFoundException::new);
        card.setIsBlockRequested(true);
        return cardMapper.toDTO(cardRepository.save(card), CardResponse.class);
    }

    public Set<CardResponse> getAllCardsBlockRequested() {
        return cardRepository.findByIsBlockRequested(true).stream()
                .map(x -> cardMapper.toDTO(x, CardResponse.class))
                .collect(Collectors.toSet());
    }
}