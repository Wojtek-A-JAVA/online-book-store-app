package book.store.mapper;

import book.store.dto.BookDto;
import book.store.dto.CreateBookRequestDto;
import book.store.model.Book;

public interface BookMapper {

    BookDto toBookDto(Book book);

    Book toBook(CreateBookRequestDto bookDto);
}
