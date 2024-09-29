package ernir.net.books;

import static java.util.stream.Collectors.*;

import ernir.net.books.data.BookCsvLine;
import ernir.net.books.data.BookCsvParser;
import ernir.net.books.models.Author;
import ernir.net.books.models.Book;
import ernir.net.books.models.Publisher;
import ernir.net.books.models.SearchResult;
import ernir.net.books.models.Series;
import ernir.net.books.models.SeriesMembership;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ApplicationScoped
final class BookService {
  private static final Pattern TITLE_PATTERN =
      Pattern.compile("(?<shortTitle>.*)\\s\\((?<seriesDescription>[^(]*)\\)");

  private final List<Book> allBooks;

  BookService(BookCsvParser bookCsvParser) {
    this.allBooks = bookCsvParser.getBooks().stream().map(BookService::from).toList();
  }

  private static Book from(BookCsvLine csvLine) {
    return new Book(
        Integer.parseInt(csvLine.id()),
        csvLine.title(),
        shortTitle(csvLine.title()),
        authorFromName(csvLine.author(), csvLine.authorLastFirst()),
        csvLine.additionalAuthors().stream()
            .filter(isNotBlank())
            .map(BookService::authorFromName)
            .toList(),
        fromBookTitle(csvLine.title()),
        csvLine.isbn(),
        csvLine.isbn13(),
        csvLine.averageRating(),
        Optional.of(new Publisher(csvLine.publisher()))
            .filter(publisher -> !publisher.name().isBlank()),
        csvLine.binding(),
        csvLine.numberOfPages(),
        csvLine.yearPublished(),
        csvLine.originalPublicationYear(),
        Optional.of(csvLine.myRating()).filter(rating -> rating != 0),
        csvLine.dateRead(),
        csvLine.dateAdded());
  }

  private static Author authorFromName(String fullName) {
    List<String> nameComponents = Arrays.stream(fullName.split(" ")).filter(isNotBlank()).toList();
    String lastName = nameComponents.getLast();
    String firstName = String.join(" ", nameComponents.subList(0, nameComponents.size() - 1));
    return new Author(firstName + " " + lastName, firstName, lastName);
  }

  private static Author authorFromName(String authorName, String authorLastFirst) {
    List<String> names = Arrays.stream(authorLastFirst.split(",")).map(String::strip).toList();
    String cleanedAuthorName =
        Arrays.stream(authorName.split(" ")).filter(isNotBlank()).collect(joining(" "));
    return new Author(cleanedAuthorName, names.get(1), names.get(0));
  }

  private static String shortTitle(String fullBookTitle) {
    Matcher matcher = TITLE_PATTERN.matcher(fullBookTitle);
    if (matcher.matches()) {
      return matcher.group("shortTitle");
    } else {
      return fullBookTitle;
    }
  }

  private static Optional<SeriesMembership> fromBookTitle(String fullBookTitle) {
    Matcher matcher = TITLE_PATTERN.matcher(fullBookTitle);
    return Optional.of(matcher)
        .filter(Matcher::matches)
        .map(m -> m.group("seriesDescription"))
        .map(BookService::parseSeriesDescription);
  }

  private static SeriesMembership parseSeriesDescription(String seriesDescription) {
    String[] commaSeparated = seriesDescription.split(",");
    String seriesName;
    String seriesPosition = null;
    if (commaSeparated.length == 2) {
      seriesName = commaSeparated[0].trim();
      seriesPosition = commaSeparated[1].trim().replaceAll("#", "");
    } else if (seriesDescription.contains("Book")) {
      String[] seriesNameComponents = seriesDescription.split("Book");
      seriesName = seriesNameComponents[0].trim();
      seriesPosition = seriesNameComponents[1].trim();
    } else {
      ArrayList<String> spaceSeparated =
          Arrays.stream(seriesDescription.trim().split(" "))
              .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
      if (spaceSeparated.getLast().contains("#")) {
        String last = spaceSeparated.removeLast();
        seriesName = String.join(" ", spaceSeparated);
        seriesPosition = last.replaceAll("#", "").trim();
      } else {
        seriesName = seriesDescription;
      }
    }
    return new SeriesMembership(new Series(seriesName), Optional.ofNullable(seriesPosition));
  }

  List<Book> findAllBooks() {
    return allBooks;
  }

  Optional<Series> getSeriesByName(String name) {
    return allBooks.stream()
        .map(Book::partOfSeries)
        .flatMap(Optional::stream)
        .map(SeriesMembership::series)
        .filter(series -> series.name().equalsIgnoreCase(name))
        .findFirst();
  }

  List<SeriesMembership> findBooksOfSeries(Series series) {
    return allBooks.stream()
        .map(Book::partOfSeries)
        .flatMap(Optional::stream)
        .filter(sm -> sm.series().name().equalsIgnoreCase(series.name()))
        .toList();
  }

  Book findBookOfSeriesMembership(SeriesMembership membership) {
    return allBooks.stream()
        .filter(
            book ->
                book.partOfSeries().isPresent()
                    && book.partOfSeries().orElseThrow().equals(membership))
        .findFirst()
        .orElseThrow(
            () ->
                new IllegalArgumentException("Book not found for series membership " + membership));
  }

  Optional<Book> findBookByTitle(String title) {
    return allBooks.stream().filter(book -> book.titleFull().equalsIgnoreCase(title)).findFirst();
  }

  List<SearchResult> search(String query) {
    String lowerCaseQuery = query.toLowerCase(Locale.ROOT);
    ArrayList<SearchResult> results = new ArrayList<>();
    results.addAll(
        allBooks.stream()
            .filter(book -> book.titleFull().toLowerCase().contains(lowerCaseQuery))
            .toList());
    results.addAll(
        allBooks.stream()
            .map(Book::author)
            .filter(author -> author.fullName().toLowerCase().contains(lowerCaseQuery))
            .distinct()
            .toList());
    results.addAll(
        allBooks.stream()
            .map(Book::publisher)
            .flatMap(Optional::stream)
            .filter(publisher -> publisher.name().toLowerCase().contains(lowerCaseQuery))
            .distinct()
            .toList());
    return results;
  }

  List<Book> getBooksByAuthor(Author author) {
    return allBooks.stream().filter(book -> book.author().equals(author)).toList();
  }

  private static Predicate<String> isNotBlank() {
    return s -> !s.isBlank();
  }
}
