package book.store.dto.category;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateCategoryRequestDto {
    @Schema(hidden = true)
    private Long id;
    @NotNull(message = "Name cannot be null")
    private String name;
    private String description;
}

