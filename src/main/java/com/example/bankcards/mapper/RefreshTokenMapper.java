package com.example.bankcards.mapper;

import com.example.bankcards.dto.response.RefreshTokenResponse;
import com.example.bankcards.dto.response.UserResponse;
import com.example.bankcards.entity.RefreshToken;
import com.example.bankcards.exception.mapper.MappingMatchTypeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RefreshTokenMapper {
    private final UserMapper userMapper;

    public <T> T toDTO(RefreshToken token, Class<T> type) {
        if (type.equals(RefreshTokenResponse.class)) {
            return type.cast(new RefreshTokenResponse(
                    token.getId(),
                    token.getToken(),
                    token.getExpiration(),
                    userMapper.toDTO(token.getUser(), UserResponse.class)
            ));
        }
        else {
            throw new MappingMatchTypeException();
        }
    }

    public RefreshToken toEntity(Object dto) {
        if (dto instanceof RefreshTokenResponse given) {
            var user = userMapper.toEntity(given.user());
            return new RefreshToken(
                    given.id(),
                    given.token(),
                    given.expiration(),
                    user
            );
        }
        else {
            throw new MappingMatchTypeException();
        }
    }
}