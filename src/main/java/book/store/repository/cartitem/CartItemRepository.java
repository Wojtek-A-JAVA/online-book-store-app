package book.store.repository.cartitem;

import book.store.model.CartItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByShoppingCartIdAndBookId(Long cartId, Long bookId);
}
