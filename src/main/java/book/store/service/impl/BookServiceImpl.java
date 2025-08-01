package book.store.service.impl;

import book.store.dto.BookDto;
import book.store.dto.BookSearchParametersDto;
import book.store.dto.CreateBookRequestDto;
import book.store.exception.EntityNotFoundException;
import book.store.mapper.BookMapper;
import book.store.model.Book;
import book.store.repository.book.BookRepository;
import book.store.repository.book.BookSpecificationBuilderImpl;
import book.store.service.BookService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilderImpl bookSpecificationBuilder;

    @Override
    public BookDto save(CreateBookRequestDto bookDto) {
        Book book = bookMapper.toBook(bookDto);
        Book savedBook = bookRepository.save(book);
        return bookMapper.toBookDto(savedBook);
    }

    @Override
    public BookDto findById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Book with id " + id + " not found"));
        return bookMapper.toBookDto(book);
    }

    @Override
    public List<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable).stream()
                .map(bookMapper::toBookDto)
                .collect(Collectors.toList());
    }

    @Override
    public BookDto updateBook(Long id, CreateBookRequestDto bookDto) {
        Book bookSaved = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Book with id " + id + " not found"));
        Book bookUpdated = bookMapper.toBook(bookDto);
        bookUpdated.setId(bookSaved.getId());
        return bookMapper.toBookDto(bookRepository.save(bookUpdated));
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        bookRepository.deleteAll();
    }

    @Override
    public List<BookDto> search(BookSearchParametersDto parameters) {
        Specification<Book> bookSpecification = bookSpecificationBuilder.build(parameters);
        return bookRepository.findAll(bookSpecification)
                .stream()
                .map(bookMapper::toBookDto)
                .toList();
    }
}
