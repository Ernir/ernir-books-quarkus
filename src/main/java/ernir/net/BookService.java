package ernir.net;

import static java.util.stream.Collectors.*;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Predicate;

@ApplicationScoped
public final class BookService {
  private final List<Book> allBooks;

  public BookService(BookCsvParser bookCsvParser) {
    this.allBooks = bookCsvParser.getBooks().stream().map(BookService::from).toList();
  }

  private static Book from(BookRecord record) {
    return new Book(
        Integer.parseInt(record.id()),
        record.title(),
        from(record.author(), record.authorLastFirst()),
        record.additionalAuthors().stream().filter(isNotBlank()).map(BookService::from).toList(),
        record.isbn(),
        record.isbn13(),
        record.averageRating(),
        Optional.of(new Publisher(record.publisher()))
            .filter(publisher -> !publisher.name().isBlank()),
        record.binding(),
        record.numberOfPages(),
        record.yearPublished(),
        record.originalPublicationYear(),
        Optional.of(record.myRating()),
        record.dateRead(),
        record.dateAdded());
  }

  private static Author from(String fullName) {
    List<String> nameComponents = Arrays.stream(fullName.split(" ")).filter(isNotBlank()).toList();
    String lastName = nameComponents.getLast();
    String firstName = String.join(" ", nameComponents.subList(0, nameComponents.size() - 1));
    return new Author(firstName + " " + lastName, firstName, lastName);
  }

  private static Author from(String authorName, String authorLastFirst) {
    List<String> names = Arrays.stream(authorLastFirst.split(",")).map(String::strip).toList();
    String cleanedAuthorName =
        Arrays.stream(authorName.split(" ")).filter(isNotBlank()).collect(joining(" "));
    return new Author(cleanedAuthorName, names.get(1), names.get(0));
  }

  public List<Book> findAllBooks() {
    return allBooks;
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
