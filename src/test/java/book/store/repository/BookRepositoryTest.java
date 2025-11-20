package book.store.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import book.store.model.Book;
import book.store.repository.book.BookRepository;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    void findAllByCategoryId_BooksInDatabaseWithCategoryIdOne_Success() {
        Long categoryId = 1L;

        List<Book> actual = bookRepository.findAllByCategoryId(categoryId);

        Assertions.assertNotNull(actual);
        assertEquals(2, actual.size());
        assertEquals("Book 1", actual.get(0).getTitle());
        assertEquals("1234567890126", actual.getLast().getIsbn());
    }
}
