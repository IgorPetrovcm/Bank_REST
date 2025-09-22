package com.example.bankcards.service;

import com.example.bankcards.dto.request.TransferRequest;
import com.example.bankcards.dto.response.TransferResponse;
import com.example.bankcards.exception.entity.CardNotFoundException;
import com.example.bankcards.mapper.TransferMapper;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.TransferRepository;
import com.example.bankcards.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class TransferService {
    private final TransferRepository transferRepository;
    private final UserRepository userRepository;
    private final CardRepository cardRepository;

    private final TransferMapper transferMapper;

    public TransferResponse init(TransferRequest request) {
        var transfer = transferMapper.toEntity(request);

        var fromCard = cardRepository.findById(transfer.getFromCard().getId())
                .orElseThrow(CardNotFoundException::new);
        var toCard = cardRepository.findById(transfer.getToCard().getId())
                .orElseThrow(CardNotFoundException::new);

        if (fromCard.getBalance().compareTo(transfer.getAmount()) < 0) {
            throw new IllegalArgumentException();
        }

        fromCard.setBalance(fromCard.getBalance().subtract(transfer.getAmount()));
        toCard.setBalance(toCard.getBalance().add(transfer.getAmount()));
        cardRepository.saveAll(Set.of(fromCard, toCard));

        return transferMapper.toDTO(transferRepository.save(transfer), TransferResponse.class);
    }
}
