package book.store.service.impl;

import book.store.dto.cartitem.CartItemDto;
import book.store.dto.shoppingcart.CreateShoppingCartRequestDto;
import book.store.dto.shoppingcart.ShoppingCartDto;
import book.store.dto.shoppingcart.UpdateCartItemRequestDto;
import book.store.exception.EntityNotFoundException;
import book.store.mapper.CartItemMapper;
import book.store.mapper.ShoppingCartMapper;
import book.store.model.Book;
import book.store.model.CartItem;
import book.store.model.ShoppingCart;
import book.store.model.User;
import book.store.repository.book.BookRepository;
import book.store.repository.cartitem.CartItemRepository;
import book.store.repository.shoppingcart.ShoppingCartRepository;
import book.store.repository.user.UserRepository;
import book.store.service.ShoppingCartService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final BookRepository bookRepository;
    private final CartItemRepository cartItemRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public ShoppingCartDto getShoppingCart(Long userId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId).orElseThrow(
                () -> new EntityNotFoundException("User with id " + userId
                        + " doesn't have shopping cart")
        );
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    @Transactional
    public CartItemDto addItemToCart(
            User user, CreateShoppingCartRequestDto shoppingCartRequestDto) {

        Book book = bookRepository.findById(shoppingCartRequestDto.getBookId()).orElseThrow(
                () -> new EntityNotFoundException("Item with id "
                        + shoppingCartRequestDto.getBookId() + " not found"));

        Optional<ShoppingCart> optionalShoppingCart =
                shoppingCartRepository.findByUserId(user.getId());
        ShoppingCart shoppingCart = optionalShoppingCart.orElseGet(
                () -> {
                    ShoppingCart cart = new ShoppingCart();
                    cart.setUser(user);
                    return shoppingCartRepository.save(cart);
                });

        Optional<CartItem> optionalCartItem =
                cartItemRepository.findByShoppingCartIdAndBookId(
                        shoppingCart.getId(), book.getId());

        CartItem cartItem = optionalCartItem.orElseGet(
                () -> {
                    CartItem item = new CartItem();
                    item.setBook(book);
                    item.setShoppingCart(shoppingCart);
                    item.setQuantity(0);
                    return item;
                });

        cartItem.setQuantity(cartItem.getQuantity()
                + shoppingCartRequestDto.getQuantity());
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    @Override
    @Transactional
    public CartItemDto updateItemQuantityById(Long id, UpdateCartItemRequestDto itemRequestDto) {
        CartItem cartItem = findingCartItemById(id);
        cartItem.setQuantity(itemRequestDto.getQuantity());
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    @Override
    @Transactional
    public void deleteBookByIdFromCart(Long id) {
        CartItem cartItem = findingCartItemById(id);
        cartItemRepository.deleteById(cartItem.getId());
    }

    CartItem findingCartItemById(Long id) {
        return cartItemRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Item with id " + id + " not found"));
    }
}
