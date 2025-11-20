package book.store.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import book.store.dto.book.BookDto;
import book.store.dto.book.CreateBookRequestDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    @Sql(scripts = "classpath:database/books/delete-added-book.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createBook_ValidRequestDto_Success() throws Exception {
        CreateBookRequestDto requestDto = new CreateBookRequestDto()
                .setTitle("Title 1")
                .setAuthor("Author 1")
                .setIsbn("2427224222223")
                .setPrice(BigDecimal.valueOf(10.10));

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        BookDto expected = new BookDto()
                .setTitle(requestDto.getTitle())
                .setAuthor(requestDto.getAuthor())
                .setIsbn(requestDto.getIsbn())
                .setPrice(requestDto.getPrice());

        MvcResult result = mockMvc.perform(
                        post("/books")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class
        );

        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getAuthor(), actual.getAuthor());
        assertEquals(expected.getIsbn(), actual.getIsbn());
        assertEquals(expected.getPrice(), actual.getPrice());
        EqualsBuilder.reflectionEquals(actual, expected, "id");
    }

    @Test
    @WithMockUser(roles = "USER")
    void createBook_ValidRequestDto_ReturnStatusForbidden() throws Exception {
        CreateBookRequestDto requestDto = new CreateBookRequestDto()
                .setTitle("Title 1")
                .setAuthor("Author 1")
                .setIsbn("2427224222223")
                .setPrice(BigDecimal.valueOf(10.10));

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAll_BooksInDatabase_Success() throws Exception {
        MvcResult result = mockMvc.perform(get("/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsString());
        BookDto[] bookDtos = objectMapper.treeToValue(root.get("content"), BookDto[].class);

        assertEquals(4, bookDtos.length);
        Assertions.assertNotNull(bookDtos[1].getId());
        Assertions.assertNotNull(bookDtos[2].getTitle());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getOne_BookInDatabase_Success() throws Exception {
        BookDto expected = new BookDto()
                .setTitle("Book 1")
                .setAuthor("Author 1")
                .setIsbn("1234567890123")
                .setPrice(BigDecimal.valueOf(10));

        MvcResult result = mockMvc.perform(get("/books/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class
        );

        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getAuthor(), actual.getAuthor());
        assertEquals(expected.getIsbn(), actual.getIsbn());
        assertEquals(expected.getPrice(), actual.getPrice());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Sql(scripts = "classpath:database/books/restore-updated-book-id4.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void update_BookData_Success() throws Exception {
        CreateBookRequestDto expected = new CreateBookRequestDto()
                .setTitle("TEST Title")
                .setAuthor("TEST Author")
                .setIsbn("TEST224222223")
                .setPrice(BigDecimal.valueOf(99));

        String jsonRequest = objectMapper.writeValueAsString(expected);

        MvcResult result = mockMvc.perform(put("/books/4")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class
        );

        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getAuthor(), actual.getAuthor());
        assertEquals(expected.getIsbn(), actual.getIsbn());
        assertEquals(expected.getPrice(), actual.getPrice());
        assertEquals(expected.getPrice(), actual.getPrice());
    }

    @Test
    @WithMockUser(roles = "USER")
    void search_CorrectBook_Success() throws Exception {
        MvcResult result = mockMvc.perform(get("/books/search")
                        .param("author", "Author 1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsString());
        BookDto[] bookDtos = objectMapper.treeToValue(root.get("content"), BookDto[].class);

        assertEquals(2, bookDtos.length);
        assertEquals("Author 1", bookDtos[0].getAuthor());
        assertEquals("Book 4", bookDtos[1].getTitle());

    }

    @Test
    @WithMockUser(roles = "USER")
    void search_IncorrectBook_Success() throws Exception {
        MvcResult result = mockMvc.perform(get("/books/search")
                        .param("title", "Test")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsString());
        BookDto[] bookDtos = objectMapper.treeToValue(root.get("content"), BookDto[].class);

        assertEquals(0, bookDtos.length);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Sql(scripts = "classpath:database/books/restore-deleted-book-id2.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void delete_BookById_Success() throws Exception {
        mockMvc.perform(delete("/books/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Sql(scripts = "classpath:database/books/restore-deleted-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void delete_AllBooks_Success() throws Exception {
        mockMvc.perform(delete("/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
