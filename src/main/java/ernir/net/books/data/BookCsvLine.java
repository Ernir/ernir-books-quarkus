package ernir.net.books.data;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public record BookCsvLine(
    String id,
    String title,
    String author,
    String authorLastFirst,
    List<String> additionalAuthors,
    Optional<String> isbn,
    Optional<String> isbn13,
    int myRating,
    double averageRating,
    String publisher,
    String binding,
    Optional<Integer> numberOfPages,
    Optional<Integer> yearPublished,
    Optional<Integer> originalPublicationYear,
    Optional<LocalDate> dateRead,
    Optional<LocalDate> dateAdded) {}
