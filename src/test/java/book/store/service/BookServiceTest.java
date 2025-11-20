package book.store.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import book.store.dto.book.BookDto;
import book.store.dto.book.BookSearchParametersDto;
import book.store.dto.book.CreateBookRequestDto;
import book.store.exception.EntityNotFoundException;
import book.store.mapper.BookMapper;
import book.store.model.Book;
import book.store.repository.book.BookRepository;
import book.store.repository.book.BookSpecificationBuilderImpl;
import book.store.service.impl.BookServiceImpl;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @InjectMocks
    private BookServiceImpl bookService;

    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;
    @Mock
    private BookSpecificationBuilderImpl bookSpecificationBuilder;

    @Test
    void createBook_ValidRequestDto_Success() {
        CreateBookRequestDto requestDto = new CreateBookRequestDto()
                .setTitle("Book 1")
                .setAuthor("Author")
                .setIsbn("1234567890123")
                .setPrice(new BigDecimal("10.10"));
        Book book = createBook(1L, requestDto.getTitle(), requestDto.getAuthor(), requestDto.getIsbn(), String.valueOf(requestDto.getPrice()));
        BookDto bookDto = createBookDtoFromBook(book);

        when(bookMapper.toEntity(requestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto saveBookDto = bookService.save(requestDto);

        assertEquals(saveBookDto, bookDto);
    }

    @Test
    void getOne_BookInDatabase_Success() {
        Long bookId = 1L;
        Book book = new Book()
                .setId(bookId)
                .setTitle("Book 1")
                .setAuthor("Author")
                .setIsbn("1234567890123")
                .setPrice(new BigDecimal("10.10"));

        BookDto expected = createBookDtoFromBook(book);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(expected);

        BookDto actual = bookService.findById(bookId);

        assertEquals(expected, actual);
    }

    @Test
    void getOne_BookInDatabase_ThrowNotFoundException() {
        Long bookId = -1L;

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> bookService.findById(bookId));

        assertEquals("Book with id " + bookId + " not found", exception.getMessage());
    }

    @Test
    void getAll_BooksInDatabase_Success() {
        Book bookOne = createBook(1L, "Book 1", "Author 1", "1234567890121", "10.10");
        Book bookTwo = createBook(2L, "Book 2", "Author 2", "1234567890122", "20.20");
        Book bookThree = createBook(3L, "Book 3", "Author 1", "1234567890123", "20.20");
        BookDto bookDtoOne = createBookDtoFromBook(bookOne);
        BookDto bookDtoTwo = createBookDtoFromBook(bookTwo);
        BookDto bookDtoThree = createBookDtoFromBook(bookThree);
        List<Book> bookList = List.of(bookOne, bookTwo, bookThree);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> bookPage = new PageImpl<>(bookList, pageable, bookList.size());

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(bookOne)).thenReturn(bookDtoOne);
        when(bookMapper.toDto(bookTwo)).thenReturn(bookDtoTwo);
        when(bookMapper.toDto(bookThree)).thenReturn(bookDtoThree);

        Page<BookDto> actual = bookService.findAll(pageable);

        Assertions.assertEquals(3, actual.getNumberOfElements());
        Assertions.assertEquals(bookDtoOne, actual.getContent().getFirst());
        Assertions.assertEquals(bookDtoThree, actual.getContent().getLast());
        Assertions.assertEquals(1, actual.getTotalPages());
    }

    @Test
    void update_BookInDatabase_Success() {
        Long bookId = 1L;
        CreateBookRequestDto bookUpdatedDto = new CreateBookRequestDto()
                .setTitle("UPDATED TITLE Book 1")
                .setAuthor("UPDATED AUTHOR Author")
                .setIsbn("1234567890121")
                .setPrice(new BigDecimal("10"));
        Book book = createBook(bookId, "Book 1", "Author 1", "1234567890121", "10");
        Book bookUpdated = createBook(bookId, bookUpdatedDto.getTitle(), bookUpdatedDto.getAuthor(), bookUpdatedDto.getIsbn(), bookUpdatedDto.getIsbn());
        BookDto bookUpdateDto = createBookDtoFromBook(bookUpdated);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookMapper.toEntity(bookUpdatedDto)).thenReturn(bookUpdated);
        when(bookMapper.toDto(bookUpdated)).thenReturn(bookUpdateDto);
        when(bookRepository.save(bookUpdated)).thenReturn(bookUpdated);

        BookDto actual = bookService.updateBook(bookId, bookUpdatedDto);

        assertEquals("UPDATED TITLE Book 1", actual.getTitle());
        assertEquals("UPDATED AUTHOR Author", actual.getAuthor());
        assertEquals(book.getIsbn(), actual.getIsbn());
    }

    @Test
    void update_BookInDatabase_ThrowNotFoundException() {
        Long bookId = -1L;
        CreateBookRequestDto bookUpdatedDto = new CreateBookRequestDto()
                .setTitle("UPDATED TITLE Book 1")
                .setAuthor("UPDATED AUTHOR Author")
                .setIsbn("1234567890121")
                .setPrice(new BigDecimal("10"));

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> bookService.findById(bookId));

        assertEquals("Book with id " + bookId + " not found", exception.getMessage());
    }

    @Test
    void deleteById_BookInDatabase_Success() {
        doNothing().when(bookRepository).deleteById(1L);

        bookService.deleteById(1L);

        verify(bookRepository).deleteById(1L);
        verify(bookRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteAll_BooksInDatabase_Success() {
        doNothing().when(bookRepository).deleteAll();

        bookService.deleteAll();

        verify(bookRepository).deleteAll();
        verify(bookRepository, times(1)).deleteAll();
    }

    @Test
    void search_BookInDatabase_Success() {
        Book book = createBook(1L, "Book 1", "Author 1", "1234567890121", "10");
        BookDto bookDto = createBookDtoFromBook(book);
        BookSearchParametersDto parameters = new BookSearchParametersDto(
                new String[]{"Book 1"},
                new String[]{"Author 1"},
                new String[]{"1234567890121"}
        );
        Pageable pageable = PageRequest.of(0, 10);
        Specification<Book> specification = ((root, query, criteriaBuilder) ->
                criteriaBuilder.conjunction());
        List<Book> bookList = List.of(book);

        when(bookSpecificationBuilder.build(parameters)).thenReturn(specification);
        when(bookRepository.findAll(specification)).thenReturn(bookList);
        when(bookMapper.toDto(book)).thenReturn(bookDto);


        Page<BookDto> expected = bookService.search(parameters, pageable);

        assertEquals(1, expected.getTotalElements());
        assertEquals(book.getTitle(), expected.getContent().getFirst().getTitle());
    }

    private Book createBook(Long id, String title, String author, String isbn, String price) {
        return new Book()
                .setId(id)
                .setTitle(title)
                .setAuthor(author)
                .setIsbn(isbn)
                .setPrice(new BigDecimal(price));
    }

    private BookDto createBookDtoFromBook(Book book) {
        return new BookDto()
                .setId(book.getId())
                .setTitle(book.getTitle())
                .setAuthor(book.getAuthor())
                .setIsbn(book.getIsbn())
                .setPrice(book.getPrice());
    }
}
