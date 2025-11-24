package book.store.dto.shoppingcart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateShoppingCartRequestDto {
    @NotNull(message = "Book id cannot be null")
    @Min(value = 1, message = "Book id must greater than zero")
    private Long bookId;
    @Min(value = 1, message = "Quantity must greater than zero")
    private int quantity;
}
