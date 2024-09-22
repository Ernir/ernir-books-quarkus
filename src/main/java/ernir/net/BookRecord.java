package ernir.net;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public record BookRecord(
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
    Optional<LocalDate> dateAdded,
    String bookshelves,
    String bookshelvesWithPositions,
    String exclusiveShelf,
    String myReview,
    String spoiler,
    String privateNotes,
    int readCount,
    String owned) {}
