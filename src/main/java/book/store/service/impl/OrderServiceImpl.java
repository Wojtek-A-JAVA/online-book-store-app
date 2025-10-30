package book.store.service.impl;

import book.store.dto.order.CreateOrderRequestDto;
import book.store.dto.order.OrderDto;
import book.store.dto.order.UpdateOrderRequestDto;
import book.store.dto.orderitem.OrderItemDto;
import book.store.exception.EntityNotFoundException;
import book.store.mapper.OrderItemMapper;
import book.store.mapper.OrderMapper;
import book.store.model.CartItem;
import book.store.model.Order;
import book.store.model.OrderItem;
import book.store.model.ShoppingCart;
import book.store.model.User;
import book.store.repository.cartitem.CartItemRepository;
import book.store.repository.order.OrderRepository;
import book.store.repository.orderitem.OrderItemRepository;
import book.store.repository.shoppingcart.ShoppingCartRepository;
import book.store.service.OrderService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    @Override
    @Transactional
    public OrderDto placeOrder(CreateOrderRequestDto createOrderRequestDto, User user) {

        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(user.getId()).orElseThrow(
                () -> new EntityNotFoundException("User with id " + user.getId()
                        + " doesn't have shopping cart")
        );
        Optional<Order> orderOptional = orderRepository.findByUserId(user.getId());
        Order order = new Order();

        if (orderOptional.isEmpty() || orderOptional.get().getStatus() != Order.Status.PENDING) {
            order.setUser(user);
            order.setStatus(Order.Status.PENDING);
            order.setTotal(BigDecimal.ZERO);
            order.setOrderDate(LocalDateTime.now());
            order.setShippingAddress(createOrderRequestDto.getShippingAddress());
            orderRepository.save(order);
        } else {
            order = orderOptional.get();
        }

        Set<OrderItem> orderItems = order.getOrderItems();

        for (CartItem cartItem : shoppingCart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setBook(cartItem.getBook());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(orderItem.getBook().getPrice());
            orderItems.add(orderItemRepository.save(orderItem));
            BigDecimal orderItemTotalPrice = orderItem.getPrice()
                    .multiply(BigDecimal.valueOf(orderItem.getQuantity()));
            order.setTotal(order.getTotal().add(orderItemTotalPrice));
        }

        shoppingCartRepository.delete(shoppingCart);

        return orderMapper.toDto(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getAllOrders(User user) {
        return orderRepository.findAllByUserId(user.getId()).stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public void updateOrderStatus(Long id, UpdateOrderRequestDto updateOrderDto) {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Order with id " + id + " wasn't found"));
        order.setStatus(updateOrderDto.getStatus());
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public List<OrderItemDto> getAllOrderItemsFromOrder(Long orderId, User user) {
        Order order = findUserOrder(orderId, user);

        return order.getOrderItems().stream()
                .map(orderItemMapper::toDto)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    @Transactional
    public OrderItemDto getOrderItemFromOrder(Long orderId, Long itemId, User user) {
        Order order = findUserOrder(orderId, user);

        for (OrderItem orderItem : order.getOrderItems()) {
            if (orderItem.getId() == itemId) {
                return orderItemMapper.toDto(orderItem);
            }
        }

        throw new EntityNotFoundException("Order item with id " + itemId
                + " was not found in order with id " + orderId);
    }

    private Order findUserOrder(Long orderId, User user) {
        return orderRepository.findByIdAndUserId(orderId, user.getId()).orElseThrow(
                () -> new EntityNotFoundException("User with id " + user.getId()
                        + " doesn't have order with id " + orderId));
    }
}
