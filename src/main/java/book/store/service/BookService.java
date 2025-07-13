package book.store.service;

import book.store.dto.BookDto;
import book.store.dto.CreateBookRequestDto;
import java.util.List;

public interface BookService {
    BookDto save(CreateBookRequestDto bookDto);

    BookDto findById(Long id);

    List<BookDto> findAll();

    BookDto updateBook(Long id, CreateBookRequestDto bookDto);

    void deleteById(Long id);

    void deleteAll();
}
