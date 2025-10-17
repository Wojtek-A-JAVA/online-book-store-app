package book.store.dto.cartitem;

import lombok.Data;

@Data
public class CartItemDtoWithTitle {
    private Long id;
    private Long bookId;
    private String bookTitle;
    private int quantity;
}
