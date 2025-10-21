package book.store.mapper;

import book.store.config.MapperConfig;
import book.store.dto.user.UserRegistrationRequestDto;
import book.store.dto.user.UserResponseDto;
import book.store.model.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    @BeanMapping(ignoreByDefault = true)
    User toEntity(UserRegistrationRequestDto requestDto);

    UserResponseDto toDto(User user);
}
