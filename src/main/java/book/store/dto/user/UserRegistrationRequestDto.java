package book.store.dto.user;

import book.store.dto.user.field.match.FieldMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@FieldMatch
@Getter
public class UserRegistrationRequestDto {
    @NotBlank(message = "Cannot be blank and has to be email")
    @Email
    private String email;
    @NotBlank(message = "Cannot be blank and size from 3 to 10 characters")
    @Length(min = 3, max = 20)
    private String password;
    @NotBlank(message = "Cannot be blank and has to be the same")
    private String repeatPassword;
    @NotBlank(message = "Cannot be blank and size from 3 characters")
    @Length(min = 3)
    private String firstName;
    @NotBlank(message = "Cannot be blank and size from 3 characters")
    @Length(min = 3)
    private String lastName;
    private String shippingAddress;
}
