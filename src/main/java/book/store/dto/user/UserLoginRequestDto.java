package book.store.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserLoginRequestDto(
        @NotBlank(message = "Cannot be blank")
        @Email(message = "Must be a valid email")
        String email,
        @NotBlank(message = "Cannot be blank")
        @Size(min = 3, max = 20, message = "Min size is 3 and max size is 20 characters")
        String password
) {
}
