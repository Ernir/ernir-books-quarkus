package ernir.net;

import java.util.List;
import java.util.Optional;

public record Book(
    long id,
    String title,
    Author author,
    List<Author> additionalAuthors,
    Optional<String> isbn,
    Optional<String> isbn13,
    double averageRating,
    Optional<Publisher> publisher,
    String binding,
    Optional<Integer> pages,
    Optional<Integer> yearPublished,
    Optional<Integer> originalPublicationYear,
    Optional<ReadingInfo> readingInfo) {}
