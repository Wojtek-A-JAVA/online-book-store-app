package book.store.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import book.store.dto.book.BookDto;
import book.store.dto.category.CategoryDto;
import book.store.dto.category.CreateCategoryRequestDto;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    @Sql(scripts = "classpath:database/categories/delete-added-category.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createCategory_ValidRequestDto_Success() throws Exception {
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto()
                .setName("Category Test")
                .setDescription("Description Test");

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        CategoryDto expected = new CategoryDto()
                .setName(requestDto.getName())
                .setDescription(requestDto.getDescription());

        MvcResult result = mockMvc.perform(
                        post("/categories")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryDto.class
        );

        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDescription(), actual.getDescription());
        EqualsBuilder.reflectionEquals(actual, expected, "id");
    }

    @Test
    @WithMockUser(roles = "USER")
    void createCategory_ValidRequestDto_ReturnStatusForbidden() throws Exception {
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto()
                .setName("Category Test")
                .setDescription("Description Test");

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/categories")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    void getAll_CategoriesInDatabase_Success() throws Exception {
        MvcResult result = mockMvc.perform(get("/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsString());
        CategoryDto[] categoryDtos = objectMapper.treeToValue(root.get("content"), CategoryDto[].class);

        assertEquals(3, categoryDtos.length);
        Assertions.assertNotNull(categoryDtos[1].getId());
        Assertions.assertNotNull(categoryDtos[2].getName());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getOne_CategoryInDatabase_Success() throws Exception {
        CategoryDto expected = new CategoryDto()
                .setName("Category 1")
                .setDescription("Description 1");

        MvcResult result = mockMvc.perform(get("/categories/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryDto.class
        );

        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDescription(), actual.getDescription());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Sql(scripts = "classpath:database/categories/restore-updated-category-id2.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void update_Category_Success() throws Exception {
        CreateCategoryRequestDto expected = new CreateCategoryRequestDto()
                .setName("TEST Name")
                .setDescription("TEST Description");

        String jsonRequest = objectMapper.writeValueAsString(expected);

        MvcResult result = mockMvc.perform(put("/categories/2")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryDto.class
        );

        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDescription(), actual.getDescription());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Sql(scripts = "classpath:database/categories/restore-deleted-category-id2.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void delete_CategoryById_Success() throws Exception {
        mockMvc.perform(delete("/categories/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void find_BookByCategoryId_Success() throws Exception {
        MvcResult result = mockMvc.perform(get("/categories/1/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        BookDto[] bookDtos = objectMapper.readValue(content, BookDto[].class);

        assertEquals(2, bookDtos.length);
        assertEquals("Book 1", bookDtos[0].getTitle());
        assertEquals("Book 4", bookDtos[1].getTitle());
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
}
