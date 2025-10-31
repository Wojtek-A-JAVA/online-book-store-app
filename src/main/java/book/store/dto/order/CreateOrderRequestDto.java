package book.store.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateOrderRequestDto {
    @NotNull(message = "Shipping address can't be empty")
    private String shippingAddress;
}
