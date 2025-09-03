package book.store.mapper;

import book.store.dto.user.UserRegistrationRequestDto;
import book.store.dto.user.UserResponseDto;
import book.store.model.User;

public interface UserMapper {

    User toUser(UserRegistrationRequestDto requestDto);

    UserResponseDto toUserResponseDto(User user);
}
