package book.store.service;

import book.store.dto.order.CreateOrderRequestDto;
import book.store.dto.order.OrderDto;
import book.store.dto.order.UpdateOrderRequestDto;
import book.store.dto.orderitem.OrderItemDto;
import book.store.model.User;
import java.util.List;

public interface OrderService {

    OrderDto placeOrder(CreateOrderRequestDto createOrderRequestDto, User user);

    List<OrderDto> getAllOrders(User user);

    List<OrderItemDto> getAllOrderItemsFromOrder(Long id);

    OrderItemDto getOrderItemFromOrder(Long orderId, Long itemId);

    void updateOrderStatus(Long id, UpdateOrderRequestDto updateOrderDto);
}
