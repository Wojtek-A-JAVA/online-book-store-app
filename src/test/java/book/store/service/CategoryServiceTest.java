package book.store.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import book.store.dto.book.BookDtoWithoutCategoryIds;
import book.store.dto.category.CategoryDto;
import book.store.dto.category.CreateCategoryRequestDto;
import book.store.mapper.BookMapper;
import book.store.mapper.CategoryMapper;
import book.store.model.Book;
import book.store.model.Category;
import book.store.repository.book.BookRepository;
import book.store.repository.category.CategoryRepository;
import book.store.service.impl.CategoryServiceImpl;
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
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;

    @Test
    void getAll_CategoriesInDatabase_Success() {
        Category categoryOne = createCategory(1L, "Name 1", "Description 1");
        Category categoryTwo = createCategory(2L, "Name 2", "Description 2");
        Category categoryThree = createCategory(3L, "Name 3", "Description 3");
        CategoryDto categoryDtoOne = createCategoryDtoFromCategory(categoryOne);
        CategoryDto categoryDtoTwo = createCategoryDtoFromCategory(categoryTwo);
        CategoryDto categoryDtoThree = createCategoryDtoFromCategory(categoryThree);
        List<Category> categoryList = List.of(categoryOne, categoryTwo, categoryThree);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Category> categoryPage = new PageImpl<>(categoryList, pageable, categoryList.size());

        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);
        when(categoryMapper.toDto(categoryOne)).thenReturn(categoryDtoOne);
        when(categoryMapper.toDto(categoryTwo)).thenReturn(categoryDtoTwo);
        when(categoryMapper.toDto(categoryThree)).thenReturn(categoryDtoThree);

        Page<CategoryDto> actual = categoryService.findAll(pageable);

        Assertions.assertEquals(3, actual.getTotalElements());
        Assertions.assertEquals(categoryDtoOne, actual.getContent().getFirst());
        Assertions.assertEquals(categoryDtoThree, actual.getContent().getLast());
        Assertions.assertEquals(1, actual.getTotalPages());
    }

    @Test
    void getOne_CategoryInDatabase_Success() {
        Long categoryId = 1L;
        Category category = createCategory(categoryId, "Name 1", "Description 1");
        CategoryDto categoryDto = createCategoryDtoFromCategory(category);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto expected = categoryService.getById(categoryId);

        Assertions.assertEquals(expected, categoryDto);
    }

    @Test
    void getOne_CategoryInDatabase_ThrowNotFoundException() {
        Long categoryId = -1L;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class,
                () -> categoryService.getById(categoryId));

        assertEquals("Category with id " + categoryId + " not found", exception.getMessage());
    }

    @Test
    void creatCategory_ValidRequestDto_Success() {
        CreateCategoryRequestDto categoryRequestDto = new CreateCategoryRequestDto()
                .setName("Name 1")
                .setDescription("Description 1");
        Category category = createCategory(1L, categoryRequestDto.getName(), categoryRequestDto.getDescription());
        CategoryDto categoryDto = createCategoryDtoFromCategory(category);

        when(categoryMapper.toEntity(categoryRequestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto expected = categoryService.save(categoryRequestDto);

        assertEquals(expected, categoryDto);
    }

    @Test
    void update_CategoryInDatabase_Success() {
        Long categoryId = 1L;
        CreateCategoryRequestDto categoryRequestDto = new CreateCategoryRequestDto()
                .setName("TEST Name")
                .setDescription("TEST Description");
        Category category = createCategory(categoryId, "Name 1", "Description 1");
        Category categoryUpdated = createCategory(categoryId, categoryRequestDto.getName(),
                categoryRequestDto.getDescription());
        CategoryDto categoryUpdatedDto = createCategoryDtoFromCategory(categoryUpdated);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryMapper.toEntity(categoryRequestDto)).thenReturn(categoryUpdated);
        when(categoryRepository.save(categoryUpdated)).thenReturn(categoryUpdated);
        when(categoryMapper.toDto(categoryUpdated)).thenReturn(categoryUpdatedDto);

        CategoryDto actual = categoryService.update(categoryId, categoryRequestDto);

        assertEquals("TEST Name", actual.getName());
        assertEquals("TEST Description", actual.getDescription());
    }

    @Test
    void update_CategoryInDatabase_ThrowNotFoundException() {
        Long categoryId = -1L;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class,
                () -> categoryService.getById(categoryId));

        assertEquals("Category with id " + categoryId + " not found", exception.getMessage());
    }

    @Test
    void deleteById_CategoryInDatabase_Success() {
        doNothing().when(categoryRepository).deleteById(1L);

        categoryService.deleteById(1L);

        verify(categoryRepository).deleteById(1L);
        verify(categoryRepository, times(1)).deleteById(1L);
    }

    @Test
    void get_BooksByCategoryIdInDatabase_Success() {
        Long categoryId = 1L;
        Book bookOne = createBook(1L, "Book 1", "Author 1", "1234567890121", "10.10");
        Book bookTwo = createBook(2L, "Book 2", "Author 2", "1234567890122", "20.20");
        Book bookThree = createBook(3L, "Book 3", "Author 1", "1234567890123", "20.20");
        List<Book> bookList = List.of(bookOne, bookTwo, bookThree);

        BookDtoWithoutCategoryIds bookDtoWithoutCategoryIdOne = createCategoryDtoFromCategory(bookOne);
        BookDtoWithoutCategoryIds bookDtoWithoutCategoryIdTwo = createCategoryDtoFromCategory(bookTwo);
        BookDtoWithoutCategoryIds bookDtoWithoutCategoryIdThree = createCategoryDtoFromCategory(bookThree);

        when(bookRepository.findAllByCategoryId(categoryId)).thenReturn(bookList);
        when(bookMapper.toDtoWithoutCategories(bookOne)).thenReturn(bookDtoWithoutCategoryIdOne);
        when(bookMapper.toDtoWithoutCategories(bookTwo)).thenReturn(bookDtoWithoutCategoryIdTwo);
        when(bookMapper.toDtoWithoutCategories(bookThree)).thenReturn(bookDtoWithoutCategoryIdThree);

        List<BookDtoWithoutCategoryIds> actual =
                categoryService.getBooksByCategoryId(categoryId);

        assertEquals(3, actual.size());
        assertEquals(bookDtoWithoutCategoryIdOne, actual.get(0));
        assertEquals(bookDtoWithoutCategoryIdTwo, actual.get(1));
        assertEquals(bookDtoWithoutCategoryIdThree, actual.get(2));
    }

    private Category createCategory(Long id, String name, String description) {
        return new Category()
                .setId(id)
                .setName(name)
                .setDescription(description);
    }

    private CategoryDto createCategoryDtoFromCategory(Category category) {
        return new CategoryDto()
                .setId(category.getId())
                .setName(category.getName())
                .setDescription(category.getDescription());
    }

    private Book createBook(Long id, String title, String author, String isbn, String price) {
        return new Book()
                .setId(id)
                .setTitle(title)
                .setAuthor(author)
                .setIsbn(isbn)
                .setPrice(new BigDecimal(price));
    }

    private BookDtoWithoutCategoryIds createCategoryDtoFromCategory(Book book) {
        return new BookDtoWithoutCategoryIds()
                .setId(book.getId())
                .setTitle(book.getTitle())
                .setAuthor(book.getAuthor())
                .setIsbn(book.getIsbn())
                .setPrice(book.getPrice());
    }
}
