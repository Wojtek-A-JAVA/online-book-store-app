package book.store.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import book.store.service.impl.ShoppingCartServiceImpl;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ShoppingCartServiceTest {

    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;

    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private ShoppingCartMapper shoppingCartMapper;
    @Mock
    private CartItemMapper cartItemMapper;

    @Test
    void getOne_ShoppingCartInDatabase_Success() {
        User user = createUser();
        ShoppingCart shoppingCart = createShoppingCart(user);
        ShoppingCartDto expected = new ShoppingCartDto().setId(shoppingCart.getId())
                .setUserId(user.getId());

        when(shoppingCartRepository.findByUserId(user.getId())).thenReturn(Optional.of(shoppingCart));
        when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(expected);

        ShoppingCartDto actual = shoppingCartService.getShoppingCart(user.getId());

        assertEquals(expected, actual);
    }

    @Test
    void getShoppingCart_ShoppingCartNotInDatabase_ThrowsException() {
        Long userId = -1L;

        when(shoppingCartRepository.findByUserId(userId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> shoppingCartService.getShoppingCart(userId));

        assertEquals("User with id " + userId + " doesn't have shopping cart", exception.getMessage());
    }

    @Test
    void addItemToCart_NewCartItemToNotExistingShoppingCartInDatabase_Success() {
        User user = createUser();
        CreateShoppingCartRequestDto shoppingCartRequestDto = new CreateShoppingCartRequestDto()
                .setBookId(2L)
                .setQuantity(5);
        Book book = createBook()
                .setId(shoppingCartRequestDto.getBookId());
        ShoppingCart shoppingCart = createShoppingCart(user);
        CartItem cartItem = new CartItem().setId(1L).setShoppingCart(shoppingCart).setBook(book)
                .setQuantity(shoppingCartRequestDto.getQuantity());
        CartItemDto cartItemDto = new CartItemDto().setId(cartItem.getId())
                .setBookId(cartItem.getBook().getId()).setQuantity(cartItem.getQuantity());

        when(bookRepository.findById(shoppingCartRequestDto.getBookId())).thenReturn(Optional.of(book));
        when(shoppingCartRepository.findByUserId(user.getId())).thenReturn(Optional.empty());
        when(shoppingCartRepository.save(any(ShoppingCart.class))).thenReturn(shoppingCart);
        when(cartItemRepository.findByShoppingCartIdAndBookId(shoppingCart.getId(), book.getId()))
                .thenReturn(Optional.empty());
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);
        when(cartItemMapper.toDto(any(CartItem.class))).thenReturn(cartItemDto);

        CartItemDto actual = shoppingCartService.addItemToCart(user, shoppingCartRequestDto);

        assertNotNull(actual.getId());
        assertEquals(shoppingCartRequestDto.getBookId(), actual.getBookId());
        assertEquals(shoppingCartRequestDto.getQuantity(), actual.getQuantity());
    }

    @Test
    void addItemToCart_NewCartItemToExistingShoppingCartInDatabase_Success() {
        User user = createUser();
        CreateShoppingCartRequestDto shoppingCartRequestDto = new CreateShoppingCartRequestDto()
                .setBookId(2L)
                .setQuantity(5);
        Book book = createBook()
                .setId(shoppingCartRequestDto.getBookId());
        ShoppingCart shoppingCart = createShoppingCart(user);
        CartItem cartItem = new CartItem().setId(1L).setShoppingCart(shoppingCart).setBook(book)
                .setQuantity(shoppingCartRequestDto.getQuantity());
        CartItemDto cartItemDto = new CartItemDto().setId(cartItem.getId())
                .setBookId(cartItem.getBook().getId()).setQuantity(cartItem.getQuantity());

        when(bookRepository.findById(shoppingCartRequestDto.getBookId())).thenReturn(Optional.of(book));
        when(shoppingCartRepository.findByUserId(user.getId())).thenReturn(Optional.of(shoppingCart));
        when(cartItemRepository.findByShoppingCartIdAndBookId(shoppingCart.getId(), book.getId()))
                .thenReturn(Optional.empty());
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);
        when(cartItemMapper.toDto(any(CartItem.class))).thenReturn(cartItemDto);

        CartItemDto actual = shoppingCartService.addItemToCart(user, shoppingCartRequestDto);

        assertEquals(shoppingCartRequestDto.getBookId(), actual.getBookId());
        assertEquals(shoppingCartRequestDto.getQuantity(), actual.getQuantity());
        assertEquals(cartItemDto.getId(), actual.getId());
    }

    @Test
    void addItemToCart_existingCartAndItem_updatesQuantity() {
        User user = createUser();
        CreateShoppingCartRequestDto shoppingCartRequestDto = new CreateShoppingCartRequestDto()
                .setBookId(2L)
                .setQuantity(15);
        Book book = createBook()
                .setId(shoppingCartRequestDto.getBookId());
        ShoppingCart shoppingCart = createShoppingCart(user);
        CartItem existingCartItem = new CartItem().setId(1L).setShoppingCart(shoppingCart).setBook(book)
                .setQuantity(5);
        CartItem updatedCartItem = new CartItem().setId(existingCartItem.getId())
                .setShoppingCart(existingCartItem.getShoppingCart())
                .setBook(existingCartItem.getBook())
                .setQuantity(shoppingCartRequestDto.getQuantity() + existingCartItem.getQuantity());
        CartItemDto cartItemDto = new CartItemDto().setId(updatedCartItem.getId())
                .setBookId(updatedCartItem.getBook().getId())
                .setQuantity(updatedCartItem.getQuantity());

        when(bookRepository.findById(shoppingCartRequestDto.getBookId())).thenReturn(Optional.of(book));
        when(shoppingCartRepository.findByUserId(user.getId())).thenReturn(Optional.of(shoppingCart));
        when(cartItemRepository.findByShoppingCartIdAndBookId(shoppingCart.getId(), book.getId()))
                .thenReturn(Optional.of(existingCartItem));
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(updatedCartItem);
        when(cartItemMapper.toDto(any(CartItem.class))).thenReturn(cartItemDto);

        CartItemDto actual = shoppingCartService.addItemToCart(user, shoppingCartRequestDto);

        assertNotNull(actual.getId());
        assertEquals(book.getId(), actual.getBookId());
        assertEquals(updatedCartItem.getQuantity(), actual.getQuantity());
    }

    @Test
    void addItemToCart_BookNotFound_ThrowsException() {
        User user = createUser();
        CreateShoppingCartRequestDto shoppingCartRequestDto = new CreateShoppingCartRequestDto()
                .setBookId(-2L)
                .setQuantity(1);

        when(bookRepository.findById(shoppingCartRequestDto.getBookId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> shoppingCartService.addItemToCart(user, shoppingCartRequestDto));
        assertEquals("Item with id " + shoppingCartRequestDto.getBookId()
                + " not found", exception.getMessage());
    }

    @Test
    void updateItemQuantityById_changeItemQuantityInDatabase_Success() {
        Long itemId = 1L;
        UpdateCartItemRequestDto itemRequestDto = new UpdateCartItemRequestDto().setQuantity(10);
        User user = createUser();
        ShoppingCart shoppingCart = createShoppingCart(user);
        Book book = createBook();
        CartItem cartItem = new CartItem().setId(itemId).setShoppingCart(shoppingCart).setBook(book)
                .setQuantity(itemRequestDto.getQuantity());
        CartItemDto cartItemDto = new CartItemDto().setId(cartItem.getId())
                .setBookId(cartItem.getBook().getId()).setQuantity(cartItem.getQuantity());

        when(cartItemRepository.findById(itemId)).thenReturn(Optional.of(cartItem));
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);
        when(cartItemMapper.toDto(any(CartItem.class))).thenReturn(cartItemDto);

        CartItemDto actual = shoppingCartService.updateItemQuantityById(itemId, itemRequestDto);

        assertEquals(1L, actual.getBookId());
        assertEquals(10, actual.getQuantity());
        assertEquals(1, actual.getId());
    }

    @Test
    void updateItemQuantityById_changeItemQuantityInDatabase_ThrowException() {
        Long itemId = -1L;
        UpdateCartItemRequestDto itemRequestDto = new UpdateCartItemRequestDto().setQuantity(10);

        when(cartItemRepository.findById(itemId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> shoppingCartService.updateItemQuantityById(itemId, itemRequestDto));
        assertEquals("Item with id " + itemId + " not found", exception.getMessage());
    }

    @Test
    void deleteBookByIdFromCart_deleteCartItemInDatabase_Success() {
        Long itemId = 1L;
        UpdateCartItemRequestDto itemRequestDto = new UpdateCartItemRequestDto().setQuantity(10);
        User user = createUser();
        ShoppingCart shoppingCart = createShoppingCart(user);
        Book book = createBook();
        CartItem cartItem = new CartItem().setId(itemId).setShoppingCart(shoppingCart).setBook(book)
                .setQuantity(itemRequestDto.getQuantity());

        when(cartItemRepository.findById(cartItem.getId())).thenReturn(Optional.of(cartItem));

        shoppingCartService.deleteBookByIdFromCart(itemId);

        verify(cartItemRepository).deleteById(itemId);
    }

    @Test
    void deleteBookByIdFromCart_deleteCartItemInDatabase_ThrowException() {
        Long itemId = -1L;
        when(cartItemRepository.findById(itemId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> shoppingCartService.deleteBookByIdFromCart(itemId));
        assertEquals("Item with id " + itemId + " not found", exception.getMessage());
    }

    private User createUser() {
        return new User()
                .setId(1L)
                .setEmail("mail@test.com")
                .setPassword("password")
                .setFirstName("John")
                .setLastName("Doe")
                .setDeleted(false);
    }

    private Book createBook() {
        return new Book()
                .setId(1L)
                .setTitle("Book 2")
                .setAuthor("Author")
                .setIsbn("1234567890123")
                .setPrice(new BigDecimal("10.10"));
    }

    private ShoppingCart createShoppingCart(User user) {
        return new ShoppingCart()
                .setId(1L)
                .setUser(user);
    }
}
