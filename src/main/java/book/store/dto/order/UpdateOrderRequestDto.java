package book.store.dto.order;

import book.store.model.Order;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateOrderRequestDto {
    @NotNull(message = "Status can't be empty")
    private Order.Status status;
}
