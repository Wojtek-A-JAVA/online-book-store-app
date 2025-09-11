package book.store.service;

import book.store.dto.book.BookDto;
import book.store.dto.book.BookSearchParametersDto;
import book.store.dto.book.CreateBookRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto bookDto);

    BookDto findById(Long id);

    Page<BookDto> findAll(Pageable pageable);

    BookDto updateBook(Long id, CreateBookRequestDto bookDto);

    void deleteById(Long id);

    void deleteAll();

    Page<BookDto> search(BookSearchParametersDto parameters, Pageable pageable);
}
