package book.store.dto.shoppingcart;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class UpdateCartItemRequestDto {
    @Min(value = 1, message = "Quantity must greater than zero")
    private int quantity;
}
