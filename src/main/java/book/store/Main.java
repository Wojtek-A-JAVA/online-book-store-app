package book.store;

import book.store.config.AppConfig;
import book.store.model.Book;
import book.store.service.BookService;
import java.math.BigDecimal;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        Book book = new Book();
        book.setTitle("Book Title");
        book.setAuthor("Author");
        book.setIsbn("ISBN");
        book.setPrice(BigDecimal.valueOf(10));
        book.setDescription("Book Description");
        book.setCoverImage("cover.jpg");

        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(AppConfig.class);
        BookService bookService = context.getBean(BookService.class);

        bookService.save(book);
        System.out.println(bookService.findAll());
    }
}
