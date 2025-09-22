package com.example.bankcards.mapper;

import com.example.bankcards.dto.request.CardRequest;
import com.example.bankcards.dto.response.CardResponse;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.exception.mapper.MappingMatchTypeException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class CardMapper {
    public <T> T toDTO(Card card, Class<T> type) {
        if (type.equals(CardRequest.class)) {
            type.cast(new CardRequest(
                    card.getId(),
                    card.getNumber(),
                    card.getExpiration(),
                    card.getBalance()
            ));
        }
        else if(type.equals(CardResponse.class)) {
            var status = card.getStatus().stream()
                    .map(x -> x.getStatus().getValue())
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);
            type.cast(new CardResponse(
                    card.getId(),
                    card.getUser().getId(),
                    mask(card.getNumber()),
                    status,
                    card.getBalance()
            ));
        }
        else {
            throw new MappingMatchTypeException();
        }
        return null;
    }

    public Card toEntity(Object dto) {
        if (dto instanceof CardRequest given) {
            var setOfStatuses = Set.of(new CardStatus(null, CardStatus.ECardStatus.ACTIVE));
            return new Card(
                    null,
                    given.number(),
                    null,
                    given.expiration(),
                    setOfStatuses,
                    given.balance()
            );
        }
        else {
            throw new MappingMatchTypeException();
        }
    }

    private String mask(String number) {
        if (StringUtils.isNotBlank(number)) {
            var lastDigits = number.substring(12);
            return """
                   **** **** **** %s""".formatted(lastDigits);
        }
        throw new IllegalArgumentException("Invalid card number");
    }
}