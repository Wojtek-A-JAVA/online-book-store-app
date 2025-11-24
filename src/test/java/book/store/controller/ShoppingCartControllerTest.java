package book.store.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import book.store.dto.cartitem.CartItemDto;
import book.store.dto.shoppingcart.CreateShoppingCartRequestDto;
import book.store.dto.shoppingcart.ShoppingCartDto;
import book.store.dto.shoppingcart.UpdateCartItemRequestDto;
import book.store.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ShoppingCartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Sql(scripts = "classpath:database/shoppingcart/add-shopping-cart.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/shoppingcart/delete-added-shopping-cart.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getShoppingCart_ExistingInDatabase_Success() throws Exception {
        User user = new User()
                .setId(1L)
                .setEmail("user@test.com")
                .setPassword("test");
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user, null, List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        MvcResult result = mockMvc.perform(get("/cart")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ShoppingCartDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), ShoppingCartDto.class
        );

        Assertions.assertNotNull(actual);
        assertEquals(1L, actual.getId());
        assertEquals(1L, actual.getUserId());
    }

    @Test
    @Sql(scripts = "classpath:database/shoppingcart/add-shopping-cart.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/shoppingcart/delete-added-cart-item.sql",
            "classpath:database/shoppingcart/delete-added-shopping-cart.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void addItemToShoppingCart_ExistingInDataBase_Success() throws Exception {
        User user = new User()
                .setId(1L)
                .setEmail("user@test.com")
                .setPassword("test");
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user, null, List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        CreateShoppingCartRequestDto shoppingCartRequestDto = new CreateShoppingCartRequestDto()
                .setBookId(1L)
                .setQuantity(10);

        String jsonRequest = objectMapper.writeValueAsString(shoppingCartRequestDto);

        CartItemDto expected = new CartItemDto().setQuantity(shoppingCartRequestDto.getQuantity())
                .setBookId(shoppingCartRequestDto.getBookId()).setId(1L);


        MvcResult result = mockMvc.perform(
                        post("/cart")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CartItemDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CartItemDto.class
        );

        Assertions.assertNotNull(actual);
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
    void updateItem_ExistingInDataBase_Success() throws Exception {
        Long itemId = 1L;
        UpdateCartItemRequestDto itemRequestDto = new UpdateCartItemRequestDto().setQuantity(100);
        CartItemDto expected = new CartItemDto().setId(itemId).setBookId(1L).setQuantity(itemRequestDto.getQuantity());

        String jsonRequest = objectMapper.writeValueAsString(expected);

        MvcResult result = mockMvc.perform(put("/cart/cart-items/1")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CartItemDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CartItemDto.class
        );

        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
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
    void deleteItemFromShoppingCart_ExistingInDataBase_Success() throws Exception {
        mockMvc.perform(delete("/cart/cart-items/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
