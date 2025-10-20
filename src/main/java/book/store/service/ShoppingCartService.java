package book.store.service;

import book.store.dto.cartitem.CartItemDto;
import book.store.dto.shoppingcart.CreateShoppingCartRequestDto;
import book.store.dto.shoppingcart.ShoppingCartDto;
import book.store.dto.shoppingcart.UpdateCartItemRequestDto;
import book.store.model.User;

public interface ShoppingCartService {

    ShoppingCartDto getShoppingCart(Long userId);

    CartItemDto addItemToCart(User user, CreateShoppingCartRequestDto shoppingCartRequestDto);

    CartItemDto updateItemQuantityById(Long id, UpdateCartItemRequestDto itemRequestDto);

    void deleteBookByIdFromCart(Long id);
}
