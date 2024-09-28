package ernir.net.books.models;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public record Book(
    long id,
    String titleFull,
    String titleShort,
    Author author,
    List<Author> additionalAuthors,
    Optional<SeriesMembership> partOfSeries,
    Optional<String> isbn,
    Optional<String> isbn13,
    double averageRating,
    Optional<Publisher> publisher,
    String binding,
    Optional<Integer> pages,
    Optional<Integer> yearPublished,
    Optional<Integer> originalPublicationYear,
    Optional<Integer> myRating,
    Optional<LocalDate> myDateRead,
    Optional<LocalDate> myDateAdded)
    implements SearchResult {}
