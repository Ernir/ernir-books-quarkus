package ernir.net;

import ernir.net.books.models.Author;
import ernir.net.books.models.Book;
import ernir.net.books.models.SearchResult;
import io.smallrye.graphql.api.Context;
import java.util.List;
import java.util.Optional;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Name;
import org.eclipse.microprofile.graphql.Query;
import org.eclipse.microprofile.graphql.Source;

@GraphQLApi
public class BookResource {
  private final BookService service;

  public BookResource(BookService service) {
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
  public Optional<Book> bookByTitle(Context context, @Name("titleFull") String bookTitle) {
    return service.findBookByTitle(bookTitle);
  }
}
