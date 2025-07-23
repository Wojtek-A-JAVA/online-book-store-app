package book.store.dto;

public record BookSearchParametersDto(String[] title, String[] author, String[] isbn) {
}
