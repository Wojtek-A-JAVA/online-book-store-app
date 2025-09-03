package book.store.service.impl;

import book.store.dto.book.BookDto;
import book.store.dto.book.BookSearchParametersDto;
import book.store.dto.book.CreateBookRequestDto;
import book.store.exception.EntityNotFoundException;
import book.store.mapper.BookMapper;
import book.store.model.Book;
import book.store.repository.book.BookRepository;
import book.store.repository.book.BookSpecificationBuilderImpl;
import book.store.service.BookService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
    public Page<BookDto> findAll(Pageable pageable) {
        List<BookDto> bookDtoList = bookRepository.findAll(pageable).stream()
                .map(bookMapper::toBookDto)
                .collect(Collectors.toList());
        return new PageImpl<>(bookDtoList, pageable, bookDtoList.size());
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
    public Page<BookDto> search(BookSearchParametersDto parameters, Pageable pageable) {
        Specification<Book> bookSpecification = bookSpecificationBuilder.build(parameters);
        List<BookDto> bookDtoList = bookRepository.findAll(bookSpecification)
                .stream()
                .map(bookMapper::toBookDto)
                .toList();
        return new PageImpl<>(bookDtoList, pageable, bookDtoList.size());
    }
}
