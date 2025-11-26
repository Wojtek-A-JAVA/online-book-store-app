package book.store.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import book.store.model.ShoppingCart;
import book.store.repository.shoppingcart.ShoppingCartRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ShoppingCartRepositoryTest {
    private static final Long EXISTING_SHOPPING_CART_ID = 1L;
    private static final Long EXISTING_USER_ID = 2L;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Test
    @Sql(scripts = "classpath:database/shoppingcart/add-shopping-cart.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/shoppingcart/delete-added-shopping-cart.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByUserId_ShoppingCartInDatabase_Success() {
        Optional<ShoppingCart> actual = shoppingCartRepository.findByUserId(EXISTING_USER_ID);

        assertTrue(actual.isPresent());
        assertEquals(EXISTING_SHOPPING_CART_ID, actual.get().getId());
        assertEquals(EXISTING_USER_ID, actual.get().getUser().getId());
    }
}
