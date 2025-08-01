package book.store.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record BookSearchParametersDto(@Schema(name = "title", type = "string") String[] title,
                                      @Schema(name = "author", type = "string")String[] author,
                                      @Schema(name = "isbn", type = "string")String[] isbn) {
}
