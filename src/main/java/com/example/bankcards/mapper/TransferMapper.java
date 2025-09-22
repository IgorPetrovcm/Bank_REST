package com.example.bankcards.mapper;

import com.example.bankcards.dto.request.TransferRequest;
import com.example.bankcards.dto.response.TransferResponse;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.Transfer;
import com.example.bankcards.exception.entity.CardNotFoundException;
import com.example.bankcards.exception.mapper.MappingMatchTypeException;
import com.example.bankcards.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class TransferMapper {
    private final CardRepository cardRepository;

    public <T> T toDTO(Transfer transfer, Class<T> type) {
        if (type.equals(TransferResponse.class)) {
            var fromBalance = getBalance(transfer.getFromCard().getId());
            var toBalance = getBalance(transfer.getToCard().getId());
            return type.cast(new TransferResponse(
                    fromBalance,
                    toBalance,
                    transfer.getAmount(),
                    transfer.getCreatedAt()
            ));
        }
        else {
            throw new MappingMatchTypeException();
        }
    }

    public Transfer toEntity(Object dto) {
        if (dto instanceof TransferRequest given) {
            var fromCard = findCard(given.fromId());
            var toCard = findCard(given.toId());
            return new Transfer(
                    null,
                    fromCard,
                    toCard,
                    given.amount(),
                    null
            );
        }
        else {
            throw new MappingMatchTypeException();
        }
    }

    private BigDecimal getBalance(Long cardId) {
        return cardRepository.findById(cardId)
                .orElseThrow(CardNotFoundException::new).getBalance();
    }

    private Card findCard(Long cardId) {
        return cardRepository.findById(cardId)
                .orElseThrow(CardNotFoundException::new);
    }
}
