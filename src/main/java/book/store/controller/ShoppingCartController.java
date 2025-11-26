package book.store.controller;

import book.store.dto.cartitem.CartItemDto;
import book.store.dto.shoppingcart.CreateShoppingCartRequestDto;
import book.store.dto.shoppingcart.ShoppingCartDto;
import book.store.dto.shoppingcart.UpdateCartItemRequestDto;
import book.store.model.User;
import book.store.service.ShoppingCartService;
import book.store.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping cart", description = "Shopping carts related endpoints")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/cart")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;
    private final UserService userService;

    @GetMapping
    @Operation(summary = "View shopping cart", description = "User can view his shopping cart")
    @PreAuthorize("hasRole('USER')")
    public ShoppingCartDto getShoppingCart(Authentication authentication) {
        String email = authentication.getName();
        User user = userService.getUserByEmail(email);
        return shoppingCartService.getShoppingCart(user.getId());
    }

    @PostMapping
    @Operation(summary = "Add item", description = "User can add item to his shopping cart")
    @PreAuthorize("hasRole('USER')")
    public CartItemDto addItemToShoppingCart(
            @RequestBody @Valid CreateShoppingCartRequestDto shoppingCartRequestDto,
            Authentication authentication) {
        String email = authentication.getName();
        User user = userService.getUserByEmail(email);
        return shoppingCartService.addItemToCart(user, shoppingCartRequestDto);
    }

    @PutMapping("/cart-items/{id}")
    @Operation(summary = "Update Item quantity", description =
            "User can update item quantity in his shopping cart")
    @PreAuthorize("hasRole('USER')")
    public CartItemDto updateItem(@PathVariable Long id,
                                  @RequestBody @Valid UpdateCartItemRequestDto itemRequestDto) {
        return shoppingCartService.updateItemQuantityById(id, itemRequestDto);
    }

    @DeleteMapping("/cart-items/{id}")
    @Operation(summary = "Delete Item", description = "User can remove item from his shopping cart")
    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.OK)
    public void deleteItemFromShoppingCart(@PathVariable Long id) {
        shoppingCartService.deleteBookByIdFromCart(id);
    }
}


