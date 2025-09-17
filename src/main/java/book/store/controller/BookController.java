package book.store.controller;

import book.store.dto.book.BookDto;
import book.store.dto.book.BookSearchParametersDto;
import book.store.dto.book.CreateBookRequestDto;
import book.store.service.BookService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
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

@OpenAPIDefinition(tags = {
        @Tag(name = "Create"),
        @Tag(name = "Update"),
        @Tag(name = "Find"),
        @Tag(name = "Search"),
        @Tag(name = "Delete"),
})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/books")
public class BookController {

    private final BookService bookService;

    @Tag(name = "Create")
    @PostMapping
    @Operation(summary = "Create new book", description = "Create new book")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public BookDto createBook(@RequestBody @Valid CreateBookRequestDto bookDto) {
        return bookService.save(bookDto);
    }

    @Tag(name = "Update")
    @PutMapping("/{id}")
    @Operation(summary = "Update book data", description = "Update book data by id")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public BookDto updateBook(@PathVariable Long id, @RequestBody CreateBookRequestDto bookDto) {
        return bookService.updateBook(id, bookDto);
    }

    @Tag(name = "Find")
    @GetMapping
    @Operation(summary = "Get all books", description = "Get a list of undeleted books "
            + "with the possibility of sorting and pagination")
    @PageableAsQueryParam
    @PreAuthorize("hasRole('ROLE_USER')")
    public Page<BookDto> getAll(@Parameter(hidden = true) Pageable pageable) {
        return bookService.findAll(pageable);
    }

    @Tag(name = "Find")
    @GetMapping("/{id}")
    @Operation(summary = "Get one book", description = "Get a book by id")
    @PreAuthorize("hasRole('ROLE_USER')")
    public BookDto getBookById(@PathVariable Long id) {
        return bookService.findById(id);
    }

    @Tag(name = "Search")
    @GetMapping("/search")
    @Operation(summary = "Search for books", description = "Search for books by given data")
    @PreAuthorize("hasRole('ROLE_USER')")
    public Page<BookDto> search(BookSearchParametersDto parameters,
                                @Parameter(hidden = true) Pageable pageable) {
        return bookService.search(parameters, pageable);
    }

    @Tag(name = "Delete")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete one book", description = "Delete a book by id")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteBookById(@PathVariable Long id) {
        bookService.deleteById(id);
    }

    @Tag(name = "Delete")
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete all books", description = "Delete all books")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteAll() {
        bookService.deleteAll();
    }
}
