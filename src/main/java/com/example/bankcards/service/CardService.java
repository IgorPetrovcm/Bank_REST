package com.example.bankcards.service;

import com.example.bankcards.dto.request.CardRequest;
import com.example.bankcards.dto.response.CardResponse;
import com.example.bankcards.exception.service.UserNotFoundException;
import com.example.bankcards.mapper.CardMapper;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardService {
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
}