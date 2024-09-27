package ernir.net.books.models;

public record Author(String fullName, String firstName, String lastName) implements SearchResult {}
