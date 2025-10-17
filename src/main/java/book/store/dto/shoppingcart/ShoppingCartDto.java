package book.store.dto.shoppingcart;

import book.store.dto.cartitem.CartItemDtoWithTitle;
import java.util.Set;
import lombok.Data;

@Data
public class ShoppingCartDto {
    private Long id;
    private Long userId;
    private Set<CartItemDtoWithTitle> cartItems;
}


