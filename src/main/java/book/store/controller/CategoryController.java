package book.store.controller;

import book.store.dto.book.BookDtoWithoutCategoryIds;
import book.store.dto.category.CategoryDto;
import book.store.dto.category.CreateCategorykRequestDto;
import book.store.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
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

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Tag(name = "Create")
    @PostMapping
    @Operation(summary = "Create new category", description = "Create new category")
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryDto createCategory(@RequestBody @Valid CreateCategorykRequestDto categoryDto) {
        return categoryService.save(categoryDto);
    }

    @Tag(name = "Find")
    @GetMapping
    @Operation(summary = "Find all categories", description = "Get a list of undeleted categories")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<CategoryDto> getAll() {
        return categoryService.findAll();
    }

    @Tag(name = "Find")
    @GetMapping("/{id}")
    @Operation(summary = "Find one category", description = "Find category by id")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public CategoryDto getCategoryById(@PathVariable Long id) {
        return categoryService.getById(id);
    }

    @Tag(name = "Update")
    @PutMapping("/{id}")
    @Operation(summary = "Update category", description = "Update category with given id")
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryDto updateCategory(@PathVariable Long id,
                                      @RequestBody CreateCategorykRequestDto categoryDto) {
        return categoryService.update(id, categoryDto);
    }

    @Tag(name = "Delete")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete one category", description = "Delete a category with id")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteById(id);
    }

    @Tag(name = "Find")
    @GetMapping("/{id}/books")
    @Operation(summary = "Find by category", description = "Find books by category")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<BookDtoWithoutCategoryIds> getBooksByCategoryId(@PathVariable Long id) {
        return categoryService.getBooksByCategoryId(id);
    }
}
