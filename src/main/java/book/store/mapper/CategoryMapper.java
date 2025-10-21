package book.store.mapper;

import book.store.config.MapperConfig;
import book.store.dto.category.CategoryDto;
import book.store.dto.category.CreateCategoryRequestDto;
import book.store.model.Category;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    @BeanMapping(ignoreByDefault = true)
    Category toEntity(CreateCategoryRequestDto categoryDto);
}
