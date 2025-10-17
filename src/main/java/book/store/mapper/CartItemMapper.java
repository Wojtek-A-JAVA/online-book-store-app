package book.store.mapper;

import book.store.config.MapperConfig;
import book.store.dto.cartitem.CartItemDto;
import book.store.dto.cartitem.CartItemDtoWithTitle;
import book.store.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = BookMapper.class)
public interface CartItemMapper {
    @Mapping(target = "bookId", source = "book.id")
    CartItemDto toDto(CartItem cartItem);

    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "bookTitle", source = "book.title")
    CartItemDtoWithTitle toDtoWithTitle(CartItem cartItem);
}
