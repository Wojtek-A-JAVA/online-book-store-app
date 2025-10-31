package book.store.controller;

import book.store.dto.order.CreateOrderRequestDto;
import book.store.dto.order.OrderDto;
import book.store.dto.order.UpdateOrderRequestDto;
import book.store.dto.orderitem.OrderItemDto;
import book.store.model.User;
import book.store.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order", description = "Orders related endpoints")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "Create Order", description = "The user can place an order")
    @PreAuthorize("hasRole('USER')")
    public OrderDto placeOrder(@RequestBody @Valid CreateOrderRequestDto createOrderRequestDto,
                               Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return orderService.placeOrder(createOrderRequestDto, user);
    }

    @GetMapping
    @Operation(summary = "Get all orders", description = "The user can get all orders")
    @PreAuthorize("hasRole('USER')")
    public List<OrderDto> getAllOrders(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return orderService.getAllOrders(user);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update order status", description = "The admin can update order status")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public void updateOrderStatus(@PathVariable Long id,
                                  @RequestBody @Valid UpdateOrderRequestDto updateOrderDto) {
        orderService.updateOrderStatus(id, updateOrderDto);
    }

    @GetMapping("/{id}/items")
    @Operation(summary = "Get all order items", description = "The user can get all order items "
            + "from specific order")
    @PreAuthorize("hasRole('USER')")
    public List<OrderItemDto> getAllOrderItems(@PathVariable Long id,
                                               Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return orderService.getAllOrderItemsFromOrder(id, user);
    }

    @GetMapping("/{orderId}/items/{itemId}")
    @Operation(summary = "Get order item from order", description = "The user can get order item "
            + "from specific order")
    @PreAuthorize("hasRole('USER')")
    public OrderItemDto getOrderItemFromOrder(@PathVariable Long orderId,
                                              @PathVariable Long itemId,
                                              Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return orderService.getOrderItemFromOrder(orderId, itemId, user);
    }
}
