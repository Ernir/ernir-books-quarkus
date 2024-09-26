package ernir.net;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public final class BookService {
  private final List<Book> books;
  private final BookData bookData;

  public BookService(BookData bookData) {
    this.bookData = bookData;
    this.books = bookData.getBooks().stream().map(BookService::from).toList();
  }

  private static Book from(BookRecord record) {
    return new Book(
        Integer.parseInt(record.id()),
        record.title(),
        from(record.author(), record.authorLastFirst()),
        // TODO figure out additional author parsing
        List.of(),
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

  private static Author from(String authorName, String authorLastFirst) {
    List<String> names = Arrays.stream(authorLastFirst.split(",")).map(String::strip).toList();
    return new Author(authorName, names.get(1), names.get(0));
  }

  public List<Book> findAllBooks() {
    // TODO populate the list
    bookData.getBooks();
    return books;
  }

  public Optional<Book> findBookByTitle(String title) {
    return books.stream().filter(book -> book.title().equalsIgnoreCase(title)).findFirst();
  }

  public List<SearchResult> search(String query) {
    return List.of();
  }

  public List<Book> getBooksByAuthor(Author author) {
    return List.of();
  }
}
