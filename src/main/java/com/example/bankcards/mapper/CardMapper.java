package com.example.bankcards.mapper;

import com.example.bankcards.dto.request.CardRequest;
import com.example.bankcards.dto.response.CardResponse;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.exception.entity.CardStatusNotFoundException;
import com.example.bankcards.exception.mapper.MappingMatchTypeException;
import com.example.bankcards.repository.CardStatusRepository;
import com.example.bankcards.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.time.YearMonth;
import java.time.ZoneOffset;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class CardMapper {
    private final RoleRepository roleRepository;
    private final CardStatusRepository cardStatusRepository;

    public <T> T toDTO(Card card, Class<T> type) {
        if (type.equals(CardRequest.class)) {
            var yearMonth = YearMonth.from(card.getExpiration()
                            .atOffset(ZoneOffset.UTC).toLocalDate());
            type.cast(new CardRequest(
                    card.getId(),
                    card.getNumber(),
                    yearMonth,
                    card.getBalance()
            ));
        }
        else if(type.equals(CardResponse.class)) {
            var status = card.getStatus();
            var statusValue = status.stream()
                    .map(x -> x.getStatus().getValue())
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);
            return type.cast(new CardResponse(
                    card.getId(),
                    card.getUser().getId(),
                    mask(card.getNumber()),
                    statusValue,
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
            var cardStatus = cardStatusRepository.findByStatus(CardStatus.ECardStatus.ACTIVE)
                    .orElseThrow(CardStatusNotFoundException::new);
            var expiration = given.expiration().atDay(1).atStartOfDay().toInstant(ZoneOffset.UTC);
            return new Card(
                    null,
                    given.number(),
                    null,
                    expiration,
                    Set.of(cardStatus),
                    given.balance(),
                    false
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