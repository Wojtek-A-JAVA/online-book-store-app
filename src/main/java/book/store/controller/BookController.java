package book.store.controller;

import book.store.dto.book.BookDto;
import book.store.dto.book.BookSearchParametersDto;
import book.store.dto.book.CreateBookRequestDto;
import book.store.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Book", description = "Books related endpoints")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/books")
public class BookController {

    private final BookService bookService;

    @PostMapping
    @Operation(summary = "Create new book", description = "Create new book")
    @PreAuthorize("hasRole('ADMIN')")
    public BookDto createBook(@RequestBody @Valid CreateBookRequestDto bookDto) {
        return bookService.save(bookDto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update book data", description = "Update book data by id")
    @PreAuthorize("hasRole('ADMIN')")
    public BookDto updateBook(@PathVariable Long id, @RequestBody CreateBookRequestDto bookDto) {
        return bookService.updateBook(id, bookDto);
    }

    @GetMapping
    @Operation(summary = "Get all books", description = "Get a list of undeleted books"
            + "with the possibility of sorting and pagination")
    @PageableAsQueryParam
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Page<BookDto> getAll(@Parameter(hidden = true) Pageable pageable) {
        return bookService.findAll(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get one book", description = "Get a book by id")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public BookDto getBookById(@PathVariable Long id) {
        return bookService.findById(id);
    }

    @GetMapping("/search")
    @Operation(summary = "Search for books", description = "Search for books by given data")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Page<BookDto> search(BookSearchParametersDto parameters,
                                @Parameter(hidden = true) Pageable pageable) {
        return bookService.search(parameters, pageable);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete one book", description = "Delete a book by id")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteBookById(@PathVariable Long id) {
        bookService.deleteById(id);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete all books", description = "Delete all books")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteAll() {
        bookService.deleteAll();
    }
}
