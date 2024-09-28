package ernir.net;

import static java.util.stream.Collectors.*;

import ernir.net.books.data.BookCsvParser;
import ernir.net.books.data.BookRecord;
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
public final class BookService {
  private static final Pattern TITLE_PATTERN =
      Pattern.compile("(?<shortTitle>.*)\\s\\((?<seriesDescription>[^(]*)\\)");

  private final List<Book> allBooks;

  public BookService(BookCsvParser bookCsvParser) {
    this.allBooks = bookCsvParser.getBooks().stream().map(BookService::from).toList();
  }

  private static Book from(BookRecord record) {
    return new Book(
        Integer.parseInt(record.id()),
        record.title(),
        shortTitle(record.title()),
        authorFromName(record.author(), record.authorLastFirst()),
        record.additionalAuthors().stream()
            .filter(isNotBlank())
            .map(BookService::authorFromName)
            .toList(),
        fromBookTitle(record.title()),
        record.isbn(),
        record.isbn13(),
        record.averageRating(),
        Optional.of(new Publisher(record.publisher()))
            .filter(publisher -> !publisher.name().isBlank()),
        record.binding(),
        record.numberOfPages(),
        record.yearPublished(),
        record.originalPublicationYear(),
        Optional.of(record.myRating()).filter(rating -> rating != 0),
        record.dateRead(),
        record.dateAdded());
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
      if (spaceSeparated.size() > 1 && spaceSeparated.getLast().contains("#")) {
        String last = spaceSeparated.removeLast();
        seriesName = String.join(" ", spaceSeparated);
        seriesPosition = last.replaceAll("#", "").trim();
      } else {
        seriesName = seriesDescription;
      }
    }
    return new SeriesMembership(new Series(seriesName), Optional.ofNullable(seriesPosition));
  }

  public List<Book> findAllBooks() {
    return allBooks;
  }

  public Optional<Series> getSeriesByName(String name) {
    return allBooks.stream()
        .map(Book::partOfSeries)
        .flatMap(Optional::stream)
        .map(SeriesMembership::series)
        .filter(series -> series.name().equalsIgnoreCase(name))
        .findFirst();
  }

  public List<SeriesMembership> findBooksOfSeries(Series series) {
    return allBooks.stream()
        .map(Book::partOfSeries)
        .flatMap(Optional::stream)
        .filter(
            seriesMembership -> seriesMembership.series().name().equalsIgnoreCase(series.name()))
        .toList();
  }

  public Optional<Book> findBookByTitle(String title) {
    return allBooks.stream().filter(book -> book.titleFull().equalsIgnoreCase(title)).findFirst();
  }

  public List<SearchResult> search(String query) {
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

  public List<Book> getBooksByAuthor(Author author) {
    return allBooks.stream().filter(book -> book.author().equals(author)).toList();
  }

  private static Predicate<String> isNotBlank() {
    return s -> !s.isBlank();
  }
}
