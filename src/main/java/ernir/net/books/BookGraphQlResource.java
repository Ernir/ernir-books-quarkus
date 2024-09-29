package ernir.net.books;

import ernir.net.books.models.Author;
import ernir.net.books.models.Book;
import ernir.net.books.models.SearchResult;
import ernir.net.books.models.Series;
import ernir.net.books.models.SeriesMembership;
import io.smallrye.graphql.api.Context;
import java.util.List;
import java.util.Optional;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Name;
import org.eclipse.microprofile.graphql.Query;
import org.eclipse.microprofile.graphql.Source;

@GraphQLApi
public final class BookGraphQlResource {
  private final BookService service;

  BookGraphQlResource(BookService service) {
    this.service = service;
  }

  @Query("allBooks")
  @Description("Get all Books")
  public List<Book> getAllBooks() {
    return service.findAllBooks();
  }

  @Query
  @Description("Search for books, authors or publishers")
  public List<SearchResult> search(String query) {
    return service.search(query);
  }

  public List<Book> books(@Source Author author) {
    return service.getBooksByAuthor(author);
  }

  @Query
  public Optional<Series> seriesByName(String name) {
    return service.getSeriesByName(name);
  }

  public List<SeriesMembership> books(@Source Series series) {
    return service.findBooksOfSeries(series);
  }

  public Book book(@Source SeriesMembership membership) {
    return service.findBookOfSeriesMembership(membership);
  }

  @Query
  public Optional<Book> bookByTitle(Context context, @Name("titleFull") String bookTitle) {
    return service.findBookByTitle(bookTitle);
  }
}
