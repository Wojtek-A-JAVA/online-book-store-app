package book.store.dto.user;

import book.store.dto.user.field.match.PasswordsMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@PasswordsMatch
@Getter
public class UserRegistrationRequestDto {
    @NotBlank(message = "Cannot be blank")
    @Email(message = "Must be a valid email")
    private String email;
    @NotBlank(message = "Cannot be blank")
    @Size(min = 3, max = 20, message = "Min size is 3 and max size is 20 characters")
    private String password;
    @NotBlank(message = "Cannot be blank and has to be the same")
    private String repeatPassword;
    @NotBlank(message = "Cannot be blank")
    @Size(min = 3, message = "Min size is 3 characters")
    private String firstName;
    @NotBlank(message = "Cannot be blank")
    @Size(min = 3, message = "Min size is 3 characters")
    private String lastName;
    private String shippingAddress;
}
