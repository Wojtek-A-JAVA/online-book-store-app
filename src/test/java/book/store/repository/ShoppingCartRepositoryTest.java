package book.store.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import book.store.model.ShoppingCart;
import book.store.repository.shoppingcart.ShoppingCartRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ShoppingCartRepositoryTest {

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Test
    @Sql(scripts = "classpath:database/shoppingcart/add-shopping-cart.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/shoppingcart/delete-added-shopping-cart.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByUserId_ShoppingCartInDatabase_Success() {
        Long userId = 1L;

        Optional<ShoppingCart> actual = shoppingCartRepository.findByUserId(userId);

        Assertions.assertTrue(actual.isPresent());
        assertEquals(1, actual.get().getId());
        assertEquals(1, actual.get().getUser().getId());
    }
}
