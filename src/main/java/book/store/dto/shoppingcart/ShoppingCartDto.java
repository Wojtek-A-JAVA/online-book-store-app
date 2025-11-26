package book.store.dto.shoppingcart;

import book.store.dto.cartitem.CartItemDtoWithTitle;
import java.util.Set;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ShoppingCartDto {
    private Long id;
    private Long userId;
    private Set<CartItemDtoWithTitle> cartItems;
}


