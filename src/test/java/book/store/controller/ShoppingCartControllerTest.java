package book.store.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import book.store.dto.cartitem.CartItemDto;
import book.store.dto.shoppingcart.CreateShoppingCartRequestDto;
import book.store.dto.shoppingcart.ShoppingCartDto;
import book.store.dto.shoppingcart.UpdateCartItemRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ShoppingCartControllerTest {
    private static final Long EXISTING_SHOPPING_CART_ID = 1L;
    private static final Long EXISTING_CART_ITEM_ID = 1L;
    private static final Long EXISTING_USER_ID = 2L;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Sql(scripts = "classpath:database/shoppingcart/add-shopping-cart.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/shoppingcart/delete-added-shopping-cart.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "john.doe@google.com", roles = "USER")
    void getShoppingCart_ExistingInDatabase_Success() throws Exception {
        MvcResult result = mockMvc.perform(get("/cart")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ShoppingCartDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), ShoppingCartDto.class
        );

        assertNotNull(actual);
        assertEquals(EXISTING_SHOPPING_CART_ID, actual.getId());
        assertEquals(EXISTING_USER_ID, actual.getUserId());
    }

    @Test
    @Sql(scripts = "classpath:database/shoppingcart/add-shopping-cart.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/shoppingcart/delete-added-cart-item.sql",
            "classpath:database/shoppingcart/delete-added-shopping-cart.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "john.doe@google.com", roles = "USER")
    void addItemToShoppingCart_ExistingInDatabase_Success() throws Exception {
        CreateShoppingCartRequestDto shoppingCartRequestDto = new CreateShoppingCartRequestDto()
                .setBookId(1L)
                .setQuantity(10);

        String jsonRequest = objectMapper.writeValueAsString(shoppingCartRequestDto);

        CartItemDto expected = new CartItemDto().setQuantity(shoppingCartRequestDto.getQuantity())
                .setBookId(shoppingCartRequestDto.getBookId());


        MvcResult result = mockMvc.perform(
                        post("/cart")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CartItemDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CartItemDto.class
        );

        assertNotNull(actual);
        assertEquals(expected.getBookId(), actual.getBookId());
        assertEquals(expected.getQuantity(), actual.getQuantity());
    }

    @Test
    @WithMockUser(roles = "USER")
    @Sql(scripts = {"classpath:database/shoppingcart/add-shopping-cart.sql",
            "classpath:database/shoppingcart/add-cart-item.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/shoppingcart/delete-added-cart-item.sql",
            "classpath:database/shoppingcart/delete-added-shopping-cart.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateItem_ExistingInDatabase_Success() throws Exception {
        UpdateCartItemRequestDto itemRequestDto = new UpdateCartItemRequestDto().setQuantity(100);
        CartItemDto expected = new CartItemDto().setId(EXISTING_CART_ITEM_ID).setBookId(1L)
                .setQuantity(itemRequestDto.getQuantity());

        String jsonRequest = objectMapper.writeValueAsString(itemRequestDto);

        MvcResult result = mockMvc.perform(put("/cart/cart-items/{id}", EXISTING_CART_ITEM_ID)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CartItemDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CartItemDto.class
        );

        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertEquals(expected.getBookId(), actual.getBookId());
        assertEquals(expected.getQuantity(), actual.getQuantity());
    }

    @Test
    @WithMockUser(roles = "USER")
    @Sql(scripts = {"classpath:database/shoppingcart/add-shopping-cart.sql",
            "classpath:database/shoppingcart/add-cart-item.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/shoppingcart/delete-added-shopping-cart.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteItemFromShoppingCart_ExistingInDatabase_Success() throws Exception {

        mockMvc.perform(delete("/cart/cart-items/{id}", EXISTING_CART_ITEM_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
