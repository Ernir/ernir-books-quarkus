package ernir.net;

import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public final class BookService {
  private final List<Book> books;
  private final BookData bookData;

  public BookService(BookData bookData) {
    books =
        List.of(
            new Book(
                214240402,
                "Deathseed",
                new Author("Sarah Lin", "Sarah", "Lin"),
                List.of(),
                Optional.empty(),
                Optional.empty(),
                4.52,
                Optional.empty(),
                "Kindle Edition",
                Optional.of(465),
                Optional.of(2024),
                Optional.empty(),
                Optional.of(
                    new ReadingInfo(
                        Optional.empty(), LocalDate.of(2024, 7, 28), LocalDate.of(2024, 7, 21)))));
    this.bookData = bookData;
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
